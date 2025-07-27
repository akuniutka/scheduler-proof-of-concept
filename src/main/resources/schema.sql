DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
  id BIGINT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS events
(
  id BIGINT PRIMARY KEY,
  owner BIGINT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL
);


CREATE TABLE IF NOT EXISTS bookings
(
  id BIGINT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  owner_id BIGINT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS owner_start_end_idx ON bookings (owner_id, start_time, end_time);

CREATE TABLE IF NOT EXISTS slots
(
  id BIGINT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS event_id_start_time_idx ON slots (event_id, start_time);
