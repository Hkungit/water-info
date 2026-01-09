package com.seecen.waterinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.seecen.waterinfo.repository")
public class WaterInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterInfoApplication.class, args);
    }
}
