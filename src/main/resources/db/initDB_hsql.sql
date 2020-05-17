DROP TABLE user_roles IF EXISTS;
DROP TABLE users IF EXISTS cascade;
DROP TABLE dishes IF EXISTS;
DROP TABLE votes IF EXISTS;
DROP TABLE restaurants IF EXISTS;
DROP SEQUENCE GLOBAL_SEQ IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS BIGINT START WITH 100000;

CREATE TABLE users
(
    id         BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name       VARCHAR(255)            NOT NULL,
    email      VARCHAR(255)            NOT NULL,
    password   VARCHAR(255)            NOT NULL,
    enabled    BOOLEAN   DEFAULT TRUE  NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id         BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name       VARCHAR(255)            NOT NULL,
    address    VARCHAR(255)            NOT NULL,
    phone      VARCHAR(255)            NOT NULL,
    url        VARCHAR(255)            NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL

);
CREATE UNIQUE INDEX restaurants_unique_name_idx
    ON restaurants (name);

CREATE TABLE dishes
(
    id            BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    date          TIMESTAMP    NOT NULL,
    price         INTEGER      NOT NULL,
    restaurant_id BIGINT       NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX dishes_unique_date_name_restaurant_id_idx
    ON dishes (date, name, restaurant_id);

CREATE TABLE votes
(
    id            BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    date          TIMESTAMP NOT NULL,
    restaurant_id BIGINT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX votes_unique_date_user_id_idx
    ON votes (date, user_id);