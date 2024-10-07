package ru.prusakova.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;

import java.time.LocalDateTime;

class LinkInfoServiceImplTest {

    @Test
    void getShortLink() {
        LinkInfoServiceImpl service = new LinkInfoServiceImpl();
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1fdgdgz05QA35345KX_54JOlMMF345345dfgdfpu4sOzZH-KC7g",
                LocalDateTime.now(),
                "Документ",
                true
        );
        System.out.println(service.convertToShortLink(rq));
    }
}