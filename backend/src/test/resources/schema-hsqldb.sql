SET DATABASE SQL SYNTAX MYS TRUE;

CREATE TABLE physical_accounts (
		id  bigint NOT NULL AUTO_INCREMENT ,
        name varchar(255) NOT NULL,
        description varchar(255) NULL,
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

CREATE TABLE account_audits (
id bigint NOT NULL AUTO_INCREMENT,
from_physical_account_id  bigint NULL,
from_virtual_account_id  bigint NULL,
to_physical_account_id  bigint NULL,
to_virtual_account_id  bigint NULL,
type smallint NOT NULL,
amount bigint NOT NULL,
description   varchar(255) NULL,
PRIMARY KEY (id),
FOREIGN KEY (from_physical_account_id) REFERENCES physical_accounts (id),
FOREIGN KEY (from_virtual_account_id) REFERENCES virtual_accounts (id),
FOREIGN KEY (to_physical_account_id) REFERENCES physical_accounts (id),
FOREIGN KEY (to_virtual_account_id) REFERENCES virtual_accounts (id)
)