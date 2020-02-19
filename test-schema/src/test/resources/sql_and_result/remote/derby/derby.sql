## derby not support tinyint, use smallint instead

### schema type is file
CREATE SCHEMA csv
set current schema = csv
CREATE TABLE t1 (tiny_type smallint DEFAULT NULL,smallint_type smallint,int_type int,bigint_type bigint DEFAULT NULL,varchar_type varchar(255),float_type float DEFAULT NULL,double_type double DEFAULT NULL)
INSERT INTO t1 VALUES (1, 1, 2, 30, 'vn', 0.5, 4.8)
INSERT INTO t1 VALUES (NULL, 10, 6, 18, 'net', 1.3, 0.6)
INSERT INTO t1 VALUES (1, NULL, 2, 3, 'zh', 0.01, 0.6)
INSERT INTO t1 VALUES (5, 3, NULL, 21, 'cn', 1, 0)
INSERT INTO t1 VALUES (3, 1, 22, NULL, 'kr', 1.5, 4.3)
INSERT INTO t1 VALUES (2, 8, 6, 1, 'us', 2.2, 0.6)
INSERT INTO t1 VALUES (10, 1, 2, 3, NULL, 1.4, 0.6)
INSERT INTO t1 VALUES (1, 2, 7, 11, 'uk', NULL, 1.8)
INSERT INTO t1 VALUES (0, 1, -1, 2, 'fr', 5, NULL)

### schema type is file
CREATE SCHEMA json
set current schema = json
CREATE TABLE t1 (tiny_type smallint DEFAULT NULL,smallint_type smallint,int_type int,bigint_type bigint DEFAULT NULL,varchar_type varchar(255),float_type float DEFAULT NULL,double_type double DEFAULT NULL)
INSERT INTO t1 VALUES (1, 1, 2, 30, 'vn', 0.5, 4.8)
INSERT INTO t1 VALUES (NULL, 10, 6, 18, 'net', 1.3, 0.6)
INSERT INTO t1 VALUES (1, NULL, 2, 3, 'zh', 0.01, 0.6)
INSERT INTO t1 VALUES (5, 3, NULL, 21, 'cn', 1, 0)
INSERT INTO t1 VALUES (3, 1, 22, NULL, 'kr', 1.5, 4.3)
INSERT INTO t1 VALUES (2, 8, 6, 1, 'us', 2.2, 0.6)
INSERT INTO t1 VALUES (10, 1, 2, 3, NULL, 1.4, 0.6)
INSERT INTO t1 VALUES (1, 2, 7, 11, 'uk', NULL, 1.8)
INSERT INTO t1 VALUES (0, 1, -1, 2, 'fr', 5, NULL)

### scheme type is mysql
CREATE SCHEMA yuqi
set current schema = yuqi
CREATE TABLE t1 (tiny_type smallint DEFAULT NULL,smallint_type smallint,int_type int,bigint_type bigint DEFAULT NULL,varchar_type varchar(255),float_type float DEFAULT NULL,double_type double DEFAULT NULL)
INSERT INTO t1 VALUES (1, 1, 2, 30, 'vn', 0.5, 4.8)
INSERT INTO t1 VALUES (NULL, 10, 6, 18, 'net', 1.3, 0.6)
INSERT INTO t1 VALUES (1, NULL, 2, 3, 'zh', 0.01, 0.6)
INSERT INTO t1 VALUES (5, 3, NULL, 21, 'cn', 1, 0)
INSERT INTO t1 VALUES (3, 1, 22, NULL, 'kr', 1.5, 4.3)
INSERT INTO t1 VALUES (2, 8, 6, 1, 'us', 2.2, 0.6)
INSERT INTO t1 VALUES (10, 1, 2, 3, NULL, 1.4, 0.6)
INSERT INTO t1 VALUES (1, 2, 7, 11, 'uk', NULL, 1.8)
INSERT INTO t1 VALUES (0, 1, -1, 2, 'fr', 5, NULL)