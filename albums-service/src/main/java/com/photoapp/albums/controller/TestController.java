package com.photoapp.albums.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums")
public class TestController {

	@Autowired
	Environment env;

	@GetMapping("/status/check")
	public String status() {
		return "User Service is Working on Port : " + env.getProperty("local.server.port");
	}
}
//http://localhost:8011/albums-service/albums/status/check