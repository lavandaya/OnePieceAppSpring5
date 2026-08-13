INSERT INTO crews (name, has_bounty, ship_name) VALUES
                                                    ('Straw Hat Pirates', TRUE, 'Going Merry'),
                                                    ('Heart Pirates', TRUE, 'Polar Tang');

INSERT INTO users (user_id, username, password, email, enabled, role) VALUES
                                                                    (1, 'luffy', '$2a$10$otX76CQXwrcqXJSsYNOgXe6YtFEkSjSfbG3gFe2CpcJCbzjfWVevq', 'luffy@strawhat.com',  TRUE, 'USER'),
                                                                    (2, 'zoro',  '$2a$10$otX76CQXwrcqXJSsYNOgXe6YtFEkSjSfbG3gFe2CpcJCbzjfWVevq', 'zoro@strawhat.com',   TRUE, 'USER'),
                                                                    (3, 'admin', '$2a$10$ywZtQGbawmBrzq.bNaMigeamw7jBL3APJPYGTuby2Bya3yexzO36G', 'admin@onepiece.com',  TRUE, 'ADMIN');

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 4;

INSERT INTO characters (character_id, name, age, appearance, powertype, power, crew_name, character_type, sword_name, owner_id) VALUES
                                                                                                                          (1, 'Luffy',     18, 'https://placehold.co/400x400/d62828/ffffff?text=Luffy',     'DEVIL_FRUIT', 10, 'Straw Hat Pirates', 'CHARACTER', NULL, 1),
                                                                                                                          (2, 'Zoro',      20, 'https://placehold.co/400x400/2a6f4e/ffffff?text=Zoro',      'WILL',         9, 'Straw Hat Pirates', 'SWORDSMAN', 'Wado Ichimonji', 2),
                                                                                                                          (3, 'Sanji',     20, 'https://placehold.co/400x400/e8a23d/000000?text=Sanji',     'NO_POWER',     8, 'Straw Hat Pirates', 'CHARACTER', NULL, 1),
                                                                                                                          (4, 'Ussop',     19, 'https://placehold.co/400x400/8a5a44/ffffff?text=Ussop',     'NO_POWER',     1, 'Straw Hat Pirates', 'CHARACTER', NULL, 2),
                                                                                                                          (5, 'Nami',      19, 'https://placehold.co/400x400/e07a9b/000000?text=Nami',      'NO_POWER',     1, 'Straw Hat Pirates', 'CHARACTER', NULL, 3),
                                                                                                                          (6, 'Trafalgar', 21, 'https://placehold.co/400x400/4a4e69/ffffff?text=Trafalgar', 'DEVIL_FRUIT', 10, 'Heart Pirates',     'CHARACTER', NULL, 3);

INSERT INTO battles (battle_id, name, location, fought_at, winner) VALUES
                                                                       (1, 'Arlong Park showdown',    'Arlong Park',      '2005-07-23 12:20:00', 'Luffy'),
                                                                       (2, 'Duel of Zoro and Mihawk', 'Baratie',          '2005-09-11 09:03:00', 'Zoro'),
                                                                       (3, 'Candle show',             'Island of Giants', '2005-12-01 18:40:00', 'Zoro'),
                                                                       (4, 'Crocodile',               'Alabasta',         '2006-03-15 16:45:00', 'Luffy'),
                                                                       (5, 'Enel',                    'Skypiea',          '2007-08-09 10:30:00', 'Luffy'),
                                                                       (6, 'Rob Lucci',               'Enies Lobby',      '2008-11-02 18:15:00', 'Ussop'),
                                                                       (7, 'Gecko Moria',             'Thriller Bark',    '2009-05-20 22:00:00', 'Zoro'),
                                                                       (8, 'Doflamingo',              'Dressrosa',        '2013-09-28 14:10:00', 'Nami');

INSERT INTO character_battles (character_id, battle_id) VALUES
                                                            (1, 1), (1, 4), (1, 5),
                                                            (2, 2), (2, 3), (2, 7),
                                                            (3, 1),
                                                            (4, 1), (4, 6),
                                                            (5, 1), (5, 8),
                                                            (6, 8);

ALTER TABLE characters ALTER COLUMN character_id RESTART WITH 7;
ALTER TABLE battles ALTER COLUMN battle_id RESTART WITH 9;