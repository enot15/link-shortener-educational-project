package ru.prusakova.linkshortener.repository.impl;

import org.springframework.stereotype.Repository;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    public ConcurrentMap<String, LinkInfo> links = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(links.get(shortLink));
    }

    @Override
    public Optional<LinkInfo> findByShortLinkAndActiveAndEndTimeAfter(String shortLink) {
        return Optional.ofNullable(links.get(shortLink))
                .filter(it -> it.getActive() && it.getEndTime().isAfter(LocalDateTime.now()));
    }

    @Override
    public Optional<LinkInfo> findById(UUID id) {
        return findAll().stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        if (linkInfo.getId() == null) {
            linkInfo.setId(UUID.randomUUID());
        }
        links.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAll() {
        return new ArrayList<>(links.values());
    }

    @Override
    public void delete(UUID id) {
        findById(id)
                .ifPresent(it -> links.remove(it.getShortLink()));
    }
}
