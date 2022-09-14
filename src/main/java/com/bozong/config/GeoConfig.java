package com.bozong.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinguoyu
 * @date 2022/9/14
 * @des 
**/
@Configuration
@Data
public class GeoConfig {

    /**
     * 百度 AK
     */
    @Value("${baidu.ak}")
    private String ak;

    /**
     * 地理编码 URL
     */
    @Value("${baidu.url}")
    private String address_to_logintudea_url;

    /**
     * 逆地理编码 URL
     */
    @Value("${baidu.rurl}")
    private String loginude_to_address_url;

}