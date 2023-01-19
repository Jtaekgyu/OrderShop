package com.example.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능이다.
@SpringBootApplication
public class MyShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyShopApplication.class, args);
	}

}
