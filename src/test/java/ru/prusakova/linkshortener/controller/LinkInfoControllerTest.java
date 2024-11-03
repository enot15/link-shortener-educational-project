package ru.prusakova.linkshortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.common.CommonRequest;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@SpringBootTest
class LinkInfoControllerTest {

    @Test
    void getLinkInfosTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.getLinkInfos());

        controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", String.valueOf(LocalDateTime.now()), "description", true)));

        Assertions.assertNotNull(controller.getLinkInfos());
    }

    @Test
    void postCreateLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", String.valueOf(LocalDateTime.now()), "description", true))));
    }

    @Test
    void patchLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.patchLinkInfo(new CommonRequest<>(new UpdateLinkInfoRequest())));
    }

    @Test
    void deleteLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);
        controller.deleteLinkInfo(UUID.fromString("4325e591-02e2-45b4-8e0c-325539c4cc34"));
    }
}