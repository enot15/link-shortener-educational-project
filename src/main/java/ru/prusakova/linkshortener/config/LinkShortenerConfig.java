package ru.prusakova.linkshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.prusakova.linkshortener.property.LinkInfoProperty;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;
import ru.prusakova.linkshortener.repository.impl.LinkInfoRepositoryImpl;
import ru.prusakova.linkshortener.service.LinkInfoService;
import ru.prusakova.linkshortener.service.impl.LinkInfoServiceImpl;
import ru.prusakova.linkshortener.service.impl.LogExecutionTimeLinkInfoServiceProxy;

@Configuration
public class LinkShortenerConfig {

    @Bean
    public LinkInfoRepository linkInfoRepository() {
        return new LinkInfoRepositoryImpl();
    }

    @Bean
    public LinkInfoService linkInfoService(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty) {
        LinkInfoService linkInfoService = new LinkInfoServiceImpl(linkInfoRepository, linkInfoProperty);
        return new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
    }
}
