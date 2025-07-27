package io.github.akuniutka.scheduler.infrastructure.mapper;

import io.github.akuniutka.scheduler.domain.model.Booking;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class BookingMapper implements RowMapper<Booking> {

    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        long eventId = rs.getLong("event_id");
        long ownerId = rs.getLong("owner_id");
        Instant startTime = rs.getTimestamp("start_time").toInstant();
        Instant endTime = rs.getTimestamp("end_time").toInstant();
        return new Booking(id, eventId, ownerId, startTime, endTime);
    }
}
