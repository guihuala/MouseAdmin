package com.example.adminapi;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.adminapi.mapper")
public class AdminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApiApplication.class, args);
	}

}
