package ru.prusakova.linkshortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.common.CommonRequest;
import ru.prusakova.linkshortener.dto.common.CommonResponse;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkInfoControllerTest {

    @Test
    void getLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.getLinkInfo());

        controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", LocalDateTime.now(), "description", true)));

        Assertions.assertNotNull(controller.getLinkInfo());
    }

    @Test
    void postCreateLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", LocalDateTime.now(), "description", true))));
    }

    @Test
    void putLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.putLinkInfo(new CommonRequest<>(new UpdateLinkInfoRequest())));
    }

    @Test
    void deleteLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);
        controller.deleteLinkInfo(UUID.fromString("4325e591-02e2-45b4-8e0c-325539c4cc34"));
    }
}