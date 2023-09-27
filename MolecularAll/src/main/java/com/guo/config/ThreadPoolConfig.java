package com.guo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    // 配置线程池，这里使用了一个固定大小的线程池，可以根据需要进行调整
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10); // 10个线程
    }
}
