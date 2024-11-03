package ru.prusakova.linkshortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.prusakova.linkshortener.validation.ValidUUID;

import java.time.LocalDateTime;

@Data
public class UpdateLinkInfoRequest {

    @NotEmpty(message = "Идентификатор не может отсутствовать или быть null")
    @ValidUUID(message = "Идентификатор должен соответствовать паттерну UUID")
    private String id;
    @Pattern(regexp = "http[s]?://.+\\..+$", message = "url не соответствует паттерну")
    private String link;
    @Future(message = "Дата окончания действия короткой ссылки не может быть прошедшей")
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}