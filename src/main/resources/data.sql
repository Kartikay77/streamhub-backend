DELETE FROM watch_history;
DELETE FROM favorites;
DELETE FROM subscriptions;
DELETE FROM content;
DELETE FROM users;

INSERT INTO users (name, email, password, role)
VALUES
('Admin User', 'admin@streamhub.com', '$2a$10$9pD2o1uXh9l6L2r5W9Hz7OX46wvoVfJQpA3M1DMHfE6b8my0CwZQK', 'ROLE_ADMIN'),
('John Doe', 'john@streamhub.com', '$2a$10$9pD2o1uXh9l6L2r5W9Hz7OX46wvoVfJQpA3M1DMHfE6b8my0CwZQK', 'ROLE_USER');

INSERT INTO content (title, genre, description, release_year, rating)
VALUES
('Skybound', 'Sci-Fi', 'A futuristic sci-fi drama.', 2024, 8.7),
('Kitchen Wars', 'Reality', 'Competitive cooking show.', 2023, 7.9),
('Signal Zero', 'Thriller', 'A cyber-thriller about surveillance.', 2025, 8.2);

INSERT INTO subscriptions (tier, status, created_at, canceled_at, user_id)
VALUES
('PREMIUM', 'ACTIVE', NOW(), NULL, (SELECT id FROM users WHERE email = 'admin@streamhub.com')),
('BASIC', 'ACTIVE', NOW(), NULL, (SELECT id FROM users WHERE email = 'john@streamhub.com'));

INSERT INTO favorites (user_id, content_id)
VALUES
((SELECT id FROM users WHERE email = 'john@streamhub.com'),
 (SELECT id FROM content WHERE title = 'Skybound')),
((SELECT id FROM users WHERE email = 'john@streamhub.com'),
 (SELECT id FROM content WHERE title = 'Signal Zero'));

INSERT INTO watch_history (watched_minutes, watched_at, user_id, content_id)
VALUES
(45, NOW(),
 (SELECT id FROM users WHERE email = 'john@streamhub.com'),
 (SELECT id FROM content WHERE title = 'Skybound')),
(30, NOW(),
 (SELECT id FROM users WHERE email = 'john@streamhub.com'),
 (SELECT id FROM content WHERE title = 'Kitchen Wars'));