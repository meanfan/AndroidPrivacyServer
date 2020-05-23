package com.mean.androidprivacy.server.demo.controller;

import com.mean.androidprivacy.server.demo.analysis.FlowDroidConfig;
import com.mean.androidprivacy.server.demo.analysis.FlowDroidLauncher;
import com.mean.androidprivacy.server.demo.analysis.FlowDroidRuntime;
import com.mean.androidprivacy.server.demo.util.Md5CalcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soot.jimple.infoflow.results.DataFlowResult;

import java.io.FileOutputStream;
import java.io.IOException;
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
        Path outputPath = Paths.get(String.format("%s/%s.xml", flowDroidConfig.getOutputFileDir(), Md5));
        if (outputPath.toFile().exists()) {
            return getFileSystemResourceResponseEntity(outputPath);
        } else {
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
            return null;
        }
        Path outputPath = FlowDroidLauncher.launch(uploadApkFile);
        if(outputPath!=null) {
            return getFileSystemResourceResponseEntity(outputPath);
        }else {
            return null;
        }
    }
}
