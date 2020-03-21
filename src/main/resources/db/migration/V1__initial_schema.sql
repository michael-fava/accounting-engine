create table "account" (
    "id" uuid not null,
    "balance" decimal(19,2),
    "beneficiary_name" varchar(255),
    "created_date" timestamp,
    "currency" varchar(255),
    "last_modified_date" timestamp,
    "pin_number" varchar(4),
    "account_type" varchar(255),
    primary key ("id"));

create table "transaction_entry" (
    "id" uuid not null,
    "amount" decimal(19,2),
    "created_date" timestamp,
    "currency" varchar(255),
    "destination_identifier" varchar(255),
    "destination_type" varchar(255),
    "comment" varchar(255),
    "last_modified_date" timestamp,
    "source_identifier" varchar(255),
    "source_type" varchar(255),
    "transaction_status" varchar(255),
    "transaction_type" varchar(255),
    primary key ("id"));
