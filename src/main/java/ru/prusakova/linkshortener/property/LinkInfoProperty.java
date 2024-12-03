package ru.prusakova.linkshortener.property;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("link-shortener")
public class LinkInfoProperty {

    @Min(value = 7, message = "Длина короткой ссылки не может быть меньше 7")
    private Integer shortLinkLength;

    private String scheduledDailyCron;
}
