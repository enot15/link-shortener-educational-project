package ru.prusakova.linkshortener.repository.impl;

import org.springframework.stereotype.Repository;
import ru.prusakova.linkshortener.model.LinkInfo;
import ru.prusakova.linkshortener.repository.LinkInfoRepository;

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
    public Optional<LinkInfo> findById(UUID id) {
        return findAll().stream()
                .filter(it -> it.getId() == id)
                .findFirst();
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

    @Override
    public void delete(UUID id) {
        Optional<LinkInfo> linkInfo = findById(id);
        if (linkInfo.isPresent()) {
            if (links.get(linkInfo.get().getShortLink()) != null) {
                links.remove(linkInfo.get().getShortLink());
            }
        }
    }

    @Override
    public Optional<LinkInfo> updateLinkInfo(LinkInfo linkInfo) {
        Optional<LinkInfo> oldLinkInfo = findById(linkInfo.getId());
        if (oldLinkInfo.isPresent()) {
            if (linkInfo.getLink() != null) {
                oldLinkInfo.get().setLink(linkInfo.getLink());
            }
            if (linkInfo.getEndTime() != null) {
                oldLinkInfo.get().setEndTime(linkInfo.getEndTime());
            }
            if (linkInfo.getDescription() != null) {
                oldLinkInfo.get().setDescription(linkInfo.getDescription());
            }
            if (linkInfo.getActive() != null) {
                oldLinkInfo.get().setDescription(linkInfo.getDescription());
            }
            return oldLinkInfo;
        }
        return Optional.empty();
    }
}
