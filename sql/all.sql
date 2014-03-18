
source schema.sql;
-- source populate.sql;
source semtypes.sql;

FLUSH PRIVILEGES;
GRANT SELECT ON TERMINOLOGY_DB.* TO 'biosio-db'@'%' identified by 'biosio';

