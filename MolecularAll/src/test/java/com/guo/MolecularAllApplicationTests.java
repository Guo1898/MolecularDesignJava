package com.guo;


import com.guo.uilts.ReadData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;






@SpringBootTest
class MolecularAllApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(1);
    }

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private ReadData readData;

    @Test
    void test() throws IOException {

        readData.readBreed("aaa");

    }




}
