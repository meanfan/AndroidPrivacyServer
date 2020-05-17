package com.mean.androidprivacy.server.demo.analysis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: FlowDroidConfig
 * @Description:
 * @Author: MeanFan
 * @Create: 2020-05-17 20:11
 * @Version: 1.0
 **/
@ConfigurationProperties(prefix = "flowdroid", ignoreInvalidFields = true)
@PropertySource("classpath:config/flowdroid.properties")
@Data
@Component
public class FlowDroidConfig {
    private String javaCmd;
    private String flowDroidCmdJarWorkDir;
    private String flowDroidCmdJarFilePath;
    private String androidSdkPlatformsDir;
    private String sourcesAndSinksFilePath;
    private String apkFileDir;
    private String outputFileDir;
    private String logFileDir;
}
