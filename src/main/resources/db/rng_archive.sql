-- TABLES CREATION
CREATE TABLE rng_archive.users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(45) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    uid VARCHAR(10) NOT NULL UNIQUE,
    is_admin TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE rng_archive.news (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    new_type ENUM('event', 'banner', 'code') NOT NULL,
    description VARCHAR(150) NOT NULL,
    link VARCHAR(250),
    date_of_publication DATE DEFAULT (CURRENT_DATE()),
    is_deleted TINYINT DEFAULT 0
);
CREATE TABLE rng_archive.stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stat_name VARCHAR(25) NOT NULL,
    stat_type ENUM('main_only', 'both') NOT NULL,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE rng_archive.artifact_sets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    set_name VARCHAR(50) UNIQUE NOT NULL,
    set_image VARCHAR(60) UNIQUE NOT NULL,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE rng_archive.artifact_pieces (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    piece_type ENUM('flower', 'feather', 'sands', 'goblet', 'circlet') NOT NULL,
    piece_name VARCHAR(50) UNIQUE NOT NULL,
    set_id BIGINT NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_artifact_pieces_artifact_sets Foreign Key (set_id) REFERENCES artifact_sets (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE rng_archive.users_artifacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    main_stat_id BIGINT NOT NULL,
    artifact_piece_id BIGINT NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_users_artifacts_users Foreign Key (user_id) REFERENCES users (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_users_artifacts_stats Foreign Key (main_stat_id) REFERENCES stats (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_users_artifacts_artifact_pieces Foreign Key (artifact_piece_id) REFERENCES artifact_pieces (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE rng_archive.secondary_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stat_id BIGINT NOT NULL,
    user_artifact_id BIGINT NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_secondary_stats_stats Foreign Key (stat_id) REFERENCES stats (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_secondary_stats_users_artifacts Foreign Key (user_artifact_id) REFERENCES users_artifacts (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE rng_archive.banners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    banner_type ENUM('limited_character', 'weapon', 'standard') NOT NULL,
    banner_name VARCHAR(60) NOT NULL,
    banner_version VARCHAR(5) NOT NULL,
    banner_phase ENUM('1', '2', '-') NOT NULL,
    banner_start_date DATE NOT NULL,
    banner_image VARCHAR(60) UNIQUE NOT NULL,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE rng_archive.units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    unit_type ENUM('character', 'weapon') NOT NULL,
    unit_name VARCHAR(60) NOT NULL,
    number_of_stars ENUM('3', '4', '5') NOT NULL,
    unit_banner ENUM('all', 'character', 'weapon') NOT NULL,
    unit_image VARCHAR(60) UNIQUE NOT NULL,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE rng_archive.banners_units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    banner_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_banners_units_banners Foreign Key (banner_id) REFERENCES banners (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_banners_units_units Foreign Key (unit_id) REFERENCES units (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE rng_archive.pulls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    banner_id BIGINT NOT NULL,
    pulls_amount INT NOT NULL CHECK (pulls_amount > 1 AND pulls_amount < 89),
    won_50_50 TINYINT NOT NULL,
    activated_capturing_radiance TINYINT NOT NULL,
    pull_date DATE DEFAULT (CURRENT_DATE()),
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_pulls_users Foreign Key (user_id) REFERENCES users (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pulls_banners Foreign Key (banner_id) REFERENCES banners (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE rng_archive.pulls_units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pull_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    is_deleted TINYINT DEFAULT 0,
    CONSTRAINT fk_pulls_units_pulls Foreign Key (pull_id) REFERENCES pulls (id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pulls_units_units Foreign Key (unit_id) REFERENCES units (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- INSERTS
-- Stats
INSERT INTO rng_archive.stats (stat_name, stat_type) VALUES
('HP', 'both'),
('HP%', 'both'),
('ATK', 'both'),
('ATK%', 'both'),
('DEF', 'both'),
('DEF%', 'both'),
('Elemental Mastery', 'both'),
('Energy Recharge', 'both'),
('Crit Rate', 'both'),
('Crit DMG', 'both'),
('Healing Bonus', 'main_only'),
('Pyro DMG Bonus', 'main_only'),
('Electro DMG Bonus', 'main_only'),
('Hydro DMG Bonus', 'main_only'),
('Dendro DMG Bonus', 'main_only'),
('Anemo DMG Bonus', 'main_only'),
('Geo DMG Bonus', 'main_only'),
('Physical DMG Bonus', 'main_only');

-- Artifact sets
INSERT INTO rng_archive.artifact_sets (set_name, set_image) VALUES
('Gladiator''s Finale', 'Gladiator''s-Finale.jpg'),
('Wanderer''s Troupe', 'Wanderer''s-Troupe.jpg'),
('Viridescent Venerer', 'Viridescent-Venerer.jpg'),
('Maiden Beloved', 'Maiden-Beloved.jpg'),
('Thundersoother', 'Thundersoother.jpg'),
('Thundering Fury', 'Thundering-Fury.jpg'),
('Crimson Witch of Flames', 'Crimson-Witch-of-Flames.jpg'),
('Lavawalker', 'Lavawalker.jpg'),
('Retracing Bolide', 'Retracing-Bolide.jpg'),
('Archaic Petra', 'Archaic-Petra.jpg'),
('Noblesse Oblige', 'Noblesse-Oblige.jpg'),
('Bloodstained Chivalry', 'Bloodstained-Chivalry.jpg'),
('Blizzard Strayer', 'Blizzard-Strayer.jpg'),
('Heart of Depth', 'Heart-of-Depth.jpg'),
('Tenacity of the Millelith', 'Tenacity-of-the-Millelith.jpg'),
('Pale Flame', 'Pale-Flame.jpg'),
('Shimenawa''s Reminiscence', 'Shimenawa''s-Reminiscence.jpg'),
('Emblem of Severed Fate', 'Emblem-of-Severed-Fate.jpg'),
('Husk of Opulent Dreams', 'Husk-of-Opulent-Dreams.jpg'),
('Ocean-Hued Clam', 'Ocean-Hued-Clam.jpg'),
('Vermillion Hereafter', 'Vermillion-Hereafter.jpg'),
('Echoes of an Offering', 'Echoes-of-an-Offering.jpg'),
('Deepwood Memories', 'Deepwood-Memories.jpg'),
('Gilded Dreams', 'Gilded-Dreams.jpg'),
('Desert Pavilion Chronicle', 'Desert-Pavilion-Chronicle.jpg'),
('Flower of Paradise Lost', 'Flower-of-Paradise-Lost.jpg'),
('Vourukasha''s Glow', 'Vourukasha''s-Glow.jpg'),
('Nymph''s Dream', 'Nymph''s-Dream.jpg'),
('Marechaussee Hunter', 'Marechaussee-Hunter.jpg'),
('Golden Troupe', 'Golden-Troupe.jpg'),
('Song of Days Past', 'Song-of-Days-Past.jpg'),
('Nighttime Whispers in the Echoing Woods', 'Nighttime-Whispers-in-the-Echoing-Woods.jpg'),
('Fragment of Harmonic Whimsy', 'Fragment-of-Harmonic-Whimsy.jpg'),
('Unfinished Reverie', 'Unfinished-Reverie.jpg'),
('Obsidian Codex', 'Obsidian-Codex.jpg'),
('Scroll of the Hero of Cinder City', 'Scroll-of-the-Hero-of-Cinder-City.jpg'),
('Long Night''s Oath', 'Long-Night''s-Oath.jpg'),
('Finale of the Deep Galleries', 'Finale-of-the-Deep-Galleries.jpg');

-- Artifact pieces
-- Gladiator's Finale
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Gladiator''s Nostalgia', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gladiator''s Finale')),
('feather', 'Gladiator''s Destiny', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gladiator''s Finale')),
('sands', 'Gladiator''s Longing', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gladiator''s Finale')),
('goblet', 'Gladiator''s Intoxication', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gladiator''s Finale')),
('circlet', 'Gladiator''s Triumphus', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gladiator''s Finale'));

-- Wanderer's Troupe
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Troupe''s Dawnlight', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Wanderer''s Troupe')),
('feather', 'Bard''s Arrow Feather', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Wanderer''s Troupe')),
('sands', 'Concert''s Final Hour', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Wanderer''s Troupe')),
('goblet', 'Wanderer''s String-Kettle', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Wanderer''s Troupe')),
('circlet', 'Conductor''s Top Hat', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Wanderer''s Troupe'));

-- Viridescent Venerer
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'In Rememberence of Viridescent Fields', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Viridescent Venerer')),
('feather', 'Viridescent Arrow Feather', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Viridescent Venerer')),
('sands', 'Viridescent Venerer''s Determination', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Viridescent Venerer')),
('goblet', 'Viridescent Venerer''s Vessel', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Viridescent Venerer')),
('circlet', 'Viridescent Venerer''s Diadem', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Viridescent Venerer'));

-- Maiden Beloved
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Maiden''s Distant Love', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Maiden Beloved')),
('feather', 'Maiden''s Heart-stricken Infatuation', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Maiden Beloved')),
('sands', 'Maiden''s Passing Youth', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Maiden Beloved')),
('goblet', 'Maiden''s Fleeting Leisure', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Maiden Beloved')),
('circlet', 'Maiden''s Fading Beauty', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Maiden Beloved'));

-- Thundersoother
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Thundersoother''s Heart', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundersoother')),
('feather', 'Thundersoother''s Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundersoother')),
('sands', 'Hour of Soothing Thunder', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundersoother')),
('goblet', 'Thundersoother''s Goblet', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundersoother')),
('circlet', 'Thundersoother''s Diadem', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundersoother'));

-- Thundering Fury
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Thunderbird''s Mercy', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundering Fury')),
('feather', 'Survivor of Catastrophe', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundering Fury')),
('sands', 'Hourglass of Thunder', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundering Fury')),
('goblet', 'Omen of Thunderstorm', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundering Fury')),
('circlet', 'TThunder Summoner''s Crown', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Thundering Fury'));

-- Crimson Witch of Flames
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Witch''s Flower of Blaze', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Crimson Witch of Flames')),
('feather', 'Witch''s Ever-Burning Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Crimson Witch of Flames')),
('sands', 'Witch''s End Time', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Crimson Witch of Flames')),
('goblet', 'Witch''s Heart Flames', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Crimson Witch of Flames')),
('circlet', 'Witch''s Scorching Hat', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Crimson Witch of Flames'));

-- Lavawalker
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Lavawalker''s Resolution', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Lavawalker')),
('feather', 'Lavawalker''s Salvation', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Lavawalker')),
('sands', 'Lavawalker''s Torment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Lavawalker')),
('goblet', 'Lavawalker''s Epiphany', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Lavawalker')),
('circlet', 'Lavawalker''s Wisdom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Lavawalker'));

-- Retracing Bolide
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Summer Night''s Bloom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Retracing Bolide')),
('feather', 'Summer Night''s Finale', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Retracing Bolide')),
('sands', 'Summer Night''s Moment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Retracing Bolide')),
('goblet', 'Summer Night''s Waterballoon', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Retracing Bolide')),
('circlet', 'Summer Night''s Mask', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Retracing Bolide'));

-- Archaic Petra
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Flower of the Creviced Cliff', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Archaic Petra')),
('feather', 'Feather of Jagged Peaks', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Archaic Petra')),
('sands', 'Sundial of Enduring Jade', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Archaic Petra')),
('goblet', 'Goblet of Chiseled Crag', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Archaic Petra')),
('circlet', 'Mask of Solitude Basalt', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Archaic Petra'));

-- Noblesse Oblige
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Royal Flora', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Noblesse Oblige')),
('feather', 'Royal Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Noblesse Oblige')),
('sands', 'Royal Pocket Watch', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Noblesse Oblige')),
('goblet', 'Royal Silver Urn', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Noblesse Oblige')),
('circlet', 'Royal Masque', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Noblesse Oblige'));

-- Bloodstained Chivalry
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Bloodstained Flower of Iron', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Bloodstained Chivalry')),
('feather', 'Bloodstained Black Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Bloodstained Chivalry')),
('sands', 'Bloodstained Final Hour', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Bloodstained Chivalry')),
('goblet', 'Bloodstained Chevalier''s Goblet', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Bloodstained Chivalry')),
('circlet', 'Bloodstained Iron Mask', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Bloodstained Chivalry'));

-- Blizzard Strayer
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Snowswept Memory', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Blizzard Strayer')),
('feather', 'Icebreaker''s Resolve', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Blizzard Strayer')),
('sands', 'Frozen Homeland''s Demise', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Blizzard Strayer')),
('goblet', 'Frost-Weaved Dignity', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Blizzard Strayer')),
('circlet', 'Broken Rime''s Echo', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Blizzard Strayer'));

-- Heart of Depth
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Gilded Corsage', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Heart of Depth')),
('feather', 'Gust of Nostalgia', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Heart of Depth')),
('sands', 'Copper Compass', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Heart of Depth')),
('goblet', 'Goblet of Thundering Deep', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Heart of Depth')),
('circlet', 'Wine-Stained Tricorne', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Heart of Depth'));

-- Tenacity of the Millelith
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Flower of Accolades', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Tenacity of the Millelith')),
('feather', 'Ceremonial War-Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Tenacity of the Millelith')),
('sands', 'Orichalceous Time-Dial', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Tenacity of the Millelith')),
('goblet', 'Noble''s Pledging Vessel', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Tenacity of the Millelith')),
('circlet', 'General''s Ancient Helm', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Tenacity of the Millelith'));

-- Pale Flame
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Stainless Bloom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Pale Flame')),
('feather', 'Wise Doctor''s Pinion', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Pale Flame')),
('sands', 'Moment of Cessation', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Pale Flame')),
('goblet', 'Surpassing Cup', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Pale Flame')),
('circlet', 'Mocking Mask', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Pale Flame'));

-- Shimenawa's Reminiscence
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Entangling Bloom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Shimenawa''s Reminiscence')),
('feather', 'Shaft of Remembrance', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Shimenawa''s Reminiscence')),
('sands', 'Morning Dew''s Moment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Shimenawa''s Reminiscence')),
('goblet', 'Hopeful Heart', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Shimenawa''s Reminiscence')),
('circlet', 'Capricious Visage', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Shimenawa''s Reminiscence'));

-- Emblem of Severed Fate
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Magnificent Tsuba', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Emblem of Severed Fate')),
('feather', 'Sundered Feather', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Emblem of Severed Fate')),
('sands', 'Storm Cage', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Emblem of Severed Fate')),
('goblet', 'Scarlet Vessel', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Emblem of Severed Fate')),
('circlet', 'Ornate Kabuto', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Emblem of Severed Fate'));

-- Husk of Opulent Dreams
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Bloom Times', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Husk of Opulent Dreams')),
('feather', 'Plume of Luxury', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Husk of Opulent Dreams')),
('sands', 'Song of Life', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Husk of Opulent Dreams')),
('goblet', 'Calabash of Awakening', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Husk of Opulent Dreams')),
('circlet', 'Skeletal Hat', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Husk of Opulent Dreams'));

-- Ocean-Hued Clam
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Sea-Dyed Blossom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Ocean-Hued Clam')),
('feather', 'Deep Palace''s Plume', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Ocean-Hued Clam')),
('sands', 'Cowry of Parting', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Ocean-Hued Clam')),
('goblet', 'Pearl Cage', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Ocean-Hued Clam')),
('circlet', 'Crown of Watatsumi', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Ocean-Hued Clam'));

-- Vermillion Hereafter
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Flowering Life', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vermillion Hereafter')),
('feather', 'Feather of Nascent Light', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vermillion Hereafter')),
('sands', 'Solar Relic', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vermillion Hereafter')),
('goblet', 'Moment of the Pact', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vermillion Hereafter')),
('circlet', 'Thundering Poise', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vermillion Hereafter'));

-- Echoes of an Offering
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Soulscent Bloom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Echoes of an Offering')),
('feather', 'Jade Leaf', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Echoes of an Offering')),
('sands', 'Symbol of Felicitation', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Echoes of an Offering')),
('goblet', 'Chalice of the Font', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Echoes of an Offering')),
('circlet', 'Flowing Rings', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Echoes of an Offering'));

-- Deepwood Memories
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Labyrinth Wayfarer', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Deepwood Memories')),
('feather', 'Scholar of Vines', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Deepwood Memories')),
('sands', 'A Time of Insight', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Deepwood Memories')),
('goblet', 'Lamp of the Lost', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Deepwood Memories')),
('circlet', 'Laurel Coronet', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Deepwood Memories'));

-- Gilded Dreams
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Dreaming Steelbloom', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gilded Dreams')),
('feather', 'Feather of Judgment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gilded Dreams')),
('sands', 'The Sunken Years', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gilded Dreams')),
('goblet', 'Honeyed Final Feast', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gilded Dreams')),
('circlet', 'Shadow of the Sand King', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Gilded Dreams'));

-- Desert Pavilion Chronicle
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'The First Days of the City of Kings', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Desert Pavilion Chronicle')),
('feather', 'End of the Golden Realm', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Desert Pavilion Chronicle')),
('sands', 'Timepiece of the Lost Path', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Desert Pavilion Chronicle')),
('goblet', 'Defender of the Enchanting Dream', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Desert Pavilion Chronicle')),
('circlet', 'Legacy of the Desert High-Born', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Desert Pavilion Chronicle'));

-- Flower of Paradise Lost
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Ay-Khanoum''s Myriad', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Flower of Paradise Lost')),
('feather', 'Wilting Feast', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Flower of Paradise Lost')),
('sands', 'A Moment Congealed', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Flower of Paradise Lost')),
('goblet', 'Secret-Keeper''s Magic Bottle', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Flower of Paradise Lost')),
('circlet', 'Amethyst Crown', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Flower of Paradise Lost'));

-- Vourukasha's Glow
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Stamen of Khvarena''s Origin', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vourukasha''s Glow')),
('feather', 'Vibrant Pinion', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vourukasha''s Glow')),
('sands', 'Ancient Abscission', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vourukasha''s Glow')),
('goblet', 'Feast of Boundless Joy', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vourukasha''s Glow')),
('circlet', 'Heart of Khvarena''s Brilliance', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Vourukasha''s Glow'));

-- Nymph's Dream
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Odyssean Flower', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nymph''s Dream')),
('feather', 'Wicked Mage''s Plumule', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nymph''s Dream')),
('sands', 'Nymph''s Constancy', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nymph''s Dream')),
('goblet', 'Heroe''s Tea Party', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nymph''s Dream')),
('circlet', 'Fell Dragon''s Monocle', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nymph''s Dream'));

-- Marechaussee Hunter
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Hunter''s Brooch', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Marechaussee Hunter')),
('feather', 'Masterpiece''s Overture', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Marechaussee Hunter')),
('sands', 'Moment of Judgment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Marechaussee Hunter')),
('goblet', 'Forgotten Vessel', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Marechaussee Hunter')),
('circlet', 'Veteran''s Visage', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Marechaussee Hunter'));

-- Golden Troupe
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Golden Song''s Variation', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Golden Troupe')),
('feather', 'Golden Bird''s Shedding', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Golden Troupe')),
('sands', 'Golden Era''s Prelude', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Golden Troupe')),
('goblet', 'Golden Night''s Bustle', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Golden Troupe')),
('circlet', 'Golden Troupe''s Reward', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Golden Troupe'));

-- Song of Days Past
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Forgotten Oath of Days Past', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Song of Days Past')),
('feather', 'Recollection of Days Past', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Song of Days Past')),
('sands', 'Echoing Sound From Days Past', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Song of Days Past')),
('goblet', 'Promised Dream of Days Past', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Song of Days Past')),
('circlet', 'Poetry of Days Past', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Song of Days Past'));

-- Nighttime Whispers in the Echoing Woods
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Selfless Floral Accessory', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nighttime Whispers in the Echoing Woods')),
('feather', 'Honest Quill', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nighttime Whispers in the Echoing Woods')),
('sands', 'Faithful Hourglass', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nighttime Whispers in the Echoing Woods')),
('goblet', 'Magnanimous Ink Bottle', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nighttime Whispers in the Echoing Woods')),
('circlet', 'Compassionate Ladies'' Hat', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Nighttime Whispers in the Echoing Woods'));

-- Fragment of Harmonic Whimsy
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Harmonious Symphony Prelude', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Fragment of Harmonic Whimsy')),
('feather', 'Ancient Sea''s Nocturnal Musing', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Fragment of Harmonic Whimsy')),
('sands', 'The Grand Jape of the Turning of Fate', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Fragment of Harmonic Whimsy')),
('goblet', 'Ichor Shower Rhapsody', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Fragment of Harmonic Whimsy')),
('circlet', 'Whimsical Dance of the Withered', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Fragment of Harmonic Whimsy'));

-- Unfinished Reverie
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Dark Fruit of Bright Flowers', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Unfinished Reverie')),
('feather', 'Faded Emerald Tail', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Unfinished Reverie')),
('sands', 'Moment of Attainment', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Unfinished Reverie')),
('goblet', 'The Wine-Flask Over Which the Plan Was Hatched', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Unfinished Reverie')),
('circlet', 'Crownless Crown', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Unfinished Reverie'));

-- Obsidian Codex
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Reckoning of the Xenogenic', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Obsidian Codex')),
('feather', 'Root of the Spirit-Marrow', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Obsidian Codex')),
('sands', 'Myths of the Night Realm', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Obsidian Codex')),
('goblet', 'Pre-Banquet of the Contenders', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Obsidian Codex')),
('circlet', 'Crown of the Saints', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Obsidian Codex'));

-- Scroll of the Hero of Cinder City
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Beast Tamer''s Talisman', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Scroll of the Hero of Cinder City')),
('feather', 'Mountain Ranger''s Marker', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Scroll of the Hero of Cinder City')),
('sands', 'Mystic''s Gold Dial', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Scroll of the Hero of Cinder City')),
('goblet', 'Wandering Scholar''s Claw Cup', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Scroll of the Hero of Cinder City')),
('circlet', 'Demon-Warrior''s Feather Mask', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Scroll of the Hero of Cinder City'));

-- Long Night's Oath
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Lightkeeper''s Pledge', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Long Night''s Oath')),
('feather', 'Nightingale''s Tail Feather', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Long Night''s Oath')),
('sands', 'Undying One''s Mourning Bell', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Long Night''s Oath')),
('goblet', 'A Horn Unwinded', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Long Night''s Oath')),
('circlet', 'Dyed Tassel', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Long Night''s Oath'));

-- Finale of the Deep Galleries
INSERT INTO rng_archive.artifact_pieces (piece_type, piece_name, set_id) VALUES
('flower', 'Deep Gallery''s Echoing Song', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Finale of the Deep Galleries')),
('feather', 'Deep Gallery''s Distant Pact', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Finale of the Deep Galleries')),
('sands', 'Deep Gallery''s Moment of Oblivion', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Finale of the Deep Galleries')),
('goblet', 'Deep Gallery''s Bestowed Banquet', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Finale of the Deep Galleries')),
('circlet', 'Deep Gallery''s Lost Crown', (SELECT id FROM rng_archive.artifact_sets WHERE set_name = 'Finale of the Deep Galleries'));

-- Banners
INSERT INTO rng_archive.banners (banner_type, banner_name, banner_version, banner_phase, banner_start_date, banner_image) VALUES
('limited_character', 'Cornucopia of Contention', '5.5', '1', '2025-03-26', 'Cornucopia-of-Contention.png'),
('limited_character', 'The Crane Soars Skyward', '5.5', '1', '2025-03-26', 'The-Crane-Soars-Skyward.png'),
('limited_character', 'Forgefire''s Blessing', '5.5', '2', '2025-04-15', 'Forgefire''s-Blessing.png'),
('limited_character', 'Ballad in Goblets', '5.5', '2', '2025-04-15', 'Ballad-in-Goblets.png'),
('limited_character', 'La Chanson Cerise', '5.6', '1', '2025-05-07', 'La-Chanson-Cerise.png'),
('limited_character', 'In the Name of the Rosula', '5.6', '1', '2025-05-07', 'In-the-Name-of-the-Rosula.png'),
('limited_character', 'Seeker of Flame-Wrought Secrets', '5.6', '2', '2025-05-27', 'Seeker-of-Flame-Wrought-Secrets.png'),
('limited_character', 'Reign of Serenity', '5.6', '2', '2025-05-27', 'Reign-of-Serenity.png'),
('limited_character', 'Void Star''s Advent', '5.7', '1', '2025-06-18', 'Void-Star''s-Advent.png'),
('limited_character', 'The Transcendent One Returns', '5.7', '1', '2025-06-18', 'The-Transcendent-One-Returns.png'),
('limited_character', 'Ancient Flame Ablaze', '5.7', '2', '2025-07-08', 'Ancient-Flame-Ablaze.png'),
('limited_character', 'Ambrosial Essence', '5.7', '2', '2025-07-08', 'Ambrosial-Essence.png'),
('weapon', 'Epitome Invocation', '5.5', '1', '2025-03-26', '5.5-phase1.png'),
('weapon', 'Epitome Invocation', '5.5', '2', '2025-04-15', '5.5-phase2.png'),
('weapon', 'Epitome Invocation', '5.6', '1', '2025-05-07', '5.6-phase1.png'),
('weapon', 'Epitome Invocation', '5.6', '2', '2025-05-27', '5.6-phase2.png'),
('weapon', 'Epitome Invocation', '5.7', '1', '2025-06-18', '5.7-phase1.png'),
('weapon', 'Epitome Invocation', '5.7', '2', '2025-07-08', '5.7-phase2.png'),
('standard', 'Wonderlust Invocation', '-', '-', '2020-09-28', 'Wanderlust-Invocation.jpg');

-- Units
INSERT INTO rng_archive.units (unit_type, unit_name, number_of_stars, unit_banner, unit_image) VALUES
-- Characters in order of launch
('character', 'Amber', '4', 'all', 'amber.png'),
('character', 'Kaeya', '4', 'all', 'kaeya.png'),
('character', 'Lisa', '4', 'all', 'lisa.png'),
('character', 'Barbara', '4', 'all', 'barbara.png'),
('character', 'Razor', '4', 'all', 'razor.png'),
('character', 'Xiangling', '4', 'all', 'xianglin.png'),
('character', 'Beidou', '4', 'all', 'beidou.png'),
('character', 'Xingqiu', '4', 'all', 'xingqiu.png'),
('character', 'Ningguang', '4', 'all', 'ningguang.png'),
('character', 'Fischl', '4', 'all', 'fischl.png'),
('character', 'Bennett', '4', 'all', 'bennett.png'),
('character', 'Noelle', '4', 'all', 'noelle.png'),
('character', 'Chongyun', '4', 'all', 'chongyun.png'),
('character', 'Sucrose', '4', 'all', 'sucrose.png'),
('character', 'Jean', '5', 'all', 'jean.png'),
('character', 'Diluc', '5', 'all', 'diluc.png'),
('character', 'Qiqi', '5', 'all', 'qiqi.png'),
('character', 'Mona', '5', 'all', 'mona.png'),
('character', 'Keqing', '5', 'all', 'keqing.png'),
('character', 'Venti', '5', 'character', 'venti.png'),
('character', 'Klee', '5', 'character', 'klee.png'),
('character', 'Diona', '4', 'all', 'diona.png'),
('character', 'Tartaglia', '5', 'character', 'tartaglia.png'),
('character', 'Xinyan', '4', 'all', 'xinyan.png'),
('character', 'Zhongli', '5', 'character', 'zhongli.png'),
('character', 'Albedo', '5', 'character', 'albedo.png'),
('character', 'Ganyu', '5', 'character', 'ganyu.png'),
('character', 'Xiao', '5', 'character', 'xiao.png'),
('character', 'Hu Tao', '5', 'character', 'hutao.png'),
('character', 'Rosaria', '4', 'all', 'rosaria.png'),
('character', 'Yanfei', '4', 'all', 'yanfei.png'),
('character', 'Eula', '5', 'character', 'eula.png'),
('character', 'Kaedehara Kazuha', '5', 'character', 'kazuha.png'),
('character', 'Kamisato Ayaka', '5', 'character', 'ayaka.png'),
('character', 'Sayu', '4', 'all', 'sayu.png'),
('character', 'Yoimiya', '5', 'character', 'yoimiya.png'),
('character', 'Kujou Sara', '4', 'all', 'kujou_sara.png'),
('character', 'Raiden Shogun', '5', 'character', 'raiden.png'),
('character', 'Sangonomiya Kokomi', '5', 'character', 'kokomi.png'),
('character', 'Thoma', '4', 'all', 'thoma.png'),
('character', 'Gorou', '4', 'all', 'gorou.png'),
('character', 'Arataki Itto', '5', 'character', 'itto.png'),
('character', 'Yun Jin', '4', 'all', 'yun_jin.png'),
('character', 'Shenhe', '5', 'character', 'shenhe.png'),
('character', 'Yae Miko', '5', 'character', 'yae_miko.png'),
('character', 'Kamisato Ayato', '5', 'character', 'ayato.png'),
('character', 'Yelan', '5', 'character', 'yelan.png'),
('character', 'Kuki Shinobu', '4', 'all', 'kuki_shinobu.png'),
('character', 'Shikanoin Heizou', '4', 'all', 'shikanoin_heizou.png'),
('character', 'Collei', '4', 'all', 'collei.png'),
('character', 'Tighnari', '5', 'all', 'tighnari.png'),
('character', 'Dori', '4', 'all', 'dori.png'),
('character', 'Candace', '4', 'all', 'candace.png'),
('character', 'Cyno', '5', 'character', 'cyno.png'),
('character', 'Nilou', '5', 'character', 'nilou.png'),
('character', 'Nahida', '5', 'character', 'nahida.png'),
('character', 'Layla', '4', 'all', 'layla.png'),
('character', 'Faruzan', '4', 'all', 'faruzan.png'),
('character', 'Wanderer', '5', 'character', 'wanderer.png'),
('character', 'Yaoyao', '4', 'all', 'yaoyao.png'),
('character', 'Alhaitham', '5', 'character', 'alhaitham.png'),
('character', 'Dehya', '5', 'all', 'dehya.png'),
('character', 'Mika', '4', 'all', 'mika.png'),
('character', 'Kaveh', '4', 'all', 'kaveh.png'),
('character', 'Baizhu', '5', 'character', 'baizhu.png'),
('character', 'Kirara', '4', 'all', 'kirara.png'),
('character', 'Lynette', '4', 'all', 'lynette.png'),
('character', 'Lyney', '5', 'character', 'lyney.png'),
('character', 'Freminet', '4', 'all', 'freminet.png'),
('character', 'Neuvillette', '5', 'character', 'neuvillette.png'),
('character', 'Wriothesley', '5', 'character', 'wriothesley.png'),
('character', 'Charlotte', '4', 'all', 'charlotte.png'),
('character', 'Furina', '5', 'character', 'furina.png'),
('character', 'Navia', '5', 'character', 'navia.png'),
('character', 'Chevreuse', '4', 'all', 'chevreuse.png'),
('character', 'Gaming', '4', 'all', 'gaming.png'),
('character', 'Xianyun', '5', 'character', 'xianyun.png'),
('character', 'Chiori', '5', 'character', 'chiori.png'),
('character', 'Arlecchino', '5', 'character', 'arlecchino.png'),
('character', 'Sethos', '4', 'all', 'sethos.png'),
('character', 'Clorinde', '5', 'character', 'clorinde.png'),
('character', 'Sigewinne', '5', 'character', 'sigewinne.png'),
('character', 'Emilie', '5', 'character', 'emilie.png'),
('character', 'Kachina', '4', 'all', 'kachina.png'),
('character', 'Mualani', '5', 'character', 'mualani.png'),
('character', 'Kinich', '5', 'character', 'kinich.png'),
('character', 'Xilonen', '5', 'character', 'xilonen.png'),
('character', 'Ororon', '4', 'all', 'ororon.png'),
('character', 'Chasca', '5', 'character', 'chasca.png'),
('character', 'Citlali', '5', 'character', 'citlali.png'),
('character', 'Mavuika', '5', 'character', 'mavuika.png'),
('character', 'Lan Yan', '4', 'all', 'lan_yan.png'),
('character', 'Yumemizuki Mizuki', '5', 'all', 'yumemizuki_mizuki.png'),
('character', 'Iansan', '4', 'all', 'iansan.png'),
('character', 'Varesa', '5', 'character', 'varesa.png'),
('character', 'Ifa', '4', 'all', 'ifa.png'),
('character', 'Escoffier', '5', 'character', 'escoffier.png'),
('character', 'Dahlia', '4', 'all', 'dahlia.png'),
('character', 'Skirk', '5', 'character', 'skirk.png'),
('character', 'Ineffa', '5', 'character', 'ineffa.png'),
-- Three-star weapons
-- Bows
('weapon', 'Messenger', '3', 'all', 'messenger.png'),
('weapon', 'Raven Bow', '3', 'all', 'raven_bow.png'),
('weapon', 'Recurve Bow', '3', 'all', 'recurve_bow.png'),
('weapon', 'Sharpshooter''s Oath', '3', 'all', 'sharpshooters_oath.png'),
('weapon', 'Slingshot', '3', 'all', 'slingshot.png'),
-- Catalysts
('weapon', 'Emerald Orb', '3', 'all', 'emerald_orb.png'),
('weapon', 'Magic Guide', '3', 'all', 'magic_guide.png'),
('weapon', 'Otherworldly Story', '3', 'all', 'otherworldly_story.png'),
('weapon', 'Thrilling Tales of Dragon Slayers', '3', 'all', 'thrilling_tales_of_dragon_slayers.png'),
('weapon', 'Twin Nephrite', '3', 'all', 'twin_nephrite.png'),
-- Claymores
('weapon', 'Bloodtainted Greatsword', '3', 'all', 'bloodtainted_greatsword.png'),
('weapon', 'Debate Club', '3', 'all', 'debate_club.png'),
('weapon', 'Ferrous Shadow', '3', 'all', 'ferrous_shadow.png'),
('weapon', 'Skyrider Greatsword', '3', 'all', 'skyrider_greatsword.png'),
('weapon', 'White Iron Greatsword', '3', 'all', 'white_iron_greatsword.png'),
-- Polearms
('weapon', 'Black Tassel', '3', 'all', 'black_tassel.png'),
('weapon', 'Halberd', '3', 'all', 'halberd.png'),
('weapon', 'White Tassel', '3', 'all', 'white_tassel.png'),
-- Swords
('weapon', 'Cool Steel', '3', 'all', 'cool_steel.png'),
('weapon', 'Dark Iron Sword', '3', 'all', 'dark_iron_sword.png'),
('weapon', 'Fillet Blade', '3', 'all', 'fillet_blade.png'),
('weapon', 'Harbinger of Dawn', '3', 'all', 'harbinger_of_dawn.png'),
('weapon', 'Skyrider Sword', '3', 'all', 'skyrider_sword.png'),
('weapon', 'Traveler''s Handy Sword', '3', 'all', 'travelers_handy_sword.png'),
-- Four-star weapons
-- Bows
('weapon', 'Alley Hunter', '4', 'weapon', 'alley_hunter.png'),
('weapon', 'Mouun''s Moon', '4', 'weapon', 'mouuns_moon.png'),
('weapon', 'Flower-Wreathed Feathers', '4', 'weapon', 'flower-wreathed_feathers.png'),
('weapon', 'Mitternachts Waltz', '4', 'weapon', 'mitternachts_waltz.png'),
('weapon', 'Range Gauge', '4', 'weapon', 'range_gauge.png'),

('weapon', 'Rust', '4', 'all', 'rust.png'),
('weapon', 'Sacrificial Bow', '4', 'all', 'sacrificial_bow.png'),
('weapon', 'The Stringless', '4', 'all', 'the_stringless.png'),
('weapon', 'Favonius Warbow', '4', 'all', 'favonius_warbow.png'),
-- Catalysts
('weapon', 'Wandering Evenstar', '4', 'weapon', 'wandering_evenstar.png'),
('weapon', 'Waveriding Whirl', '4', 'weapon', 'waveriding_whirl.png'),
('weapon', 'Wine and Song', '4', 'weapon', 'wine_and_song.png'),

('weapon', 'Eye of Perception', '4', 'all', 'eye_of_perception.png'),
('weapon', 'Favonius Codex', '4', 'all', 'favonius_codex.png'),
('weapon', 'Sacrificial Fragments', '4', 'all', 'sacrificial_fragments.png'),
('weapon', 'The Widsith', '4', 'all', 'the_widsith.png'),
-- Claymores
('weapon', 'Akuoumaru', '4', 'weapon', 'akuoumaru.png'),
('weapon', 'Fruitful Hook', '4', 'weapon', 'fruitful_hook.png'),
('weapon', 'Lithic Blade', '4', 'weapon', 'lithic_blade.png'),
('weapon', 'Makhaira Aquamarine', '4', 'weapon', 'makhaira_aquamarine.png'),
('weapon', 'Portable Power Saw', '4', 'weapon', 'portable_power_saw.png'),

('weapon', 'Favonius Greatsword', '4', 'all', 'favonius_greatsword.png'),
('weapon', 'Rainslasher', '4', 'all', 'rainslasher.png'),
('weapon', 'Sacrificial Greatsword', '4', 'all', 'sacrificial_greatsword.png'),
('weapon', 'The Bell', '4', 'all', 'the_bell.png'),
-- Polearms
('weapon', 'Lithic Spear', '4', 'weapon', 'lithic_spear.png'),
('weapon', 'Mountain-Bracing Bolt', '4', 'weapon', 'mountain-bracing_bolt.png'),
('weapon', 'Prospector''s Drill', '4', 'weapon', 'prospectors_drill.png'),
('weapon', 'Wavebreaker''s Fin', '4', 'weapon', 'wavebraekers_fin.png'),

('weapon', 'Dragon''s Bane', '4', 'all', 'dragons_bane.png'),
('weapon', 'Favonius Lance', '4', 'all', 'favonius_lance.png'),
-- Swords
('weapon', 'Sturdy Bone', '4', 'weapon', 'sturdy_bone.png'),
('weapon', 'The Alley Flash', '4', 'weapon', 'the_alley_flash.png'),
('weapon', 'The Dockhand''s Assistant', '4', 'weapon', 'the_dockhands_assistant.png'),
('weapon', 'Xiphos'' Moonlight', '4', 'weapon', 'xiphos_moonlight.png'),

('weapon', 'Favonius Sword', '4', 'all', 'favonius_sword.png'),
('weapon', 'Lion''s Roar', '4', 'all', 'lions_roar.png'),
('weapon', 'Sacrificial Sword', '4', 'all', 'sacrificial_sword.png'),
('weapon', 'The Flute', '4', 'all', 'the_flute.png'),
-- Five-star weapons
-- Bows
('weapon', 'Aqua Simulacra', '5', 'weapon', 'aqua_simulacra.png'),
('weapon', 'Astral Vulture''s Crimson Plumage', '5', 'weapon', 'astral_vultures_crimson_plumage.png'),
('weapon', 'Elegy for the End', '5', 'weapon', 'elegy_for_the_end.png'),
('weapon', 'Hunter''s Path', '5', 'weapon', 'hunters_path.png'),
('weapon', 'Polar Star', '5', 'weapon', 'polar_star.png'),
('weapon', 'Silvershower Heartstrings', '5', 'weapon', 'silvershower_heartstrings.png'),
('weapon', 'The First Great Magic', '5', 'weapon', 'the_first_great_magic.png'),
('weapon', 'Thundering Pulse', '5', 'weapon', 'thundering_pulse.png'),

('weapon', 'Skyward Harp', '5', 'all', 'skyward_harp.png'),
('weapon', 'Amos'' Bow', '5', 'all', 'amos_bow.png'),
-- Catalysts
('weapon', 'A Thousand Floating Dreams', '5', 'weapon', 'a_thousand_floating_dreams.png'),
('weapon', 'Cashflow Supervision', '5', 'weapon', 'cashflow_supervision.png'),
('weapon', 'Crane''s Echoing Call', '5', 'weapon', 'cranes_echoing_call.png'),
('weapon', 'Everlasting Moonglow', '5', 'weapon', 'everlasting_moonglow.png'),
('weapon', 'Jadefall''s Splendor', '5', 'weapon', 'jadefalls_splendor.png'),
('weapon', 'Kagura''s Verity', '5', 'weapon', 'kaguras_verity.png'),
('weapon', 'Memory of Dust', '5', 'weapon', 'memory_of_dust.png'),
('weapon', 'Starcaller''s Watch', '5', 'weapon', 'starcallers_watch.png'),
('weapon', 'Sunny Morning Sleep-In', '5', 'weapon', 'sunny_morning_sleep-in.png'),
('weapon', 'Surf''s Up', '5', 'weapon', 'surfs_up.png'),
('weapon', 'Tome of the Eternal Flow', '5', 'weapon', 'tome_of_the_eternal_flow.png'),
('weapon', 'Tulaytullah''s Remembrance', '5', 'weapon', 'tulaytullahs_remembrance.png'),
('weapon', 'Vivid Notions', '5', 'weapon', 'vivid_notions.png'),

('weapon', 'Lost Prayer to the Sacred Winds', '5', 'all', 'lost_prayer_to_the_sacred_winds.png'),
('weapon', 'Skyward Atlas', '5', 'all', 'skyward_atlas.png'),
-- Claymores
('weapon', 'A Thousand Blazing Suns', '5', 'weapon', 'a_thousand_blazing_suns.png'),
('weapon', 'Beacon of the Reed Sea', '5', 'weapon', 'beacon_of_the_reed_sea.png'),
('weapon', 'Fang of the Mountain King', '5', 'weapon', 'fang_of_the_mountain_king.png'),
('weapon', 'Redhorn Stonethresher', '5', 'weapon', 'redhorn_stonethresher.png'),
('weapon', 'Song of Broken Pines', '5', 'weapon', 'song_of_broken_pines.png'),
('weapon', 'The Unforged', '5', 'weapon', 'the_unforged.png'),
('weapon', 'Verdict', '5', 'weapon', 'verdict.png'),

('weapon', 'Skyward Pride', '5', 'all', 'skyward_pride.png'),
('weapon', 'Wolf''s Gravestone', '5', 'all', 'wolfs_gravestone.png'),
-- Polearms
('weapon', 'Calamity Queller', '5', 'weapon', 'calamity_queller.png'),
('weapon', 'Crimson Moon''s Semblance', '5', 'weapon', 'crimson_moons_semblance.png'),
('weapon', 'Engulfing Lightning', '5', 'weapon', 'engulfing_lightning.png'),
('weapon', 'Lumidouce Elegy', '5', 'weapon', 'lumidouce_elegy.png'),
('weapon', 'Staff of Homa', '5', 'weapon', 'staff_of_homa.png'),
('weapon', 'Staff of the Scarlet Sands', '5', 'weapon', 'staff_of_the_scarlet_sands.png'),
('weapon', 'Symphonist of Scents', '5', 'weapon', 'symphonist_of_scents.png'),
('weapon', 'Vortex Vanquisher', '5', 'weapon', 'vortex_vanquisher.png'),

('weapon', 'Primordial Jade Winged-Spear', '5', 'all', 'primordial_jade_winged_spear.png'),
('weapon', 'Skyward Spine', '5', 'all', 'skyward_spine.png'),
-- Swords
('weapon', 'Absolution', '5', 'weapon', 'absolution.png'),
('weapon', 'Azurelight', '5', 'weapon', 'azurelight.png'),
('weapon', 'Freedom-Sworn', '5', 'weapon', 'freedom-sworn.png'),
('weapon', 'Haran Geppaku Futsu', '5', 'weapon', 'haran_geppaku_futsu.png'),
('weapon', 'Key of Khaj-Nisut', '5', 'weapon', 'key_of_khaj_nisut.png'),
('weapon', 'Light of Foliar Incision', '5', 'weapon', 'light_of_foliar_incision.png'),
('weapon', 'Mistsplitter Reforged', '5', 'weapon', 'mistsplitter_reforged.png'),
('weapon', 'Peak Patrol Song', '5', 'weapon', 'peak_patrol_song.png'),
('weapon', 'Primordial Jade Cutter', '5', 'weapon', 'primordial_jade_cutter.png'),
('weapon', 'Splendor of Tranquil Waters', '5', 'weapon', 'splendor_of_tranquil_waters.png'),
('weapon', 'Summit Shaper', '5', 'weapon', 'summit_shaper.png'),
('weapon', 'Uraku Misugiri', '5', 'weapon', 'uraku_misugiri.png'),

('weapon', 'Skyward Blade', '5', 'all', 'skyward_blade.png'),
('weapon', 'Aquila Favonia', '5', 'all', 'aquila_favonia.png');

-- Banners_Units
INSERT INTO rng_archive.banners_units (banner_id, unit_id) VALUES
(1, 95),
(1, 76),
(1, 75),
(1, 94),
(2, 77),
(2, 76),
(2, 75),
(2, 94),
(3, 87),
(3, 7),
(3, 31),
(3, 58),
(4, 20),
(4, 7),
(4, 31),
(4, 58),
(5, 97),
(5, 57),
(5, 88),
(5, 96),
(6, 74),
(6, 57),
(6, 88),
(6, 96),
(7, 86),
(7, 67),
(7, 37),
(7, 40),
(8, 38),
(8, 67),
(8, 37),
(8, 40),
(9, 99),
(9, 22),
(9, 53),
(9, 98),
(10, 44),
(10, 22),
(10, 53),
(10, 98),
(11, 91),
(11, 94),
(11, 6),
(11, 60),
(12, 83),
(12, 94),
(12, 6),
(12, 60),
(13, 187),
(13, 177),
(13, 157),
(13, 127),
(13, 143),
(13, 152),
(13, 135),
(14, 216),
(14, 167),
(14, 156),
(14, 134),
(14, 139),
(14, 163),
(14, 150),
(15, 205),
(15, 196),
(15, 153),
(15, 129),
(15, 141),
(15, 146),
(15, 159),
(16, 192),
(16, 201),
(16, 133),
(16, 138),
(16, 147),
(16, 155),
(16, 162),
(17, 210),
(17, 199),
(17, 132),
(17, 135),
(17, 145),
(17, 156),
(17, 160),
(18, 190),
(18, 202),
(18, 131),
(18, 140),
(18, 148),
(18, 155),
(18, 164);