package com.guo.uilts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.guo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReadData {

    @Autowired
    private UserService userService;



    public Map<String, List<String>> readBreed(String username) {

        try (BufferedReader reader = new BufferedReader(new FileReader(userService.QueryNewfolder(username) + "/" + username + ".Breed"))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // 如果你需要保留换行符，可以加上这一行
            }

            String fileContent = stringBuilder.toString();
            System.out.println("读取到的文件内容:");
            System.out.println(fileContent);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = objectMapper.readValue(fileContent, HashMap.class);

            // 创建一个新的Map，用于存储转换后的结果
            Map<String, List<String>> convertedMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                if (entry.getValue() instanceof List) {
                    // 强制转换值为List<String>
                    List<String> stringList = (List<String>) entry.getValue();
                    // 将转换后的List添加到新的Map中
                    convertedMap.put(entry.getKey(), stringList);
                }
            }

            System.out.println("转换后的HashMap:");
            for (Map.Entry<String, List<String>> entry : convertedMap.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }
            System.out.println(convertedMap);

            // 返回新的Map
            return convertedMap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, List<String>> readTrait(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userService.QueryNewfolder(username) + "/" + username))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // 如果你需要保留换行符，可以加上这一行
            }

            String fileContent = stringBuilder.toString();
            System.out.println("读取到的文件内容:");
            System.out.println(fileContent);



            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = objectMapper.readValue(fileContent, HashMap.class);

            // 创建一个新的Map，用于存储转换后的结果
            Map<String, List<String>> convertedMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                if (entry.getValue() instanceof List) {
                    // 强制转换值为List<String>
                    List<String> stringList = (List<String>) entry.getValue();
                    // 将转换后的List添加到新的Map中
                    convertedMap.put(entry.getKey(), stringList);
                }
            }

            System.out.println("转换后的HashMap:");
            for (Map.Entry<String, List<String>> entry : convertedMap.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }
            System.out.println(convertedMap);

            // 返回新的Map
            return convertedMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
