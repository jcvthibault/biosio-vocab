
-- Add semantic types

insert into SEMANTIC_TYPE (TID, STR) values ("ROOT_ST", "Simulation feature");

insert into SEMANTIC_TYPE (TID, STR) values ("MTH_ALL", "Computational methods and parameters");
insert into SEMANTIC_TYPE (TID, STR) values ("MTH_MDY", "Molecular dynamics methods and parameters");
insert into SEMANTIC_TYPE (TID, STR) values ("MTH_QCH", "Quantum chemistry methods and parameters");

insert into SEMANTIC_TYPE (TID, STR) values ("MOL_ALL", "Molecular system feature");
insert into SEMANTIC_TYPE (TID, STR) values ("MOL_STR", "Structural feature of molecular system");
insert into SEMANTIC_TYPE (TID, STR) values ("MOL_BIO", "Biological feature of molecular system");
insert into SEMANTIC_TYPE (TID, STR) values ("MOL_CHE", "Chemical feature of molecular system");
insert into SEMANTIC_TYPE (TID, STR) values ("MOL_MTH", "Method-specific feature of molecular system");

insert into SEMANTIC_TYPE (TID, STR) values ("ENV_ALL", "Computing environment feature");
insert into SEMANTIC_TYPE (TID, STR) values ("ENV_HDW", "Hardware feature");
insert into SEMANTIC_TYPE (TID, STR) values ("ENV_SFW", "Software feature");

-- Add parent-child relationships between semantic types

insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("ROOT_ST", "MTH_ALL");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("ROOT_ST", "MOL_ALL");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("ROOT_ST", "ENV_ALL");

insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MTH_ALL", "MTH_MDY");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MTH_ALL", "MTH_QCH");

insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MOL_ALL", "MOL_MTH");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MOL_ALL", "MOL_STR");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MOL_ALL", "MOL_CHE");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("MOL_ALL", "MOL_BIO");

insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("ENV_ALL", "ENV_HDW");
insert into SEMANTIC_TYPE_PARENT_REL (TID_PARENT, TID_CHILD) values ("ENV_ALL", "ENV_SFW");


