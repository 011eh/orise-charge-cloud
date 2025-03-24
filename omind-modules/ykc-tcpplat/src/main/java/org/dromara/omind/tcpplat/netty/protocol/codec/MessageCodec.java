package org.dromara.omind.tcpplat.netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;
import org.dromara.omind.tcpplat.netty.common.constant.UpMsgType;
import org.dromara.omind.tcpplat.netty.exception.BusinessException;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.core.MessageRegistry;
import org.dromara.omind.tcpplat.netty.protocol.model.MsgLogEntry;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.util.CrcUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.AUTHENTICATED;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        Class<?> msgClass = msg.getClass();
        MessageType messageType = msgClass.getAnnotation(MessageType.class);
        byte type = messageType.type();
        ByteBuf messageBody = ctx.alloc().buffer();
        List<Field> fields = getProtocolFields(msgClass);
        for (Field field : fields) {
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            field.setAccessible(true);
            Object value = field.get(msg);
            writeField(messageBody, value, protocolField);
        }
        ByteBuf buffer = ctx.alloc().buffer();

        // 起始标志
        buffer.writeByte(ProtocolConstant.START_FLAG);

        // 2 （序列号域）+ 1 （加密标志）+ 1 +（帧类型标志）+ （数据长度）
        int dataLength = 4 + messageBody.readableBytes();

        if (dataLength > ProtocolConstant.MAX_DATA_LENGTH) {
            throw new BusinessException("");
        }

        buffer.writeByte(dataLength);

        // 序列号域
        buffer.writeShortLE(msg.getSequenceId());

        // 加密标志
        buffer.writeByte(ProtocolConstant.ENCRYPT_NONE);

        // 帧类型标志
        buffer.writeByte(type);

        //消息体
        buffer.writeBytes(messageBody);

        // 从序列号域到数据域的 CRC 校验
        ByteBuf dataForCrc = buffer.slice(2, dataLength);
        int crc = CrcUtil.calculateCRC(dataForCrc);

        // 帧校验域
        buffer.writeShortLE(crc);
        messageBody.release();
        out.add(buffer);
        String rawHex = ByteBufUtil.hexDump(buffer);
        out.add(new MsgLogEntry(rawHex, msg));
    }

    private void writeField(ByteBuf out, Object value, ProtocolField protocolField) {
        if (value == null) {
            return;
        }
        ProtocolField.DataType dataType = protocolField.type();
        int length = protocolField.length();
        ProtocolField.ByteOrder byteOrder = protocolField.BYTE_ORDER();
        int scale = protocolField.scale();
        int offset = protocolField.offset();

        if (protocolField.listSize() >= 0) {
            List<?> list = (List<?>) value;
            for (Object element : list) {
                Object scaledValue = element;
                if (element instanceof Number) {
                    if (scale > 1) {
                        scaledValue = scaleElement((Number) element, scale);
                    }
                    if (offset != 0) {
                        scaledValue = applyOffset((Number) scaledValue, offset, true);
                    }
                }
                writeSingleValue(out, scaledValue, dataType, byteOrder, protocolField);
            }
            return;
        }

        if (value instanceof Number) {
            if (scale > 1) {
                value = scaleElement((Number) value, scale);
            }
            if (offset != 0) {
                value = applyOffset((Number) value, offset, true);
            }
        }

        writeSingleValue(out, value, dataType, byteOrder, protocolField);
    }

    private void writeSingleValue(ByteBuf out, Object value, ProtocolField.DataType dataType,
                                  ProtocolField.ByteOrder byteOrder, ProtocolField protocolField) {
        switch (dataType) {
            case BIN:
                writeBcd(out, value.toString(), protocolField.length());
                break;
            case STRING:
                writeString(out, value.toString(), protocolField.length(), protocolField.charset());
                break;
            case INT8:
            case UINT8:
                if (value instanceof ProtocolCode) {
                    out.writeByte(((ProtocolCode) value).getCode());
                } else {
                    out.writeByte(((Number) value).byteValue());
                }
                break;
            case INT16:
            case UINT16:
                if (byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN) {
                    out.writeShortLE(((Number) value).shortValue());
                } else {
                    out.writeShort(((Number) value).shortValue());
                }
                break;
            case INT32:
            case UINT32:
                if (byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN) {
                    out.writeIntLE(((Number) value).intValue());
                } else {
                    out.writeInt(((Number) value).intValue());
                }
                break;
            case FLOAT:
                if (byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN) {
                    out.writeFloatLE(((Number) value).floatValue());
                } else {
                    out.writeFloat(((Number) value).floatValue());
                }
                break;
            case DOUBLE:
                if (byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN) {
                    out.writeDoubleLE(((Number) value).doubleValue());
                } else {
                    out.writeDouble(((Number) value).doubleValue());
                }
                break;
            case BYTES:
                writeBytes(out, (byte[]) value, protocolField.length());
                break;
            case ARBITRARY_INT:
                writeArbitraryInt(out, ((Number) value).longValue(), protocolField.length(), byteOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported data type: " + dataType);
        }
    }

    /**
     * 写入任意字节长度的整数值
     *
     * @param out       ByteBuf输出
     * @param value     要写入的长整数值
     * @param length    要写入的字节数
     * @param byteOrder 字节序
     */
    private void writeArbitraryInt(ByteBuf out, long value, int length, ProtocolField.ByteOrder byteOrder) {
        if (length <= 0 || length > 8) {
            throw new IllegalArgumentException("Invalid arbitrary int length: " + length);
        }

        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int shift = i * 8;
            bytes[byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? i : (length - 1 - i)] = (byte) ((value >> shift) & 0xFF);
        }

        out.writeBytes(bytes);
    }

    private void writeBcd(ByteBuf out, String value, int length) {
        String paddedValue = String.format("%" + (length * 2) + "s", value).replace(' ', '0');

        if (paddedValue.length() > length * 2) {
            paddedValue = paddedValue.substring(paddedValue.length() - length * 2);
        }

        for (int i = 0; i < paddedValue.length(); i += 2) {
            int highNibble = Character.digit(paddedValue.charAt(i), 16);
            int lowNibble = (i + 1 < paddedValue.length()) ? Character.digit(paddedValue.charAt(i + 1), 16) : 0;
            out.writeByte((highNibble << 4) | lowNibble);
        }
    }

    private void writeString(ByteBuf out, String value, int length, String charsetName) {
        byte[] bytes = value.getBytes(Charset.forName(charsetName));
        byte[] paddedBytes = new byte[length];

        System.arraycopy(bytes, 0, paddedBytes, 0, Math.min(bytes.length, length));

        out.writeBytes(paddedBytes);
    }

    private void writeBytes(ByteBuf out, byte[] bytes, int length) {
        byte[] paddedBytes = new byte[length];

        System.arraycopy(bytes, 0, paddedBytes, 0, Math.min(bytes.length, length));

        out.writeBytes(paddedBytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (in.readByte() != 0x68) {
            log.info("标志位不为0x68");
            return;
        }
        int dataLength = in.readUnsignedByte();
        ByteBuf data = in.readBytes(dataLength);
        int receivedCrc = in.readUnsignedShortLE();
        int calculatedCrc = CrcUtil.calculateCRC(data);
        if (calculatedCrc != receivedCrc) {
            log.info("CRC 不一致：期望：{}，接收到：{}", Integer.toHexString(calculatedCrc), Integer.toHexString(receivedCrc));
            return;
        }

        int sequenceId = data.readUnsignedShortLE();
        byte encryptFlag = data.readByte();
        byte type = data.readByte();

        if (type != UpMsgType.LOGIN_01 && !ctx.channel().hasAttr(AUTHENTICATED)) {
            ctx.channel().remoteAddress();
            log.info("未认证连接：{}，报文类型：0x{}", ctx.channel().remoteAddress(), Integer.toHexString(type));
        }

        Class<? extends Message> clazz = MessageRegistry.getMessage(type);
        Message message = clazz.getDeclaredConstructor().newInstance();
        List<Field> fields = getProtocolFields(clazz);
        for (Field field : fields) {
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            Object value = readField(data, protocolField);
            field.setAccessible(true);
            if (field.getType() == ProtocolCode.class && protocolField.options().length > 0) {
                int codeValue = ((Number) value).intValue();
                for (ProtocolField.Option option : protocolField.options()) {
                    if (option.code() == codeValue) {
                        field.set(message, new ProtocolCode(option.code(), option.desc()));
                    }
                }
                continue;
            }
            field.set(message, applyScale(value, protocolField.scale()));
        }
        message.setSequenceId(sequenceId);
        in.resetReaderIndex();
        String rawHex = ByteBufUtil.hexDump(in);
        out.add(new MsgLogEntry(rawHex, message));
        out.add(message);
    }

    private List<Field> getProtocolFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtocolField.class)) {
                fields.add(field);
            }
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtocolField.class)) {
                fields.add(field);
            }
        }
        fields.sort(Comparator.comparing(field -> field.getAnnotation(ProtocolField.class).index()));
        return fields;
    }

    private Object readField(ByteBuf in, ProtocolField protocolField) {
        ProtocolField.DataType dataType = protocolField.type();
        int length = protocolField.length();
        ProtocolField.ByteOrder byteOrder = protocolField.BYTE_ORDER();
        int scale = protocolField.scale();
        int offset = protocolField.offset();

        if (protocolField.listSize() >= 0) {
            List<Object> list = new ArrayList<>(protocolField.listSize());
            for (int i = 0; i < protocolField.listSize(); i++) {
                Object value = readSingleValue(in, dataType, byteOrder, length, protocolField);
                value = applyScale(value, scale);
                if (value instanceof Number && offset != 0) {
                    value = applyOffset((Number) value, offset, false);
                }
                list.add(value);
            }
            return list;
        }

        Object value = readSingleValue(in, dataType, byteOrder, length, protocolField);
        value = applyScale(value, scale);
        if (value instanceof Number && offset != 0) {
            value = applyOffset((Number) value, offset, false);
        }
        return value;
    }

    private Object readSingleValue(ByteBuf in, ProtocolField.DataType dataType,
                                   ProtocolField.ByteOrder byteOrder, int length,
                                   ProtocolField protocolField) {
        switch (dataType) {
            case BIN:
                return readBcd(in, length);
            case STRING:
                return readString(in, length, protocolField.charset());
            case INT8:
            case UINT8:
                return in.readByte();
            case INT16:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readShortLE() : in.readShort();
            case UINT16:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readUnsignedShortLE() : in.readUnsignedShort();
            case INT32:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readIntLE() : in.readInt();
            case UINT32:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readUnsignedIntLE() : in.readUnsignedInt();
            case FLOAT:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readFloatLE() : in.readFloat();
            case DOUBLE:
                return byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? in.readDoubleLE() : in.readDouble();
            case BYTES:
                return readBytes(in, length);
            case BIT:
                return readBits(in, protocolField.bitOffset(), protocolField.bitLength());
            case ARBITRARY_INT:
                return readArbitraryInt(in, length, byteOrder);
            default:
                throw new UnsupportedOperationException("Unsupported data type: " + dataType);
        }
    }

    /**
     * 读取任意字节长度的整数值 long
     *
     * @param in        ByteBuf输入
     * @param length    要读取的字节数
     * @param byteOrder 字节序
     * @return 读取的长整数值
     */
    private Number readArbitraryInt(ByteBuf in, int length, ProtocolField.ByteOrder byteOrder) {
        if (length <= 0 || length > 8) {
            throw new IllegalArgumentException("Invalid arbitrary int length: " + length);
        }

        long result = 0;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        for (int i = 0; i < length; i++) {
            int shift = (byteOrder == ProtocolField.ByteOrder.LITTLE_ENDIAN ? i : (length - 1 - i)) * 8;
            result |= (bytes[i] & 0xFFL) << shift;
        }

        // 4字节及以下返回Integer，否则返回Long
        if (length <= 4) {
            return (int) result;
        } else {
            return result;
        }
    }

    private String readBcd(ByteBuf in, int length) {
        if (in.readableBytes() < length) return null;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private String readString(ByteBuf in, int length, String charset) {
        if (in.readableBytes() < length) return null;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return new String(bytes, Charset.forName(charset)).trim();
    }

    private byte[] readBytes(ByteBuf in, int length) {
        if (in.readableBytes() < length) return null;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return bytes;
    }

    private int readBits(ByteBuf in, int bitOffset, int bitLength) {
        int bitReadLength = bitOffset + bitLength;
        if (bitOffset < 0 || bitLength <= 0 || bitReadLength > 8) {
            throw new IllegalArgumentException("错误配置");
        }
        in.markReaderIndex();
        byte b = in.readByte();
        int mask = (0xFF >> (8 - bitLength)) << bitOffset;
        int result = (b & mask) >> bitOffset;
        if (bitReadLength != 8) {
            in.resetReaderIndex();
        }
        return result;
    }

    private Object applyScale(Object value, int scale) {
        if (value instanceof Number) {
            Number number = (Number) value;
            if (number instanceof Long) {
                return number.longValue() / (long) scale;
            } else if (number instanceof Integer) {
                return number.intValue() / scale;
            } else if (number instanceof Float) {
                return number.floatValue() / scale;
            } else if (number instanceof Double) {
                return number.doubleValue() / scale;
            } else {
                return number.doubleValue() / scale;
            }
        }
        return value;
    }

    private Object scaleElement(Number number, int scale) {
        if (number instanceof Integer) {
            return ((Integer) number) * scale;
        } else if (number instanceof Float) {
            return ((Float) number) * scale;
        } else if (number instanceof Double) {
            return ((Double) number) * scale;
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + number.getClass());
        }
    }

    private Object applyOffset(Number number, int offset, boolean forEncoding) {
        if (forEncoding) {
            if (number instanceof Integer) {
                return ((Integer) number) - offset;
            } else if (number instanceof Float) {
                return ((Float) number) - offset;
            } else if (number instanceof Double) {
                return ((Double) number) - offset;
            }
        } else {
            if (number instanceof Integer) {
                return ((Integer) number) + offset;
            } else if (number instanceof Float) {
                return ((Float) number) + offset;
            } else if (number instanceof Double) {
                return ((Double) number) + offset;
            }
        }
        return number;
    }
}
