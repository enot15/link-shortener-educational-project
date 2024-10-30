package ru.prusakova.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.annotation.LogExecutionTime;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.exception.NotFoundException;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.property.LinkInfoProperty;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoRepository linkInfoRepository;
    private final LinkInfoProperty linkInfoProperty;

    @Override
    @LogExecutionTime
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request) {
        String shortLink = RandomStringUtils.randomAlphabetic(linkInfoProperty.getShortLinkLength());
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
    @LogExecutionTime
    public LinkInfoResponse getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .filter(it -> it.getActive() && it.getEndTime().isAfter(LocalDateTime.now()))
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по короткой ссылке " + shortLink));
    }

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @LogExecutionTime
    public void delete(UUID id) {
        linkInfoRepository.delete(id);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse updateLinkInfo(UpdateLinkInfoRequest request) {
        LinkInfo linkInfo = linkInfoRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по id " + request.getId()));

        if (request.getLink() != null) {
            linkInfo.setLink(request.getLink());
        }
        if (request.getEndTime() != null) {
            linkInfo.setEndTime(request.getEndTime());
        }
        if (request.getDescription() != null) {
            linkInfo.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            linkInfo.setDescription(request.getDescription());
        }

        linkInfoRepository.save(linkInfo);
        return toResponse(linkInfo);
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
