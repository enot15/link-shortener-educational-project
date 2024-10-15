package ru.prusakova.linkshortener.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.exception.NotFoundException;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.prusakova.linkshortener.util.Constants.LENGTH_SHORT_LINK;

@Service
public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoRepository linkInfoRepository;

    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository) {
        this.linkInfoRepository = linkInfoRepository;
    }

    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request) {
        String shortLink = RandomStringUtils.randomAlphabetic(LENGTH_SHORT_LINK);
        LinkInfo linkInfo = LinkInfo.builder()
                .link(request.getLink())
                .shortLink(shortLink)
                .description(request.getDescription())
                .endTime(request.getEndTime())
                .active(request.getActive())
                .openingCount(0L)
                .build();
        LinkInfo saveLinkInfo = linkInfoRepository.save(linkInfo);
        return toResponse(saveLinkInfo);
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по короткой ссылке " + shortLink));
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private LinkInfoResponse toResponse(LinkInfo linkInfo) {
        return LinkInfoResponse.builder()
                .id(linkInfo.getId())
                .link(linkInfo.getLink())
                .shortLink(linkInfo.getShortLink())
                .endTime(linkInfo.getEndTime())
                .description(linkInfo.getDescription())
                .active(linkInfo.getActive())
                .openingCount(linkInfo.getOpeningCount())
                .build();
    }
}
