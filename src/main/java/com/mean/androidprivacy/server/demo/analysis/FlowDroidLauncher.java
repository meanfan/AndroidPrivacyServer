package com.mean.androidprivacy.server.demo.analysis;

import com.mean.androidprivacy.server.demo.util.Md5CalcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: FlowDroidLauncher
 * @Description:
 * @Author: MeanFan
 * @Create: 2020-05-23 15:30
 * @Version: 1.0
 **/
public class FlowDroidLauncher {
    @Autowired
    private static FlowDroidConfig flowDroidConfig;

    public static Path launch(MultipartFile uploadApkFile){
        try {
            // 获取文件的Md5值
            String Md5 = Md5CalcUtil.calcMD5(uploadApkFile.getInputStream());
            Path apkPath = Paths.get(String.format("%s/%s.apk", flowDroidConfig.getApkFileDir(), Md5));
            Path outputPath = Paths.get(String.format("%s/%s.xml",flowDroidConfig.getOutputFileDir(),Md5));
            Path logPath = Paths.get(String.format("%s/%s.log",flowDroidConfig.getLogFileDir(),Md5));
            if(!outputPath.toFile().exists()) {    //输出文件不存在才进行后续的分析
                if(!apkPath.toFile().exists()){     //apk文件不存在才写入磁盘
                    FileCopyUtils.copy(uploadApkFile.getInputStream(), new FileOutputStream(apkPath.toFile()));
                }
                FlowDroidRuntime flowDroidRuntime = new FlowDroidRuntime();
                flowDroidRuntime.setEnv(flowDroidConfig.getJavaCmd(),
                                        flowDroidConfig.getFlowDroidCmdJarWorkDir(),
                                        flowDroidConfig.getFlowDroidCmdJarFilePath(),
                                        flowDroidConfig.getAndroidSdkPlatformsDir(),
                                        flowDroidConfig.getSourcesAndSinksFilePath(),
                                        outputPath.toString(),
                                        logPath.toString());
                flowDroidRuntime.exec(apkPath.toString());
            }
            return outputPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
