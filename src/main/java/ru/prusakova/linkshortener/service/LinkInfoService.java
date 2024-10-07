package ru.prusakova.linkshortener.service;

import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;

public interface LinkInfoService {

    String convertToShortLink(CreateLinkInfoRequest request);
}
