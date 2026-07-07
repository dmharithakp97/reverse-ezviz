-- db_audit.sql — READ-ONLY database audit for the myRewards e-Bill CRM (MySQL/MariaDB).
--
-- AUTHORIZED USE ONLY — your own database. Every statement below is read-only:
-- information_schema metadata + SHOW commands + a couple of harmless SELECT equality probes.
-- Nothing here writes, alters, or drops. Run it, capture the output, hand it back to Claude.
--
--   mysql -h <host> -u <user> -p <database> < db_audit.sql > db_audit_out.txt 2>&1
--
-- It confirms the [V] items in ../DATABASE_ANALYSIS.md: column types, PII inventory, secret-at-rest,
-- schema/code drift, index coverage, and the DB user's privilege scope (shared-CRM blast radius).

SELECT '=== 0. server + current user ===' AS section;
SELECT VERSION() AS mysql_version, CURRENT_USER() AS effective_user, DATABASE() AS current_db;

SELECT '=== 1. DB-1: is contact_no a string or a number? (type juggling risk) ===' AS section;
SELECT COLUMN_NAME, COLUMN_TYPE, DATA_TYPE, IS_NULLABLE, COLUMN_KEY, CHARACTER_MAXIMUM_LENGTH
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'CRM_Customer'
ORDER BY ORDINAL_POSITION;
-- If contact_no is VARCHAR/CHAR/TEXT, the app must compare with a STRING, not an int.
-- Demonstrate the juggling hazard (read-only, harmless):
SELECT '0772152986' = 772152986      AS leading_zero_collides,      -- expect 1 (TRUE) => hazard
       '772152986abc' = 772152986    AS trailing_junk_collides;     -- expect 1 (TRUE) => hazard

SELECT '=== 2. DB-3: does merchant_id exist ANYWHERE? what is the real scoping column? ===' AS section;
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND (COLUMN_NAME = 'merchant_id'
       OR COLUMN_NAME LIKE '%merchant%'
       OR COLUMN_NAME LIKE '%store%'
       OR COLUMN_NAME LIKE '%chart%'      -- candidate rename seen in related errors
       OR COLUMN_NAME LIKE '%branch%')
ORDER BY TABLE_NAME, COLUMN_NAME;

SELECT '=== 3. DB-2: verification_code storage (reversible vs hashed) ===' AS section;
SELECT COLUMN_NAME, COLUMN_TYPE, CHARACTER_MAXIMUM_LENGTH
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'CRM_Customer'
  AND COLUMN_NAME IN ('verification_code','has_subscribed');
-- A Laravel encrypt() blob is a long base64 string (typically > 200 chars). A bcrypt hash is 60 chars.
-- Length + a sample of the format tells us which. (Sample is your own data; treat as sensitive.)

SELECT '=== 4. DB-6: PII inventory sitting behind select * ===' AS section;
SELECT COLUMN_NAME, DATA_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'CRM_Customer'
  AND (COLUMN_NAME REGEXP 'name|nic|id_?no|address|email|phone|mobile|contact|dob|birth|gender|account|meter')
ORDER BY ORDINAL_POSITION;

SELECT '=== 5. DB-1/perf: indexes on CRM_Customer (is the (merchant/contact) lookup indexed?) ===' AS section;
SELECT INDEX_NAME, SEQ_IN_INDEX, COLUMN_NAME, NON_UNIQUE
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'CRM_Customer'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;
-- No index on the lookup columns => every subscribe attempt full-scans the CRM = DoS amplifier.

SELECT '=== 6. DB-4: shared-CRM blast radius — how many CRM_* tables can this user see? ===' AS section;
SELECT COUNT(*) AS crm_prefixed_tables
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME LIKE 'CRM\_%';
SELECT TABLE_NAME, TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME LIKE 'CRM\_%'
ORDER BY TABLE_NAME;

SELECT '=== 7. DB-4: least-privilege check — what can the web app DB user actually DO? ===' AS section;
SHOW GRANTS FOR CURRENT_USER();
-- A public-facing web app should NOT have ALL PRIVILEGES / write access across the whole CRM schema.
-- Ideal: SELECT/UPDATE on the handful of tables/columns the e-bill flow touches, nothing more.

SELECT '=== 8. did Laravel migrations ever run against THIS database? (drift check) ===' AS section;
SELECT COUNT(*) AS has_migrations_table
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'migrations';
-- If 0, this DB was never managed by `php artisan migrate` here => explains DB-3 schema drift.

SELECT '=== done — send db_audit_out.txt back to Claude ===' AS section;
