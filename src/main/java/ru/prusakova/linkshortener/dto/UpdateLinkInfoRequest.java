package ru.prusakova.linkshortener.dto;

import lombok.Data;
import ru.prusakova.linkshortener.validation.ValidLocalDateTime;
import ru.prusakova.linkshortener.validation.ValidUUID;

@Data
public class UpdateLinkInfoRequest {

    @ValidUUID(message = "Идентификатор не может отсутствовать или быть null; должен соответствовать паттерну")
    private String id;
    private String link;
    @ValidLocalDateTime(message = "Дата окончания действия короткой ссылки должна соответствовать паттерну; не может быть прошедшей")
    private String endTime;
    private String description;
    private Boolean active;
}