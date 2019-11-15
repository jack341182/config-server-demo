package com.example.configSeverDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigSeverDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigSeverDemoApplication.class, args);
    }

}
