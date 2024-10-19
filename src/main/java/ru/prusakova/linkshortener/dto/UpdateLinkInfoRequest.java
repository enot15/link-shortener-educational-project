package ru.prusakova.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLinkInfoRequest {

    private UUID id;
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
