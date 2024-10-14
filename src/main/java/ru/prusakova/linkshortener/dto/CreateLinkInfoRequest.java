package ru.prusakova.linkshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkInfoRequest {

    /**
     * Полная ссылка
     */
    private String link;

    /**
     * Время истеченияя жизни короткой ссылки
     */
    private LocalDateTime endTime;

    /**
     * Описание
     */
    private String description;

    /**
     * Признак активности ссылки
     */
    private Boolean active;
}
