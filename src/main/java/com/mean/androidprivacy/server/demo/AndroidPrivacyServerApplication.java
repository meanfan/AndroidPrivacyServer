package com.mean.androidprivacy.server.demo;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: AndroidPrivacyServerApplication
 * @Description: SpringBoot 启动入口类
 * @Author: MeanFan
 * @Create: 2020-04-04 15:23
 * @Version: 1.0
 **/
@SpringBootApplication
@ComponentScan("com.mean.androidprivacy.server")
public class AndroidPrivacyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AndroidPrivacyServerApplication.class, args);
    }

    /**
    * @Author: MeanFan
    * @Description: 解决上传文件大于10M出现连接重置的问题（https://mkyong.com/spring/spring-file-upload-and-connection-reset-issue/）
    * @Date: 11:47 2020/4/25 0025
    * @Param: []
    * @return: org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
    **/
    @Bean
    public TomcatServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 为无限制
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    }
}
