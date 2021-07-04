package com.example.forestfires;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.forestfires.dao.mapper")
public class ForestfiresApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestfiresApplication.class, args);
    }

}
