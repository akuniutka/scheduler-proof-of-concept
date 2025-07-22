package io.github.akuniutka.scheduler.infrastructure.repository;

import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.repository.BookingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO bookings (id, event_id, owner_id, start_time, end_time)
            VALUES (:id, :eventId, :ownerId, :startTime, :endTime)
            """.stripIndent();

    private static final String CHECK_SLOT_QUERY = """
            SELECT * FROM bookings
            WHERE owner_id = :ownerId AND start_time < :endTime AND end_time > :startTime
            LIMIT 1
            """.stripIndent();

    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<Booking> mapper;

    public BookingRepositoryImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new DataClassRowMapper<>(Booking.class);
    }

    @Override
    public void insertAll(Booking[] bookings) {
        SqlParameterSource[] parameterSources = new SqlParameterSource[bookings.length];
        for (int i = 0; i < bookings.length; i++) {
            parameterSources[i] = new ExtendedBeanPropertySqlParameterSource(bookings[i]);
        }
        jdbc.batchUpdate(INSERT_QUERY, parameterSources);
    }

    @Override
    public Optional<Booking> checkSlot(Long ownerId, Instant startTime, Instant endTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ownerId", ownerId)
                .addValue("startTime", Timestamp.from(startTime))
                .addValue("endTime", Timestamp.from(endTime));
        try {
            return Optional.ofNullable(jdbc.queryForObject(CHECK_SLOT_QUERY, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
