package ru.prusakova.linkshortener.service;

import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.FilterLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterRequest);

    void delete(UUID id);

    LinkInfoResponse updateLinkInfo(UpdateLinkInfoRequest request);

}
