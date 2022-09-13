package java.com.bozong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther yinguoyu
 * @date 2022/9/13
 * @des spring boot 集成 es 步骤：
 *  1.引入依赖 ： spring-boot-starter-data-elasticsearch
 *  2.配置 es 地址：spring.elasticsearch.rest.uris , es 默认不开启账号密码验证，所以不需要配置，如果 es 开启了，需要配置
**/
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}