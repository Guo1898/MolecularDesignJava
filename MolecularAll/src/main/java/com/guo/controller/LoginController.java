package com.guo.controller;


import com.guo.pojo.User;
import com.guo.service.SharedDataService;
import com.guo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class LoginController {

    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> submitValue(@RequestBody User user){
        User Founder = userService.FindByUsername(user.getUsername());
        if (Founder==null){
            System.out.println(user);
            userService.registerUser(user.getUsername(),user.getPassword());
            System.out.println("成功");
            return new ResponseEntity<>("success!", HttpStatus.CREATED); // 返回201状态码表示创建成功
        }
        return new ResponseEntity<>("该用户已经注册", HttpStatus.CONFLICT); // 返回409状态码表示冲突
    }

    @Autowired
    private SharedDataService sharedDataService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws IOException {


        User foundUser = userService.login(user.getUsername(), user.getPassword());
        System.out.println(user);
        if (foundUser != null) {

            //创建文件夹
            Random random = new Random();
            // 生成8位随机数字
            int randomNumber = random.nextInt(99999999);
            String filename = "MD"+randomNumber;
            // 格式化为8位字符串
            System.out.println(filename);
            String newFolder = "/disk195/guojw/MolecularDesign/"+filename; // 新文件夹路径
            Files.createDirectories(Paths.get(newFolder));
            System.out.println("文件夹"+newFolder+"被创建了......");

            userService.UpdateNewFolder(user.getUsername(),newFolder);

            foundUser.setFolderPath(newFolder); // 将文件夹路径存储在用户对象中

            // 创建一个包含用户信息的响应对象
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("user", foundUser);

            // 返回包含用户信息的响应
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed. Invalid username or password.");
        }
    }

}
