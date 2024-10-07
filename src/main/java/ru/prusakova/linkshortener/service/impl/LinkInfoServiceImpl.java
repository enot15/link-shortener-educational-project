package ru.prusakova.linkshortener.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.util.Map;

@Service
public class LinkInfoServiceImpl implements LinkInfoService {

    private final static int COUNT = 7;

    public String convertToShortLink(CreateLinkInfoRequest request) {
        String shortLink = "http://test.com/" + RandomStringUtils.randomAlphabetic(COUNT);
        Map<String, CreateLinkInfoRequest> map = Map.of(shortLink, request);
        return shortLink;
    }
}
