package com.guo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class controller {

    @PostMapping("/test")
    public void handlePostRequest(@RequestBody String data){
        System.out.println(data);
    }

    @GetMapping("/hello")
    public String hello1(){
        return "hello";
    }


}
