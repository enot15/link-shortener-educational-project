package ru.prusakova.linkshortener.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.FilterLinkInfoRequest;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.common.CommonRequest;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@Disabled
//@WebMvcTest(LinkInfoController.class)
class LinkInfoControllerTest {

//    @Autowired
//    MockMvc mvc;

//    @MockBean
//    LinkInfoService linkInfoService;

//    @Test
//    void getLinkInfosTest() throws Exception {
//        Mockito.when(this.linkInfoService.findByFilter()).thenReturn(getLinkInfoResponses());
//
//        mvc.perform(get("/api/v1/link-infos"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    private List<LinkInfoResponse> getLinkInfoResponses() {
//        LinkInfoResponse linkInfoResponse1 = new LinkInfoResponse(UUID.randomUUID(),
//                "https://github.com",
//                "GtCGabA",
//                LocalDateTime.now().plusDays(1),
//                "description1",
//                true,
//                0L);
//        LinkInfoResponse linkInfoResponse2 = new LinkInfoResponse(UUID.randomUUID(),
//                "https://google.com",
//                "GtCGabA",
//                LocalDateTime.now().plusDays(2),
//                "description2",
//                true,
//                0L);
//        return List.of(linkInfoResponse1, linkInfoResponse2);
//    }

    @Test
    void getLinkInfosTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        FilterLinkInfoRequest filterLinkInfoRequest = new FilterLinkInfoRequest();
        filterLinkInfoRequest.setLinkPart("go");

        Assertions.assertNotNull(controller.getLinkInfos(new CommonRequest<>(filterLinkInfoRequest)));

        controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", LocalDateTime.now(), "description", true)));

        Assertions.assertNotNull(controller.getLinkInfos(new CommonRequest<>(filterLinkInfoRequest)));
    }

    @Test
    void postCreateLinkInfoTest() {
        LinkInfoService linkInfoService = mock(LinkInfoService.class);
        LinkInfoController controller = new LinkInfoController(linkInfoService);

        Assertions.assertNotNull(controller.postCreateLinkInfo(new CommonRequest<>(new CreateLinkInfoRequest(
                "https://github.com", LocalDateTime.now(), "description", true))));
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