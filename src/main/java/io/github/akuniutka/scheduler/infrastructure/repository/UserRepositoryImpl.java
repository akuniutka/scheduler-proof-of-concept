package io.github.akuniutka.scheduler.infrastructure.repository;

import io.github.akuniutka.scheduler.domain.model.User;
import io.github.akuniutka.scheduler.domain.repository.UserRepository;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {

    private static final String INSERT_QUERY = """
            INSERT INTO users (id)
            VALUES (:id)
            RETURNING *
            """.stripIndent();

    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = new DataClassRowMapper<>(User.class);
    }

    @Override
    public void insert(User user) {
        SqlParameterSource params = toSqlParameterSource(user);
        jdbc.queryForObject(INSERT_QUERY, params, mapper);
    }

    private SqlParameterSource toSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.id());
    }
}
