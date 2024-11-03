package ru.prusakova.linkshortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotEmpty(message = "Ссылка не может быть пустой")
    @Pattern(regexp = "http[s]?://.+\\..+$", message = "url не соответствует паттерну")
    private String link;

    /**
     * Время истеченияя жизни короткой ссылки
     */
    @Future(message = "Дата окончания действия короткой ссылки не может быть прошедшей")
    private LocalDateTime endTime;

    /**
     * Описание
     */
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    /**
     * Признак активности ссылки
     */
    @NotNull(message = "признак актиновсти не может отсутствовать или быть null")
    private Boolean active;
}
