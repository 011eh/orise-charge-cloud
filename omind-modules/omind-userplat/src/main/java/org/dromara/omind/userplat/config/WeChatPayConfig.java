package org.dromara.omind.userplat.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import org.dromara.omind.userplat.api.config.WxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatPayConfig {

    @Bean
    public RSAAutoCertificateConfig rsaAutoCertificateConfig(WxProperties wxProperties) {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(wxProperties.getMerchantId())
                .privateKey(wxProperties.getPrivateKey())
                .merchantSerialNumber(wxProperties.getMerchantSerialNumber())
                .apiV3Key(wxProperties.getApiV3Key())
                .build();
    }

    @Bean
    public NativePayService nativePayService(RSAAutoCertificateConfig config) {
        return new NativePayService.Builder()
                .config(config)
                .build();
    }
    
    @Bean
    public JsapiService jsapiService(RSAAutoCertificateConfig config) {
        return new JsapiService.Builder()
                .config(config)
                .build();
    }
} 
