package com.mean.androidprivacy.server.demo.controller;

import com.mean.androidprivacy.server.demo.analysis.FlowDroidConfig;
import com.mean.androidprivacy.server.demo.analysis.FlowDroidLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: UploadController
 * @Description: SpringBoot的 控制器，负责HTTP请求的接收和响应
 * @Author: MeanFan
 * @Create: 2020-04-04 21:16
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/")
@EnableConfigurationProperties(FlowDroidConfig.class)
public class UploadController {
    @Autowired
    private FlowDroidConfig flowDroidConfig;

    @GetMapping("/")
    public String index() {
        return null;
    }


    /**
    * @Author: MeanFan
    * @Description: 根据MD5返回已有的分析结果
    * @Param: [Md5]
    * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.FileSystemResource>
    **/
    @GetMapping("/result")
    public ResponseEntity<FileSystemResource> singleFileUpload(@RequestParam("apkMd5") String Md5) {
        File outputFile = new File(String.format("%s/%s.xml", flowDroidConfig.getOutputFileDir(), Md5));
        if (outputFile.exists()) {
            System.out.println("/result: "+outputFile.getAbsolutePath());
            return getFileSystemResourceResponseEntity(outputFile.toPath());

        } else {
            System.out.println("/result: [notfound]");
            return ResponseEntity.notFound().build();
        }
    }

    /**
    * @Author: MeanFan
    * @Description: 根据路径获得文件报文体
    * @Param: [outputPath]
    * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.FileSystemResource>
    **/

    private ResponseEntity<FileSystemResource> getFileSystemResourceResponseEntity(Path outputPath) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + outputPath.getFileName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(outputPath.toFile().length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(outputPath));
    }

    /**
    * @Author: MeanFan
    * @Description: 根据上传的apk文件返回包含分析结果的响应报文
    * @Param: [uploadApkFile]
    * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.FileSystemResource>
    **/

    @PostMapping("/upload")
    public ResponseEntity<FileSystemResource> singleFileUpload(@RequestParam("uploadApkFile") MultipartFile uploadApkFile) {
        if (uploadApkFile.isEmpty()) {

            System.out.println("/upload: [null]");
            return null;
        }
        System.out.println("/upload: [start analyze apk]");
        File outputFile = new FlowDroidLauncher(flowDroidConfig).launch(uploadApkFile);
        System.out.println("/upload: [finish analyze apk]");
        if(outputFile!=null && outputFile.exists()) {
            System.out.println("/upload: "+outputFile.getAbsolutePath());
            return getFileSystemResourceResponseEntity(outputFile.toPath());
        }else {
            System.out.println("/upload: [null]");
            return null;
        }
    }
}
