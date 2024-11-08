package ru.prusakova.linkshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    @NotEmpty(message = "Не задано поле для сортировки")
    private String field;

    @Pattern(regexp = "ASC|DESC", message = "Указано некорректное направление сортировки")
    @Builder.Default
    private String direction = "ASC";
}
