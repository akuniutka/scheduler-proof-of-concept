package io.github.akuniutka.scheduler.infrastructure.repository;

import jakarta.annotation.Nonnull;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

public class ExtendedBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

    public ExtendedBeanPropertySqlParameterSource(Object object) {
        super(object);
    }

    @Override
    public Object getValue(@Nonnull String paramName) throws IllegalArgumentException {
        Object value = super.getValue(paramName);
        return switch (value) {
            case Instant instant -> Timestamp.from(instant);
            case null, default -> super.getValue(paramName);
        };
    }
}
