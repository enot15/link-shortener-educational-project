package ru.prusakova.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.prusakova.linkshortener.dto.*;
import ru.prusakova.linkshortener.exception.NotFoundShortLinkException;
import ru.prusakova.linkshortener.mapper.LinkInfoMapper;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.property.LinkInfoProperty;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkInfoServiceImplTest {

    private final LinkInfoRepository linkInfoRepositoryMock = mock(LinkInfoRepository.class);
    private final LinkInfoMapper linkInfoMapperMock = mock(LinkInfoMapper.class);
    private final LinkInfoProperty linkInfoPropertyMock = mock(LinkInfoProperty.class);

    @InjectMocks
    private LinkInfoServiceImpl linkInfoServiceMock;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Test
    void when_createLinkInfo_expect_success() {
        CreateLinkInfoRequest rq = new CreateLinkInfoRequest(
                "https://docs.google.com/document/d/1",
                LocalDateTime.now(),
                "description",
                true
        );
        LinkInfo linkInfoMock = mock(LinkInfo.class);

        when(linkInfoMapperMock.fromCreateRequest(eq(rq), anyString())).thenReturn(linkInfoMock);
        when(linkInfoPropertyMock.getShortLinkLength()).thenReturn(4);
        when(linkInfoRepositoryMock.save(linkInfoMock)).thenReturn(linkInfoMock);

        linkInfoServiceMock.createLinkInfo(rq);

        verify(linkInfoMapperMock).fromCreateRequest(eq(rq), stringCaptor.capture());
        String shortLink = stringCaptor.getValue();
        assertEquals(4, shortLink.length());

        verify(linkInfoRepositoryMock).save(linkInfoMock);
        verify(linkInfoMapperMock).toResponse(linkInfoMock);
    }

    @Test
    void when_getByShortLink_expect_success() {
        String shortLink = "shortLink";
        LocalDateTime now = LocalDateTime.now();
        LinkInfoResponse linkInfoResponse = new LinkInfoResponse();
        linkInfoResponse.setShortLink(shortLink);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(UUID.randomUUID());
        linkInfo.setShortLink(shortLink);

        when(linkInfoRepositoryMock.findActiveShortLink(eq(shortLink), any(LocalDateTime.class))).thenReturn(Optional.of(linkInfo));
        when(linkInfoMapperMock.toResponse(linkInfo)).thenReturn(linkInfoResponse);

        LinkInfoResponse actualResponse = linkInfoServiceMock.getByShortLink(shortLink);

        assertNotNull(actualResponse);
        assertEquals(linkInfoResponse, actualResponse);
        verify(linkInfoRepositoryMock, times(1)).incrementOpeningCountByShortLink(shortLink);

    }


    @Test
    void when_getByShortLink_expect_ThrowsNotFoundShortLinkException() {
        String shortLink = "shortLink";
        LocalDateTime now = LocalDateTime.now();
        LinkInfoResponse linkInfoResponse = new LinkInfoResponse();
        linkInfoResponse.setShortLink(shortLink);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(UUID.randomUUID());
        linkInfo.setShortLink(shortLink);

        when(linkInfoRepositoryMock.findActiveShortLink(shortLink, now)).thenReturn(Optional.empty());

        NotFoundShortLinkException exception = assertThrows(NotFoundShortLinkException.class,
                () -> linkInfoServiceMock.getByShortLink(shortLink));

        assertEquals("Не найдена сущность по короткой ссылке " + shortLink, exception.getMessage());
        verify(linkInfoRepositoryMock, never()).incrementOpeningCountByShortLink(shortLink);

    }

    @Test
    void when_findByFilter_expect_success() {
        PageableRequest pageableRequest = new PageableRequest(2,10, List.of());
        FilterLinkInfoRequest filterRequest = new FilterLinkInfoRequest();
        filterRequest.setPage(pageableRequest);
        filterRequest.setLinkPart("test");
        filterRequest.setActive(true);
        filterRequest.setDescriptionPart("descr");
        filterRequest.setEndTimeFrom(LocalDateTime.now().minusDays(1));
        filterRequest.setEndTimeTo(LocalDateTime.now());

        List<LinkInfo> linkInfos = Arrays.asList(
                new LinkInfo(),
                new LinkInfo()
        );

        List<LinkInfoResponse> linkInfoResponses = linkInfos.stream()
                .map(this::mapToResponse)
                .toList();

        Pageable pageable = mapPageable(pageableRequest);
        Page<LinkInfo> linkInfoPage = new PageImpl<>(linkInfos, pageable, linkInfos.size());
        when(linkInfoRepositoryMock.findByFilter(
                filterRequest.getLinkPart(),
                filterRequest.getEndTimeFrom(),
                filterRequest.getEndTimeTo(),
                filterRequest.getDescriptionPart(),
                filterRequest.getActive(),
                pageable
        )).thenReturn(linkInfoPage);

        for(int i = 0; i < linkInfos.size(); i++){
            when(linkInfoMapperMock.toResponse(linkInfos.get(i))).thenReturn(linkInfoResponses.get(i));
        }

        List<LinkInfoResponse> actualResponses = linkInfoServiceMock.findByFilter(filterRequest);

        assertNotNull(actualResponses);
        assertEquals(linkInfoResponses.size(), actualResponses.size());
        assertEquals(linkInfoResponses.get(0).getShortLink(), actualResponses.get(0).getShortLink());
        assertEquals(linkInfoResponses.get(1).getShortLink(), actualResponses.get(1).getShortLink());

        verify(linkInfoRepositoryMock).findByFilter(
                filterRequest.getLinkPart(),
                filterRequest.getEndTimeFrom(),
                filterRequest.getEndTimeTo(),
                filterRequest.getDescriptionPart(),
                filterRequest.getActive(),
                pageable
        );
    }

    private LinkInfoResponse mapToResponse(LinkInfo linkInfo){
        LinkInfoResponse response = new LinkInfoResponse();
        response.setShortLink("test");
        return response;
    }

    private Pageable mapPageable(PageableRequest page) {
        return PageRequest.of(page.getNumber(), page.getSize());
    }

    @Test
    void when_delete_expect_success(){
        UUID id = UUID.randomUUID();

        linkInfoServiceMock.delete(id);

        verify(linkInfoRepositoryMock).deleteById(id);
    }

    @Test
    void when_updateLinkInfo_expect_success () {
        UUID uuid = UUID.randomUUID();
        UpdateLinkInfoRequest updateRequest = new UpdateLinkInfoRequest();
        updateRequest.setId(uuid.toString());
        updateRequest.setLink("newLink");
        updateRequest.setEndTime(LocalDateTime.now().plusDays(1));
        updateRequest.setDescription("newDescription");
        updateRequest.setActive(true);

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setId(uuid);
        linkInfo.setLink("oldLink");
        linkInfo.setEndTime(LocalDateTime.now());
        linkInfo.setDescription("oldDescription");
        linkInfo.setActive(true);
        linkInfo.setShortLink("short");

        LinkInfoResponse linkInfoResponse = new LinkInfoResponse();
        linkInfoResponse.setShortLink("short");

        when(linkInfoRepositoryMock.findById(uuid)).thenReturn(Optional.of(linkInfo));
        when(linkInfoMapperMock.toResponse(linkInfo)).thenReturn(linkInfoResponse);

        LinkInfoResponse actualResponse = linkInfoServiceMock.updateLinkInfo(updateRequest);

        assertNotNull(actualResponse);
        assertEquals(linkInfoResponse, actualResponse);
        assertEquals(updateRequest.getLink(), linkInfo.getLink());
        assertEquals(updateRequest.getDescription(), linkInfo.getDescription());
        assertEquals(updateRequest.getEndTime(), linkInfo.getEndTime());
        assertEquals(updateRequest.getActive(), linkInfo.getActive());
        verify(linkInfoRepositoryMock).save(linkInfo);
    }
}