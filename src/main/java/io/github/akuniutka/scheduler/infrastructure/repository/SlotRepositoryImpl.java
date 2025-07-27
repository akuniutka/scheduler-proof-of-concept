package io.github.akuniutka.scheduler.infrastructure.repository;

import io.github.akuniutka.scheduler.domain.model.Slot;
import io.github.akuniutka.scheduler.domain.repository.SlotRepository;
import io.github.akuniutka.scheduler.infrastructure.mapper.SlotMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class SlotRepositoryImpl implements SlotRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO slots (id, event_id, start_time, end_time)
            VALUES (:id, :eventId, :startTime, :endTime)
            """.stripIndent();

    private static final String FIND_ALL_BY_EVENT_ID_ORDER_BY_START_TIME_QUERY = """
            SELECT * FROM slots
            WHERE event_id = :eventId
            ORDER BY start_time
            LIMIT 100000
            """.stripIndent();

    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<Slot> mapper;

    public SlotRepositoryImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new SlotMapper();
    }

    @Override
    public void insertAll(Collection<Slot> slots) {
        SqlParameterSource[] params = slots.stream()
                .map(ExtendedBeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(INSERT_QUERY, params);
    }

    @Override
    public List<Slot> findAllByEventIdOrderByStartTime(long eventId) {
        SqlParameterSource params = new MapSqlParameterSource("eventId", eventId);
        return jdbc.query(FIND_ALL_BY_EVENT_ID_ORDER_BY_START_TIME_QUERY, params, mapper);
    }
}
