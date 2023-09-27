package com.guo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guo.mapper.DataMapper;
import com.guo.pojo.AdiposeTissues;
import com.guo.pojo.DataShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DataShareController {

    @Autowired
    private DataMapper dataMapper;

    @GetMapping("/Datas")
    public List<DataShare> getDataFromTableSnp() {
        return dataMapper.queryData();
    }


    @GetMapping("/Datas81")
    public List<DataShare> getDataFrom81() {
        return dataMapper.queryData81();
    }

    @GetMapping("/Gene")
    public List<AdiposeTissues> getDataGene() {
        return dataMapper.queryGene();
    }



    @PostMapping("/DatasId")
    public List<DataShare> getDataById(@RequestBody String data) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jsonMap = mapper.readValue(data, new TypeReference<Map<String, String>>(){});
            String inputText = jsonMap.get("inputText");
            System.out.println(inputText);

            // 使用 inputText 进行 SQL 查询
            // ...

            return dataMapper.queryId(inputText);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }



    @PostMapping("/GeneId")
    public List<AdiposeTissues> getGeneById(@RequestBody String data) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jsonMap = mapper.readValue(data, new TypeReference<Map<String, String>>(){});
            String inputText = jsonMap.get("inputText1");
            System.out.println(inputText);

            // 使用 inputText 进行 SQL 查询
            // ...

            return dataMapper.queryGeneId(inputText);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }




}
