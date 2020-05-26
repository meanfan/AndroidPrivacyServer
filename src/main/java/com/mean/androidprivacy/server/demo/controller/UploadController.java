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
 * @Description:
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
