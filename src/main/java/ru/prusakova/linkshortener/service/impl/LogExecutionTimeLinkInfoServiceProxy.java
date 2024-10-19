package ru.prusakova.linkshortener.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@Slf4j
public class LogExecutionTimeLinkInfoServiceProxy implements LinkInfoService {

    private final LinkInfoService linkInfoService;

    public LogExecutionTimeLinkInfoServiceProxy(LinkInfoService linkInfoService) {
        this.linkInfoService = linkInfoService;
    }

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.createLinkInfo(request);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения метода createLinkInfo: " + stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.getByShortLink(shortLink);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения метода getByShortLink: " + stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.findByFilter();
        } finally {
            stopWatch.stop();
            log.info("Время выполнения метода findByFilter: " + stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public void delete(UUID id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            linkInfoService.delete(id);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения метода delete: " + stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public LinkInfoResponse updateLinkInfo(UpdateLinkInfoRequest request) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return linkInfoService.updateLinkInfo(request);
        } finally {
            stopWatch.stop();
            log.info("Время выполнения метода updateLinkInfo: " + stopWatch.getTotalTimeMillis());
        }
    }
}
