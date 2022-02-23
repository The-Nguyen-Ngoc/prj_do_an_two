package com.example.prj_do_an_two;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PrjDoAnTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrjDoAnTwoApplication.class, args);
    }

}
