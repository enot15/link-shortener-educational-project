package ru.prusakova.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.prusakova.logingstarter.LoggingStarterAutoConfiguration;

@SpringBootApplication
public class LinkShortenerApp {
    public static void main(String[] args) {
        LoggingStarterAutoConfiguration.println("test");
        SpringApplication.run(LinkShortenerApp.class, args);
    }
}