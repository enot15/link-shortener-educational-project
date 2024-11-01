package ru.prusakova.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prusakova.linkshortener.dto.CreateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.LinkInfoResponse;
import ru.prusakova.linkshortener.dto.UpdateLinkInfoRequest;
import ru.prusakova.linkshortener.dto.common.CommonRequest;
import ru.prusakova.linkshortener.dto.common.CommonResponse;
import ru.prusakova.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/link-infos")
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @GetMapping
    public CommonResponse<List<LinkInfoResponse>> getLinkInfo() {
        log.info("Поступил запрос на фильтрацию");

        List<LinkInfoResponse> linkInfoResponses = linkInfoService.findByFilter();

        log.info("Информация успешно получена: {}", linkInfoResponses);

        return CommonResponse.<List<LinkInfoResponse>>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponses)
                .build();
    };

    @PostMapping
    public CommonResponse<LinkInfoResponse> postCreateLinkInfo(@RequestBody CommonRequest<CreateLinkInfoRequest> request) {
        log.info("Поступил запрос на создание короткой ссылки: {}", request);

        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(request.getBody());

        log.info("Короткая ссылка создана успешно: {}", linkInfoResponse);

        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    };

    @PatchMapping
    public CommonResponse<LinkInfoResponse> putLinkInfo(@RequestBody CommonRequest<UpdateLinkInfoRequest> request) {
        log.info("Поступил запрос на изменение информации о ссылке: {}", request);

        LinkInfoResponse linkInfoResponse = linkInfoService.updateLinkInfo(request.getBody());

        log.info("Информация о ссылке успешно изменена: {}", linkInfoResponse);

        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteLinkInfo(@PathVariable UUID id) {
        log.info("Поступил запрос на удаление информации о ссылке: id={}", id);

        linkInfoService.delete(id);

        log.info("Информация о ссылке успешно удалена: id={}", id);
        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .build();
    }
}
