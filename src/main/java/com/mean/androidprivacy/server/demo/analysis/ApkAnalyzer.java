package com.mean.androidprivacy.server.demo.analysis;

import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: ApkAnalyzer
 * @Description:
 * @Author: MeanFan
 * @Create: 2020-04-04 21:25
 * @Version: 1.0
 **/
public class ApkAnalyzer {
    public static final String ANDROID_PLATFORM_DIR_DEFAULT = "D:\\Android\\sdk\\platforms";
    public static final String SOURCE_SINK_FILE_PATH_DEFAULT = "D:\\APS\\SourcesAndSinks.txt";
    public static final String ANDROID_CALLBACKS_FILE_PATH_DEFAULT = "D:\\APS\\AndroidCallbacks.txt";



    public static InfoflowResults proc(String apkFilePath) throws IOException, XmlPullParserException {
        return proc(apkFilePath,SOURCE_SINK_FILE_PATH_DEFAULT);
    }

    public static InfoflowResults proc(String apkFilePath, String sourceSinkFilePath) throws IOException, XmlPullParserException {
        InfoflowAndroidConfiguration conf = new InfoflowAndroidConfiguration();
        // Android sdk中platforms目录的路径
        conf.getAnalysisFileConfig().setAndroidPlatformDir(ANDROID_PLATFORM_DIR_DEFAULT);
        // 分析的apk的文件路径
        conf.getAnalysisFileConfig().setTargetAPKFile(apkFilePath);
        // 是source点与sink点的声明文件
        conf.getAnalysisFileConfig().setSourceSinkFile(sourceSinkFilePath);
        // 设置仅分析classes.dex
        conf.setMergeDexFiles(true);
        // 设置AccessPath长度限制，默认为5，设置负数表示不作限制
        conf.getAccessPathConfiguration().setAccessPathLength(-1);
        // 设置Abstraction的path长度限制，设置负数表示不作限制
        conf.getSolverConfiguration().setMaxAbstractionPathLength(-1);
        SetupApplication setup = new SetupApplication(conf);
        // 设置Callback的声明文件（不显式地设置FlowDroid会找不到）
        setup.setCallbackFile(ANDROID_CALLBACKS_FILE_PATH_DEFAULT);
        InfoflowResults results = setup.runInfoflow();
        //results.printResults();
        return results;
    }
}
