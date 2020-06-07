DELETE FROM VOTES;
DELETE FROM DISHES;
DELETE FROM RESTAURANTS;
DELETE FROM USER_ROLES;
DELETE FROM USERS;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO RESTAURANTS (NAME, TYPE, ADDRESS, PHONE, URL)
VALUES ('McDonalds', 'Фастфуд', 'пл. Ленина, д.1', '315-25-25', 'McDonalds.ru'),
       ('Pizza_Hut', 'Итальянский', 'пл. Стачек, д.1', '374-52-52', 'pizzahut.ru');

INSERT INTO DISHES (name, date, price, RESTAURANT_ID)
VALUES ('БигМак', '2020-04-29 00:00:00', 500, 100002),
       ('Кола', '2020-04-29 00:00:00', 100, 100002),
       ('Цезарь', '2020-04-29 00:00:00', 400, 100003),
       ('Вино', '2020-04-29 00:00:00', 200, 100003),
       ('Нагетсы', '2020-04-30 00:00:00', 200, 100002),
       ('Цезарь', '2020-04-30 00:00:00', 300, 100002),
       ('Пицца', '2020-04-30 00:00:00', 400, 100003),
       ('Гаспачо', '2020-04-30 00:00:00', 300, 100003);

INSERT INTO VOTES (USER_ID, date, RESTAURANT_ID)
VALUES (100000, '2020-04-29 00:00:00', 100002),
       (100001, '2020-04-29 00:00:00', 100003),
       (100000, '2020-04-30 00:00:00', 100003);