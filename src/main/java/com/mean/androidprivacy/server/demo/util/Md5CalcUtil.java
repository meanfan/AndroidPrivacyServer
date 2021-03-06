package com.mean.androidprivacy.server.demo.util;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: Md5CalcUtil
 * @Description: MD5计算工具
 * @Author: MeanFan
 * @Create: 2020-05-17 21:51
 * @Version: 1.0
 **/

public class Md5CalcUtil {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
    * @Author: MeanFan
    * @Description: 根据输入流获取MD5值
    * @Param: [stream]
    * @return: java.lang.String
    **/
    public static String calcMD5(InputStream stream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
    * @Author: MeanFan
    * @Description: 字节数组转String类
    * @Param: [data]
    * @return: java.lang.String
    **/
    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
