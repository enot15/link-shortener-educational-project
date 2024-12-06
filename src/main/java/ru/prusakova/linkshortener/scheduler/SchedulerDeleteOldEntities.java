package ru.prusakova.linkshortener.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.prusakova.linkshortener.service.LinkInfoService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerDeleteOldEntities {

    private final LinkInfoService linkInfoService;

    @Async("deleteOldLinkInfosExecutor")
    @Scheduled(cron = "${link-shortener.delete-old-link-infos-cron}")
    public void deleteOldLinkInfos() {
        log.info("Старт удаления старых и не активных записей");
        linkInfoService.deleteOldAndNoActiveLinkInfos();
        log.info("Старые и неактивные записи удалены");
    }
}
