CREATE TABLE test.dbo.bearing_kit
(
    id bigint PRIMARY KEY NOT NULL,
    identifier varchar(32),
    bearing_de varchar(32),
    bearing_nde varchar(32),
    color varchar(32),
    notes varchar(8000),
    color_code int
);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1001, 'DR63_IEC', '6203', null, 'Red', null, 1);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1002, 'DR63_DT71_DT80_RZ', '6303', null, 'Yellow', null, 2);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1003, 'DR71_IEC / DR80_RZ', '6204 / 6304', null, 'Blue', null, 3);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1004, 'DR80_IEC', '6205', null, 'Green', null, 4);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1005, 'DT90_100_RZ_IEC / DR90_100_RZ_IE', '6306', null, 'Red', null, 5);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1006, 'DRN90_RZ', '6305', null, 'Blue', null, 6);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1007, 'DRN90_IEC', '6305', null, 'Yellow', null, 7);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1008, 'DRN100_RZ_IEC', '6306', null, 'Green', null, 8);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1009, 'DV112_132S_RZ', '6307', null, 'Yellow', null, 9);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1010, 'DV112_132S_IEC', '6208', null, 'Red', null, 10);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1011, 'DR112_132_DRN112_132S_RZ_IEC', '6308', null, 'Green', null, 11);
INSERT INTO test.dbo.bearing_kit (id, identifier, bearing_de, bearing_nde, color, notes, color_code) VALUES (1012, 'DV132_160M_RZ_IEC', '6309', null, 'Blue', null, null);
CREATE TABLE test.dbo.bearing_kit_production
(
    id bigint PRIMARY KEY NOT NULL,
    production varchar(32),
    shaft_extention varchar(32),
    bearing_de varchar(32),
    bearing_nde varchar(32),
    bearing_kit_id bigint,
    notes varchar(8000)
);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1001, 'DR63S4', 'IEC11x23', null, null, 1001, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1002, 'DR63M4', 'IEC11x23', null, null, 1001, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1003, 'DR63L4', 'IEC11x23', null, null, 1001, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1004, 'DR63S4', 'RZ10', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1005, 'DR63M4', 'RZ10', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1006, 'DR63L4', 'RZ10', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1007, 'DRS71S4', 'RZ10', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1008, 'DRS71M4', 'RZ10', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1009, 'DRS71S4', 'RZ12', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1010, 'DRS71M4', 'RZ12', null, null, 1002, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1011, 'DRS71S4', 'IEC14x30', null, null, 1003, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1012, 'DRS71M4', 'IEC14x30', null, null, 1003, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1013, 'DRE80M4', 'RZ12', null, null, 1009, null);
INSERT INTO test.dbo.bearing_kit_production (id, production, shaft_extention, bearing_de, bearing_nde, bearing_kit_id, notes) VALUES (1014, 'DRE132S4', 'IEC28x60', null, null, 1005, null);
CREATE TABLE test.dbo.motor_routine_test
(
    ID int PRIMARY KEY NOT NULL,
    assembly_order int,
    production varchar(32),
    notes varchar(1000)
);
INSERT INTO test.dbo.motor_routine_test (ID, assembly_order, production, notes) VALUES (1, 4999424, 'DRE80M4BE1', '1   DRE80M4BE1/FG                 M1                                        12mm pinion spi               FG130 D160                                    0.75   S1          230/4003.05     0.79   230 55           50.0       delta/star                     143525.13081540.01.0001.13.50     147671631.75                                                                                                                          220-242D/380-420Y             220-266 AC                    eof');
INSERT INTO test.dbo.motor_routine_test (ID, assembly_order, production, notes) VALUES (2, 4999482, 'DRE80M4', '1   DRE80M4/FG                    M1                                        12mm pinion sha               FG130 D160                                    0.75   S1          230/4003.05     0.79       55           50.0       delta/star                     143525.13081542.01.0001.13.50     147308041.75     2.65     1.52     1745           0.75  0.76  220-242 delta / 380-420 star  254-277 delta / 440-480 star  50    60                                                                eof');
INSERT INTO test.dbo.motor_routine_test (ID, assembly_order, production, notes) VALUES (3, 50332942, 'DRE132S4', '1   DRE132S4/FF/ES7S/V            B5                                        28x60mm lg.                   FF215 D250                                    4      S1          220/38014.50    0.82       55           50.0       delta/star                     146025.76673879.01.0001X18.50             8.40                           36092714.01                                                                                                                                                            eof');