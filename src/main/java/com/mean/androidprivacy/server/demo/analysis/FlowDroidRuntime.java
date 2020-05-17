package com.mean.androidprivacy.server.demo.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: FlowDroidRuntime
 * @Description:
 * @Author: MeanFan
 * @Create: 2020-05-17 19:24
 * @Version: 1.0
 **/
public class FlowDroidRuntime {
    private String javaCmd = "java";
    private String flowDroidCmdJarWorkDir = "D:\\APS\\bin";
    private String flowDroidCmdJarFilePath = "D:\\APS\\bin\\soot-infoflow-cmd-jar-with-dependencies.jar";
    private String apkFilePath = "";
    private String androidSdkPlatformsDir = "D:\\Android\\sdk\\platforms";
    private String sourcesAndSinksFilePath = "D:\\APS\\bin\\SourcesAndSinks.txt";
    private String outputFilePath = "D:\\APS\\output\\o.txt";
    private String logFilePath = "D:\\APS\\log\\latest.txt";

    public void setEnv(String javaCmd, String flowDroidCmdJarWorkDir,
                         String flowDroidCmdJarFilePath,String androidSdkPlatformsDir,
                         String sourcesAndSinksFilePath,String outputFilePath,String logFilePath){
        this.javaCmd = javaCmd;
        this.flowDroidCmdJarWorkDir = flowDroidCmdJarWorkDir;
        this.flowDroidCmdJarFilePath = flowDroidCmdJarFilePath;
        this.androidSdkPlatformsDir = androidSdkPlatformsDir;
        this.sourcesAndSinksFilePath = sourcesAndSinksFilePath;
        this.outputFilePath = outputFilePath;
        this.logFilePath = logFilePath;
    }

    public boolean exec(String apkFilePath) {
        this.apkFilePath = apkFilePath;
        String cmdStr = String.format("%s -jar %s -a \"%s\" -p \"%s\" -s \"%s\" -o \"%s\"",
                                      javaCmd, flowDroidCmdJarFilePath, this.apkFilePath,
                                      androidSdkPlatformsDir, sourcesAndSinksFilePath,outputFilePath);
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmdStr);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String log;
            while ((log = br.readLine()) != null) {
                sb.append(log);
            }
            //TODO 写sb到log文件

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
