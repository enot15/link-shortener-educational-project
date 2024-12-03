package ru.prusakova.linkshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

@Configuration
@EnableAsync
@EnableScheduling
public class LinkShortenerConfig {

    @Bean
    public String notFoundPage() throws IOException {
        try(InputStream is = new ClassPathResource("templates/404.html").getInputStream()) {
            byte[] binaryData = FileCopyUtils.copyToByteArray(is);
            return new String(binaryData, StandardCharsets.UTF_8);
        }
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService singleExecutor() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardPolicy());
    }
}
