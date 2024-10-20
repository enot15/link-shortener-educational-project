package ru.prusakova.linkshortener.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateLinkInfoRequest {

    private UUID id;
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
