-- liquibase formatted sql

-- changeset boris:2
INSERT INTO users (id, login, pass_hash, first_name, second_name, third_name, birth_date, role) VALUES
(1, 'sdfoijvova', '$2a$10$O218/lltgvVWyVc5i7D3ie.AMJxGJUm1vhOsuKq2GYkYQYxuEzOGq', 'Клим', 'Хорин', NULL, '1969-10-20', 'USER'),
(4, 'admin', '$2a$10$0PKz0fhCPVQ7k6QNQSqM4OuQPf64EC4vAPONpyzZLflEnERAFru1S', 'Константин', 'Курилов', 'Кирилович', '1999-08-11', 'ADMIN'),
(5, 'asdou899', '$2a$10$EPHa2Gsrpqn.ad0/uEDvBuBZuEMiZlbv2njWpvjstZNLJ4L2cEvtu', 'Алиса', 'Волдыкова', 'Павловна', '2000-05-10', 'USER'),
(6, 'esosa090la', '$2a$10$uMSX/vcT9qJxUw5M7PCyqOMPGedtQCjByF/yjTKElugn.1kYKfm6O', 'Олег', 'Рубилин', NULL, '1983-12-29', 'USER'),
(7, 'jjjklesosa090la', '$2a$10$qrn.0FbhItozHc5jzvnbvu9Ybaah3.8ItRzf2QYShYlK8RtrgqdUK', 'Олег', 'Рубилин', NULL, '1983-12-29', 'USER'),
(9, 'jjjk7lesosa090l8a', '$2a$10$.IUJVCgddFJsV38x0RpbMOzguueqbDSmuR82JjyTk4gCc27H96pX6', 'Никита', 'Хоринов', NULL, '1984-10-26', 'USER'),
(10, 'auuuasdkhihi', '$2a$10$tlxsmcf26HVhm7Y5atAd6ubwtaElYbhabX2EJ/8c.RJ/55W81ZAZi', 'Никита', 'Хорошовский', NULL, '1989-10-26', 'USER'),
(11, 'aooosdkhihi', '$2a$10$Aob9uhRxcPMHXITQjdPTveqbXJpH70mHL5XOGEwOa4Q11tAgPD8cS', 'Ной', 'Фавогов', NULL, '1939-11-23', 'USER'),
(44, 'licici', '$2a$10$DHVDJHrJdsQedjl/gnDqZeM8.FeMYGBmyJkVFPV4Ux8Dt7M/nDkb6', 'Николай', 'Орлов', NULL, '1949-01-23', 'USER'),
(45, '2licici', '$2a$10$oR39U/8051tE4CqeCN3j..9lGJ6dcJXQnilT/soeUCvPKt7zswgte', 'Никита', 'Овнов', NULL, '1949-01-23', 'USER'),
(46, '3licici', '$2a$10$f.HgaYDkYcyg67Xfd2qzF.wLLWsJjinrcdcAdrpLpVbSos6wDb.wC', 'Назар', 'Орков', NULL, '1949-01-23', 'USER');

