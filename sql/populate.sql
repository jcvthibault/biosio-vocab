-- ==================================================================
-- Author:  Julien Thibault, University of Utah
-- Created: 10/04/2013
-- Description: run this script to populate the terminology using
--              the CSV files.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-- ==================================================================

LOAD DATA LOCAL INFILE '/tmp/CONCEPT.csv' 
INTO TABLE CONCEPT 
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n';

LOAD DATA LOCAL INFILE '/tmp/SYNONYM.csv' 
INTO TABLE CONCEPT_TERM 
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' 
(CUI, LANG, STR, IS_PREF, IS_ABBR);

LOAD DATA LOCAL INFILE '/tmp/DESCRIPTION.csv' 
INTO TABLE DESCRIPTION 
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' 
(CUI, LANG, STR) ;

LOAD DATA LOCAL INFILE '/tmp/CITATION.csv' 
INTO TABLE CITATION
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' 
(ID,DOI,YEAR,SOURCE,STR);

LOAD DATA LOCAL INFILE '/tmp/CONCEPT_CITATION.csv' 
INTO TABLE CONCEPT_CITATION
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n';
