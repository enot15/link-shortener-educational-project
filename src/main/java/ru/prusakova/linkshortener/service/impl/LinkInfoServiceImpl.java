package ru.prusakova.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.annotation.LogExecutionTime;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.exception.NotFoundException;
import ru.prusakova.linkshortener.mapper.LinkInfoMapper;
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

    private final LinkInfoMapper linkInfoMapper;
    private final LinkInfoRepository linkInfoRepository;
    private final LinkInfoProperty linkInfoProperty;

    @Override
    @LogExecutionTime
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request) {
        String shortLink = RandomStringUtils.randomAlphabetic(linkInfoProperty.getShortLinkLength());
        LinkInfo linkInfo = linkInfoMapper.fromCreateRequest(request, shortLink);
        LinkInfo saveLinkInfo = linkInfoRepository.save(linkInfo);
        return linkInfoMapper.toResponse(saveLinkInfo);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLinkAndActiveAndEndTimeAfter(shortLink)
                .map(linkInfoMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по короткой ссылке " + shortLink));
    }

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll().stream()
                .map(linkInfoMapper::toResponse)
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
        LinkInfo linkInfo = linkInfoRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по id " + request.getId()));

        if (request.getLink() != null) {
            linkInfo.setLink(request.getLink());
        }
        if (request.getEndTime() != null) {
            linkInfo.setEndTime(LocalDateTime.parse(request.getEndTime()));
        }
        if (request.getDescription() != null) {
            linkInfo.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            linkInfo.setDescription(request.getDescription());
        }

        linkInfoRepository.save(linkInfo);
        return linkInfoMapper.toResponse(linkInfo);
    }
}
