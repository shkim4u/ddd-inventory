package com.legmod.ddd.inventory.interfaces.rest.dto;

import lombok.*;

import jakarta.validation.Valid;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AppObject<T> {

    @Valid
    private T app;

    public static<T> AppObject<T> appObject(final T t) {
        return AppObject.<T>builder()
                .app(t)
                .build();
    }
}
