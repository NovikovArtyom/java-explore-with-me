DROP TABLE IF EXISTS hit CASCADE;

create table if not exists hit (
    id bigint generated by default as identity not null,
    app varchar(255) not null,
    uri varchar(255) not null,
    ip varchar(255) not null,
    hit_timestamp timestamp without time zone not null,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);