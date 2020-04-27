package com.mean.androidprivacy.server.demo.controller;

import com.mean.androidprivacy.server.demo.analysis.ApkAnalyzer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
public class UploadController {
    private static String UPLOADED_DIR_DEFAULT = "D://APS//upload//";

    @GetMapping("/")
    public String index() {
        return "upload";
    }
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择文件");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            Path path = Paths.get(UPLOADED_DIR_DEFAULT + file.getOriginalFilename());
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(path.toFile()));
            redirectAttributes.addFlashAttribute("message",
                                                 "成功上传文件 '" + file.getOriginalFilename() + "'");
            //TODO 分析文件
            try {
                InfoflowResults results = ApkAnalyzer.proc(path.toString());
                if(results!=null){
                    redirectAttributes.addFlashAttribute("results",results.toString());
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
