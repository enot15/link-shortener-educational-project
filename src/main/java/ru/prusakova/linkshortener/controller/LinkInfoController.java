package ru.prusakova.linkshortener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse<List<LinkInfoResponse>> getLinkInfos() {
        List<LinkInfoResponse> linkInfoResponses = linkInfoService.findByFilter();

        return CommonResponse.<List<LinkInfoResponse>>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponses)
                .build();
    };

    @PostMapping
    public CommonResponse<LinkInfoResponse> postCreateLinkInfo(@RequestBody @Valid CommonRequest<CreateLinkInfoRequest> request) {
        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(request.getBody());

        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    };

    @PatchMapping
    public CommonResponse<LinkInfoResponse> patchLinkInfo(@RequestBody @Valid CommonRequest<UpdateLinkInfoRequest> request) {
        LinkInfoResponse linkInfoResponse = linkInfoService.updateLinkInfo(request.getBody());

        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteLinkInfo(@PathVariable UUID id) {
        linkInfoService.delete(id);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .build();
    }
}
