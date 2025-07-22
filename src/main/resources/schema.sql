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
