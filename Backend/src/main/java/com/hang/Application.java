package com.hang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @ClassName Application
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/15 19:36
 * @Version 1.0
 */
@SpringBootApplication
@CrossOrigin
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
