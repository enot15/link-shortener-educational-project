package ru.prusakova.linkshortener.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import ru.prusakova.linkshortener.AbstractTest;
import ru.prusakova.linkshortener.model.LinkInfo;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShortLinkControllerTest extends AbstractTest {

    @Test
    @Transactional
    void when_shortLink_expect_success() throws Exception {
        String shortLink = "dHteSKM";
        String link = "https://github.com";

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLink(link);
        linkInfo.setShortLink(shortLink);
        linkInfo.setActive(true);

        linkInfoRepository.save(linkInfo);

        mockMvc.perform(get("/api/v1/short-link/" + shortLink))
                .andExpect(status().isTemporaryRedirect())
                .andExpect(header().string(HttpHeaders.LOCATION, link));

        Mockito.verify(linkInfoRepository).findActiveShortLink(eq(shortLink), any(LocalDateTime.class));
    }
}