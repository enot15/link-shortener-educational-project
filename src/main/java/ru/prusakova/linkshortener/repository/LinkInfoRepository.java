package ru.prusakova.linkshortener.repository;

import ru.prusakova.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository {

    Optional<LinkInfo> findByShortLink(String shortLink);

    Optional<LinkInfo> findById(UUID id);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();

    void delete(UUID id);
}
