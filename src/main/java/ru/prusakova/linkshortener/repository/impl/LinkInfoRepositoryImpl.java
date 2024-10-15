package ru.prusakova.linkshortener.repository.impl;

import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    public ConcurrentMap<String, LinkInfo> links = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(links.get(shortLink));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());
        links.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAll() {
        return new ArrayList<>(links.values());
    }
}