-- changeset boris:3
INSERT INTO cards (id, user_id, card_number, last_four_digits, expiry_date, status, balance, bank_title, type) VALUES
(1, 1, 'MLXXXgxQErczvGKND0XftM86CrxXrFQwZtsTgNmcNRM=', '3209',  '2028-01-01', 'BLOCK', 8000.93, 'Бета', 'дебетовая'),
(4, 1, 'lweyNbtB9nGjhr2gAy2Vuc86CrxXrFQwZtsTgNmcNRM=', '3200',  '2027-09-01', 'EXPIRED', 508.62, 'К-банк', 'кредитная'),
(9, 1, 'NqnaQ8PG1bRYKxrLFi91/886CrxXrFQwZtsTgNmcNRM=', '2222',  '2029-12-01', 'ACTIVE', 4450.67, 'Марс', 'дебетовая'),
(13, 1, 'x7dpcrORPQHAbQtuMoCvH886CrxXrFQwZtsTgNmcNRM=', '2206', '2028-10-01', 'ACTIVE', 3500.69, 'Бета', 'дебетовая'),
(14, 1, 'pDcXfyc2vRJ0ojwMUTEFns86CrxXrFQwZtsTgNmcNRM=', '2206', '2031-11-01', 'ACTIVE', 18910.17, 'Гамма', 'дебетовая'),
(15, 5, 'czWUcAiGll8E4IYM19C5g886CrxXrFQwZtsTgNmcNRM=', '2200', '2031-11-01', 'ACTIVE', 2500.00, 'Гамма', 'дебетовая'),
(16, 5, '1QqN+lW2w0GWlUcbHb8/ac86CrxXrFQwZtsTgNmcNRM=', '9200', '2036-01-01', 'ACTIVE', 590.80, 'РОСДОР', 'дебетовая'),
(17, 5, 'k6GdPe6+pI5P/79niB8hpc86CrxXrFQwZtsTgNmcNRM=', '2281', '2036-01-01', 'ACTIVE', 590.80, 'РОСЛЕС', 'дебетовая'),
(18, 5, '7Cmu0k3PcvicQREkjanOKs86CrxXrFQwZtsTgNmcNRM=', '2441', '2036-01-01', 'ACTIVE', 163.43, 'РОСПРОД', 'дебетовая'),
(20, 1, 'Nc6reIScrRIzxjYybgtb2M86CrxXrFQwZtsTgNmcNRM=', '2443', '2036-01-01', 'ACTIVE', 223.43, 'РОСНГМ', 'дебетовая'),
(21, 1, 'z6yzEDUHRZL9JlxzwuMvJc86CrxXrFQwZtsTgNmcNRM=', '5543', '2036-01-01', 'ACTIVE', 9999998712.46, 'К-банк', 'дебетовая'),
(23, 46, 'V04pcsqt9ju9j4sZmBOvy886CrxXrFQwZtsTgNmcNRM=', '5523', '2036-01-01', 'ACTIVE', 12300.12, 'Бета', 'дебетовая'),
(24, 46, 'rPG3DYaG0KRKkSCComNy2M86CrxXrFQwZtsTgNmcNRM=', '5503', '2036-01-01', 'ACTIVE', 12300.12, 'Омега', 'кредитная');

-- changeset boris:4
INSERT INTO blockings(card_id, date_time, id, status) VALUES
(1,'2026-03-24 02:41:23.612191',3,   'PENDING'),
(9,'2026-03-24 02:42:10.003849',4,   'CANCELLED'),
(13,'2026-03-24 02:50:42.373569',6, 'PENDING'),
(9,'2026-03-24 02:42:45.81557',5,   'CANCELLED'),
(9,'2026-03-24 04:44:18.519679',7,   'CANCELLED'),
(9,'2026-03-24 18:02:23.479985',8,   'PENDING'),
(21,'2026-03-24 18:04:12.220036',9,   'CANCELLED'),
(21,'2026-03-24 18:04:40.549023',1,   	'PENDING');

-- changeset boris:5
INSERT INTO transfers (id, card_from_id, card_to_id, amount) VALUES
(1, 13, 9, 500.00),
(2, 13, 9, 500.10),
(3, 13, 9, 500.10),
(4, 13, 9, 500.10),
(5, 9, 13, 500.10),
(6, 9, 13, 500.10),
(7, 13, 14, 500.10),
(8, 13, 14, 500.10),
(9, 14, 13, 1000.00),
(10, 14, 13, 1000.00),
(11, 14, 13, 1000.00),
(12, 14, 13, 500.00),
(13, 14, 21, 0.00),
(14, 14, 21, 0.01),
(15, 14, 21, 9999980990.01),
(16, 14, 21, 99.01),
(17, 21, 20, 100.00);

-- changeset boris:6
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval('cards_id_seq', (SELECT COALESCE(MAX(id), 1) FROM cards));
SELECT setval('transfers_id_seq', (SELECT COALESCE(MAX(id), 1) FROM transfers));
SELECT setval('blockings_id_seq', (SELECT COALESCE(MAX(id), 1) FROM blockings));