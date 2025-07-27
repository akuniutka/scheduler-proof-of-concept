package io.github.akuniutka.scheduler.infrastructure.mapper;

import io.github.akuniutka.scheduler.domain.model.Slot;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class SlotMapper implements RowMapper<Slot> {

    @Override
    public Slot mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        long eventId = rs.getLong("event_id");
        Instant startTime = rs.getTimestamp("start_time").toInstant();
        Instant endTime = rs.getTimestamp("end_time").toInstant();
        return new Slot(id, eventId, startTime, endTime);
    }
}
