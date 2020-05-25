package com.mean.androidprivacy.server.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: AndroidPrivacyServer
 * @ClassName: LocationLeakTestController
 * @Description:
 * @Author: MeanFan
 * @Create: 2020-05-14 18:51
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/leak/location")
public class LocationLeakTestController {
    @GetMapping
    public String index(Double longitude,Double latitude){
        System.out.println(String.format("LocationLeakTest: longitude %f,latitude %f",longitude,latitude));
        return "success";
    }
}
