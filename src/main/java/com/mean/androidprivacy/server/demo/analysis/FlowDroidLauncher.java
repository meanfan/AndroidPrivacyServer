package com.mean.androidprivacy.server.demo.analysis;

import com.mean.androidprivacy.server.demo.util.Md5CalcUtil;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: FlowDroidLauncher
 * @Description: 负责初始化和启动FlowDroid分析
 * @Author: MeanFan
 * @Create: 2020-05-23 15:30
 * @Version: 1.0
 **/

public class FlowDroidLauncher {
    private  FlowDroidConfig flowDroidConfig;

    public FlowDroidLauncher(FlowDroidConfig flowDroidConfig) {
        this.flowDroidConfig =flowDroidConfig;
    }

    /**
    * @Author: MeanFan
    * @Description: 根据apk文件启动FlowDroidRuntime
    * @Param: [uploadApkFile]
    * @return: java.io.File
    **/
    public  File launch(MultipartFile uploadApkFile){
        try {
            // 获取文件的Md5值
            String Md5 = Md5CalcUtil.calcMD5(uploadApkFile.getInputStream());
            File apkFile = new File(String.format("%s/%s.apk", flowDroidConfig.getApkFileDir(), Md5));
            File outputFile = new File(String.format("%s/%s.xml",flowDroidConfig.getOutputFileDir(),Md5));
            File logFile = new File(String.format("%s/%s.log",flowDroidConfig.getLogFileDir(),Md5));
            if(!outputFile.exists()) {    //输出文件不存在才进行后续的分析
                if(!apkFile.exists()){     //apk文件不存在才写入磁盘
                    FileCopyUtils.copy(uploadApkFile.getInputStream(), new FileOutputStream(apkFile));
                }
                FlowDroidRuntime flowDroidRuntime = new FlowDroidRuntime();
                flowDroidRuntime.setEnv(flowDroidConfig.getJavaCmd(),
                                        flowDroidConfig.getFlowDroidCmdJarWorkDir(),
                                        flowDroidConfig.getFlowDroidCmdJarFilePath(),
                                        flowDroidConfig.getAndroidSdkPlatformsDir(),
                                        flowDroidConfig.getSourcesAndSinksFilePath(),
                                        outputFile.getAbsolutePath(),
                                        logFile.getAbsolutePath());
                flowDroidRuntime.exec(apkFile.getAbsolutePath());
            }
            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
