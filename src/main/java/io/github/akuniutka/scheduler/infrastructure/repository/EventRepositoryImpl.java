package io.github.akuniutka.scheduler.infrastructure.repository;

import io.github.akuniutka.scheduler.domain.model.Event;
import io.github.akuniutka.scheduler.domain.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRepositoryImpl implements EventRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO events (id, owner, start_time, end_time)
            VALUES (:id, :owner, :startTime, :endTime)
            RETURNING *
            """.stripIndent();

    private static final String FIND_BY_ID_QUERY = """
            SELECT
                *
            FROM events
            WHERE id = :id
            """.stripIndent();

    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<Event> mapper;

    public EventRepositoryImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new DataClassRowMapper<>(Event.class);
    }

    @Override
    public void insert(Event event) {
        SqlParameterSource params = new ExtendedBeanPropertySqlParameterSource(event);
        jdbc.queryForObject(INSERT_QUERY, params, mapper);
    }

    @Override
    public Optional<Event> findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
