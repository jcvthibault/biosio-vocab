
source schema.sql;
-- source populate.sql;

FLUSH PRIVILEGES;
GRANT INSERT, SELECT ON TERMINOLOGY_DB.* TO 'ibiomes-db'@'%' identified by 'ibiomes';

