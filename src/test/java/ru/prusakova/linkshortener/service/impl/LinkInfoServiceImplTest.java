package ru.prusakova.linkshortener.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;
import ru.prusakova.linkshortener.repository.impl.LinkInfoRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

class LinkInfoServiceImplTest {

    @Test
    void createLinkInfoTest() {
        LinkInfoRepository linkInfoRepository = new LinkInfoRepositoryImpl();
        LinkInfoServiceImpl service = new LinkInfoServiceImpl(linkInfoRepository);
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "description",
                true
        );
        LinkInfoResponse linkInfoResponse = service.createLinkInfo(rq);

        Assertions.assertNotNull(linkInfoResponse.getId());
    }

    @Test
    void getByShortLinkTest() {
        LinkInfoRepository linkInfoRepository = new LinkInfoRepositoryImpl();
        LinkInfoServiceImpl service = new LinkInfoServiceImpl(linkInfoRepository);
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "doc1",
                true
        );
        LinkInfoResponse linkInfoResponse = service.createLinkInfo(rq);

        Assertions.assertEquals(linkInfoResponse, service.getByShortLink(linkInfoResponse.getShortLink()));
    }

    @Test
    void findByFilterTest() {
        LinkInfoRepository linkInfoRepository = new LinkInfoRepositoryImpl();
        LinkInfoServiceImpl service = new LinkInfoServiceImpl(linkInfoRepository);
        CreateLinkInfoRequest rq1 = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "doc1",
                true
        );
        CreateLinkInfoRequest rq2 = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/2",
                LocalDateTime.now(),
                "doc2",
                true
        );
        LinkInfoResponse linkInfoResponse1 = service.createLinkInfo(rq1);
        LinkInfoResponse linkInfoResponse2 = service.createLinkInfo(rq2);

        Assertions.assertEquals(List.of(linkInfoResponse1, linkInfoResponse2), service.findByFilter());
    }
}