package org.dromara.omind.tcpplat.web.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.service.IPileCtrlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PileCtrlController {

    private final IPileCtrlService pileCtrlService;

    @GetMapping("test/{pileCode}/{msgType}")
    public Message test(@PathVariable String pileCode, @PathVariable String msgType) {
        Integer num = Integer.valueOf(msgType, 16);
        Message message = null;
        switch (num) {
            case 0x12:
                message = pileCtrlService.ctrl12(pileCode);
                break;
            case 0x34:
                message = pileCtrlService.ctrl34(pileCode);
                break;
            case 0x36:
                message = pileCtrlService.ctrl36(pileCode);
                break;
            case Integer.MAX_VALUE:
                break;
        }
        return message;
    }
}
