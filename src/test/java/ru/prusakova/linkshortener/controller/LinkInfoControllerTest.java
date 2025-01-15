package ru.prusakova.linkshortener.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import ru.prusakova.linkshortener.AbstractTest;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.FilterLinkInfoRequest;
import ru.prusakova.linkshortener.dto.PageableRequest;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.common.CommonRequest;
import ru.prusakova.linkshortener.model.LinkInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.UUID.fromString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LinkInfoControllerTest extends AbstractTest {

    public static final String LINK = "https://github.com";
    public static final LocalDateTime END_TIME = LocalDateTime.now().plusDays(1);
    public static final String DESCRIPTION = "github";
    public static final String ID = "6dd756b6-2a35-4d15-bbf2-701521136b46";

    @Test
    @Transactional
    void when_post_expect_success() throws Exception {
        CreateLinkInfoRequest createLinkInfoRequest = new CreateLinkInfoRequest();
        createLinkInfoRequest.setLink(LINK);
        createLinkInfoRequest.setEndTime(END_TIME);
        createLinkInfoRequest.setDescription(DESCRIPTION);
        createLinkInfoRequest.setActive(true);

        CommonRequest<CreateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(createLinkInfoRequest);

        mockMvc.perform(post("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body[0].id").isNotEmpty());
    }

    @Test
    @Transactional
    void when_patch_expect_success() throws Exception {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLink(LINK);
        linkInfo.setShortLink("dHteSKM");
        linkInfo.setEndTime(END_TIME);
        linkInfo.setDescription(DESCRIPTION);
        linkInfo.setActive(true);
        LinkInfo save = linkInfoRepository.save(linkInfo);

        UpdateLinkInfoRequest updateLinkInfoRequest = new UpdateLinkInfoRequest();
        updateLinkInfoRequest.setId(String.valueOf(save.getId()));
        updateLinkInfoRequest.setLink(LINK);

        CommonRequest<UpdateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(updateLinkInfoRequest);

        mockMvc.perform(patch("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").isNotEmpty());
    }

    @Test
    @Transactional
    void when_delete_expect_success() throws Exception {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(fromString(ID));
        linkInfo.setLink(LINK);
        linkInfo.setShortLink("dHteSKM");
        linkInfo.setEndTime(END_TIME);
        linkInfo.setDescription(DESCRIPTION);
        linkInfo.setActive(true);
        linkInfoRepository.save(linkInfo);

        mockMvc.perform(delete("/api/v1/link-infos/" + ID)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void when_filter_expect_success() throws Exception {
        FilterLinkInfoRequest filterRequest = new FilterLinkInfoRequest();
        filterRequest.setLinkPart("dHt");
        filterRequest.setPage(new PageableRequest(1, 5, List.of()));

        CommonRequest<FilterLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(filterRequest);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLink("dHteSKM");
        linkInfoRepository.save(linkInfo);

        mockMvc.perform(post("/api/v1/link-infos/filter")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", hasSize(1)))
                .andExpect(jsonPath("$.body[0].id").isNotEmpty())
                .andExpect(jsonPath("$.body[0].link").value("dHteSKM"));
    }


    @ParameterizedTest
    @MethodSource("postCreateShortLinkInvalidRequestSource")
    void when_postCreateShortLink_withInvalidRequest_expect_validationError(CreateLinkInfoRequest createRequest, String validationErrorMessage) throws Exception {
        CommonRequest<CreateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')]").exists());
    }

    public static Stream<Arguments> postCreateShortLinkInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new CreateLinkInfoRequest(null, END_TIME, DESCRIPTION, true), "Ссылка не может быть пустой"),
                Arguments.of(new CreateLinkInfoRequest("", END_TIME, DESCRIPTION, true), "Ссылка не может быть пустой"),
                Arguments.of(new CreateLinkInfoRequest("error_url_pattern", END_TIME, DESCRIPTION, true), "url не соответствует паттерну"),
                Arguments.of(new CreateLinkInfoRequest(LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true), "Дата окончания действия короткой ссылки не может быть прошедшей"),
                Arguments.of(new CreateLinkInfoRequest(LINK, END_TIME, "", true), "Описание не может быть пустым"),
                Arguments.of(new CreateLinkInfoRequest(LINK, END_TIME, null, true), "Описание не может быть пустым"),
                Arguments.of(new CreateLinkInfoRequest(LINK, END_TIME, DESCRIPTION, null), "признак актиновсти не может отсутствовать или быть null")
        );
    }

    @ParameterizedTest
    @MethodSource("patchUpdateShortLinkInvalidRequestSource")
    void when_patchUpdateShortLink_withInvalidRequest_expect_validationError(UpdateLinkInfoRequest updateRequest, String validationErrorMessage) throws Exception {
        CommonRequest<UpdateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(updateRequest);

        mockMvc.perform(patch("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')]").exists());
    }

    public static Stream<Arguments> patchUpdateShortLinkInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new UpdateLinkInfoRequest("", LINK, END_TIME, DESCRIPTION, true), "Идентификатор не может отсутствовать или быть null"),
                Arguments.of(new UpdateLinkInfoRequest(null, LINK, END_TIME, DESCRIPTION, true), "Идентификатор не может отсутствовать или быть null"),
                Arguments.of(new UpdateLinkInfoRequest("error_UUID_pattern", LINK, END_TIME, DESCRIPTION, true), "Идентификатор должен соответствовать паттерну UUID"),
                Arguments.of(new UpdateLinkInfoRequest(ID, "error_url_patter", END_TIME, DESCRIPTION, true), "url не соответствует паттерну"),
                Arguments.of(new UpdateLinkInfoRequest(ID, LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true), "Дата окончания действия короткой ссылки не может быть прошедшей")
        );
    }
}