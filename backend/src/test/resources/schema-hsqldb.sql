SET DATABASE SQL SYNTAX MYS TRUE;

CREATE TABLE physical_accounts (
		id  bigint NOT NULL AUTO_INCREMENT ,
        name varchar(255) NOT NULL,
        description varchar(255) NULL,
        amount bigint,
        PRIMARY KEY (id)
    );
 
CREATE UNIQUE INDEX physical_accounts_idx_name ON physical_accounts(name);

CREATE TABLE virtual_accounts (
id  bigint NOT NULL AUTO_INCREMENT ,
name  varchar(255) NOT NULL ,
description  varchar(255) NULL,
PRIMARY KEY (id)
);
CREATE UNIQUE INDEX virtual_accounts_idx_name ON virtual_accounts(name);

CREATE TABLE virtual_accounts_in_physical_accounts (
physical_account_id  bigint NOT NULL,
virtual_account_id  bigint NOT NULL,
amount bigint default 0,
PRIMARY KEY (physical_account_id,virtual_account_id),
FOREIGN KEY (physical_account_id) REFERENCES physical_accounts (id),
FOREIGN KEY (virtual_account_id) REFERENCES virtual_accounts (id)
);   
