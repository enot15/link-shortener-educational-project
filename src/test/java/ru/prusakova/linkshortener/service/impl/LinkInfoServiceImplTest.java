package ru.prusakova.linkshortener.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.FilterLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.exception.NotFoundException;
import ru.prusakova.linkshortener.exception.NotFoundShortLinkException;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;

@SpringBootTest
class LinkInfoServiceImplTest {

    @Autowired
    private LinkInfoService linkInfoService;

    @Test
    void createLinkInfoTest() {
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "description",
                true
        );
        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(rq);

        Assertions.assertNotNull(linkInfoResponse.getId());
    }

    @Test
    void getByShortLinkTest() {
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now().plusDays(1),
                "doc1",
                true
        );
        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(rq);

        Assertions.assertEquals(linkInfoResponse.getShortLink(), linkInfoService.getByShortLink(linkInfoResponse.getShortLink()).getShortLink());
    }

    @Test
    void findByFilterTest() {
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
        linkInfoService.createLinkInfo(rq1);
        linkInfoService.createLinkInfo(rq2);

        FilterLinkInfoRequest filterLinkInfoRequest = new FilterLinkInfoRequest();
        filterLinkInfoRequest.setLinkPart("go");

        Assertions.assertNotNull(linkInfoService.findByFilter(filterLinkInfoRequest));
    }

    @Test
    void deleteTest() {
        CreateLinkInfoRequest rq1 = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now().plusDays(1),
                "doc1",
                true
        );
        CreateLinkInfoRequest rq2 = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/2",
                LocalDateTime.now().plusDays(1),
                "doc2",
                true
        );
        LinkInfoResponse linkInfoResponse1 = linkInfoService.createLinkInfo(rq1);
        LinkInfoResponse linkInfoResponse2 = linkInfoService.createLinkInfo(rq2);

        linkInfoService.delete(linkInfoResponse1.getId());
        Assertions.assertNotNull(linkInfoService.getByShortLink(linkInfoResponse2.getShortLink()));
        Assertions.assertThrows(NotFoundShortLinkException.class,
                () -> linkInfoService.getByShortLink(linkInfoResponse1.getShortLink()));
    }

    @Test
    void updateLinkInfoTest() {
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "doc1",
                true
        );
        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(rq);
        UpdateLinkInfoRequest updateLinkInfoRequest1 = new UpdateLinkInfoRequest();
        updateLinkInfoRequest1.setId(String.valueOf(linkInfoResponse.getId()));
        updateLinkInfoRequest1.setDescription("doc11");

        LinkInfoResponse linkInfoResponseNew1 = LinkInfoResponse.builder()
                .id(linkInfoResponse.getId())
                .link(linkInfoResponse.getLink())
                .shortLink(linkInfoResponse.getShortLink())
                .description(updateLinkInfoRequest1.getDescription())
                .active(linkInfoResponse.getActive())
                .openingCount(linkInfoResponse.getOpeningCount())
                .build();
        Assertions.assertEquals(linkInfoResponseNew1, linkInfoService.updateLinkInfo(updateLinkInfoRequest1));
    }
}