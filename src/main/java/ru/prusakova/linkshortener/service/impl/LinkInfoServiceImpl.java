package ru.prusakova.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.FilterLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.PageableRequest;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.exception.NotFoundException;
import ru.prusakova.linkshortener.exception.NotFoundShortLinkException;
import ru.prusakova.linkshortener.mapper.LinkInfoMapper;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.property.LinkInfoProperty;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;
import ru.prusakova.linkshortener.service.LinkInfoService;
import ru.prusakova.logingstarter.annotation.LogExecutionTime;

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
        LinkInfo linkInfo = linkInfoRepository.findActiveShortLink(shortLink, LocalDateTime.now())
                .orElseThrow(() -> new NotFoundShortLinkException("Не найдена сущность по короткой ссылке " + shortLink));

        linkInfoRepository.incrementOpeningCountByShortLink(shortLink);

        return linkInfoMapper.toResponse(linkInfo);

    }

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterRequest) {
        PageableRequest page = filterRequest.getPage();
        Pageable pageable = mapPageable(page);

        return linkInfoRepository.findByFilter(
                    filterRequest.getLinkPart(),
                    filterRequest.getEndTimeFrom(),
                    filterRequest.getEndTimeTo(),
                    filterRequest.getDescriptionPart(),
                    filterRequest.getActive(),
                    pageable
                ).stream()
                .map(linkInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @LogExecutionTime
    public void delete(UUID id) {
        linkInfoRepository.deleteById(id);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse updateLinkInfo(UpdateLinkInfoRequest request) {
        LinkInfo linkInfo = linkInfoRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new NotFoundException("Не найдена сущность по id " + request.getId()));

        if (request.getLink() != null) {
            linkInfo.setLink(request.getLink());
        }

        linkInfo.setEndTime(request.getEndTime());

        if (request.getDescription() != null) {
            linkInfo.setDescription(request.getDescription());
        }

        if (request.getActive() != null) {
            linkInfo.setDescription(request.getDescription());
        }

        linkInfoRepository.save(linkInfo);

        return linkInfoMapper.toResponse(linkInfo);
    }

    @Override
    public void deleteOldAndNoActiveEntity() {
        List<LinkInfo> linkInfos = linkInfoRepository.findAll();
        linkInfos.stream()
                .filter(it -> !it.getActive() && it.getLastUpdateTime().isBefore(LocalDateTime.now().minusMonths(1)))
                .forEach(it -> linkInfoRepository.deleteById(it.getId()));
    }

    private Pageable mapPageable(PageableRequest page) {
        List<Sort.Order> sorts = page.getSorts().stream()
                .map(sort -> new Sort.Order(
                        Sort.Direction.valueOf(sort.getDirection()),
                        sort.getField()
                ))
                .toList();

        return PageRequest.of(page.getNumber() - 1, page.getSize(), Sort.by(sorts));
    }
}
