package ru.prusakova.linkshortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
class ShortLinkControllerTest {

    @Test
    void getByShortLinkTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        ShortLinkController shortLinkController = new ShortLinkController(linkInfoService);

        LinkInfoResponse linkInfoResponse = new LinkInfoResponse(UUID.fromString("4325e591-02e2-45b4-8e0c-325539c4cc34"),
                "https://github.com", "dHteSKM", LocalDateTime.now(), "test", true, 0L);

        when(linkInfoService.getByShortLink(any())).thenReturn(linkInfoResponse);

        Assertions.assertNotNull(shortLinkController.getByShortLink("dHteSKM"));
    }
}