package com.mean.androidprivacy.server.demo.analysis;

import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;

import java.io.IOException;

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
    public static final String SOURCE_SINK_FILE_PATH_DEFAULT = "config\\SourcesAndSinks.txt";

    public static void proc(String apkFilePath) throws IOException, XmlPullParserException {
        proc(apkFilePath,SOURCE_SINK_FILE_PATH_DEFAULT);
    }

    public static void proc(String apkFilePath, String sourceSinkFilePath) throws IOException, XmlPullParserException {
        InfoflowAndroidConfiguration conf = new InfoflowAndroidConfiguration();
        // androidDirPath是你的android sdk中platforms目录的路径
        conf.getAnalysisFileConfig().setAndroidPlatformDir(ANDROID_PLATFORM_DIR_DEFAULT);
        // apkFilePath是你要分析的apk的文件路径
        conf.getAnalysisFileConfig().setTargetAPKFile(apkFilePath);
        // sourceSinkFilePath是source点与sink点的声明文件，后文会作说明
        conf.getAnalysisFileConfig().setSourceSinkFile(sourceSinkFilePath);
        // apk中的dex文件有对方法数量的限制导致实际app中往往是多dex，不作设置将仅分析classes.dex
        conf.setMergeDexFiles(true);
        // 设置AccessPath长度限制，默认为5，设置负数表示不作限制，AccessPath会在后文解释
        conf.getAccessPathConfiguration().setAccessPathLength(-1);
        // 设置Abstraction的path长度限制，设置负数表示不作限制，Abstraction会在后文解释
        conf.getSolverConfiguration().setMaxAbstractionPathLength(-1);
        SetupApplication setup = new SetupApplication(conf);
        // 设置Callback的声明文件（不显式地设置好像FlowDroid会找不到）
        setup.setCallbackFile("res/AndroidCallbacks.txt");
        setup.runInfoflow();
    }
}
