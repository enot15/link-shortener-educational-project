package ru.prusakova.linkshortener.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.property.LinkInfoProperty;
import ru.prusakova.linkshortener.service.LinkInfoService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerDeleteOldEntities {

    private final LinkInfoService linkInfoService;
    private final LinkInfoProperty linkInfoProperty;

    @Async("singleExecutor")
    @Scheduled(cron = "#{@linkInfoProperty.scheduledDailyCron}")
    public void deleteOldEntities() {
        linkInfoService.deleteOldAndNoActiveEntity();
        log.info("Старые записи удалены");
    }
}
