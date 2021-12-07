package com.example.sources.cofiguration;

import com.example.sources.domain.type.InteractionType;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class StringToInteractionTypeConverter implements Converter<String, InteractionType> {
    @Override
    public InteractionType convert(String source) {
        return InteractionType.valueOf(source.toUpperCase(Locale.ROOT));
    }
}
