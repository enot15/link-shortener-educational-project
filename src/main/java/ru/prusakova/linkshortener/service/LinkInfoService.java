package ru.prusakova.linkshortener.service;

import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;

import java.util.List;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();
}
