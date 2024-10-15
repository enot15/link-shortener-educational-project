package ru.prusakova.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfoResponse {

    private UUID id;
    private String link;
    private String shortLink;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
    private Long openingCount;
}
