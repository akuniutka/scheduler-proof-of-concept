package io.github.akuniutka.scheduler.infrastructure.repository;

import io.github.akuniutka.scheduler.domain.model.Booking;
import io.github.akuniutka.scheduler.domain.repository.BookingRepository;
import io.github.akuniutka.scheduler.infrastructure.mapper.BookingMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class BookingRepositoryImpl implements BookingRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO bookings (id, event_id, owner_id, start_time, end_time)
            VALUES (:id, :eventId, :ownerId, :startTime, :endTime)
            """.stripIndent();

    private static final String CHECK_SLOT_QUERY = """
            SELECT * FROM (
                SELECT *
                FROM bookings
                WHERE owner_id = :ownerId AND start_time < :endTime
                ORDER BY owner_id, start_time DESC
                LIMIT 1)
            WHERE end_time > :startTime
            """.stripIndent();

    private static final String FIND_ALL_BY_OWNER_ID_ORDER_BY_START_TIME_QUERY = """
           SELECT * FROM bookings
           WHERE owner_id = :ownerId
           ORDER BY start_time
           """.stripIndent();

    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<Booking> mapper;

    public BookingRepositoryImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new BookingMapper();
    }

    @Override
    public void insertAll(Collection<Booking> bookings) {
        SqlParameterSource[] params = bookings.stream()
                .map(this::toSqlParameterSource)
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(INSERT_QUERY, params);
    }

    @Override
    public Optional<Booking> findAnyByOwnerIdBetweenStartTimeAndEndTime(long ownerId, Instant startTime,
            Instant endTime) {
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

    @Override
    public List<Booking> findAllByOwnerIdOrderByStartTime(long ownerId) {
        SqlParameterSource params = new MapSqlParameterSource("ownerId", ownerId);
        return jdbc.query(FIND_ALL_BY_OWNER_ID_ORDER_BY_START_TIME_QUERY, params, mapper);
    }

    private SqlParameterSource toSqlParameterSource(Booking booking) {
        return new MapSqlParameterSource()
                .addValue("id", booking.id())
                .addValue("eventId", booking.eventId())
                .addValue("ownerId", booking.ownerId())
                .addValue("startTime", Timestamp.from(booking.startTime()))
                .addValue("endTime", Timestamp.from(booking.endTime()));
    }
}
