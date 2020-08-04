package com.photoapp.albums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AlbumsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlbumsServiceApplication.class, args);
	}

}
