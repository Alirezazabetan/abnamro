#!/bin/bash
psql -v ON_ERROR_STOP=1 --username "recipe" -d "recipedb"  <<-EOSQL
     create schema if not exists abnamro;
     create table abnamro.tt (
                     id int8 not null,
                      current_price numeric(21, 2),
                      last_update date,
                      created_date date,
                      name varchar(255),
                      primary key (id)
                  );
          create table abnamro.recipe
          (
              id bigint NOT NULL,
              instruction character varying(255) COLLATE pg_catalog."default",
              servings integer,
              title character varying(255) COLLATE pg_catalog."default",
              user_id character varying(255) COLLATE pg_catalog."default",
              dish_type character varying(255) COLLATE pg_catalog."default",
              CONSTRAINT recipe_pkey PRIMARY KEY (id)
          );
     create table abnamro.tbl_ingredient
     (
         id bigint NOT NULL,
         ingredient character varying(255) COLLATE pg_catalog."default" NOT NULL,
         CONSTRAINT fk3c4ic3ipm4epgid4bq1gf1kbn FOREIGN KEY (id)
             REFERENCES abnamro.recipe (id) MATCH SIMPLE
             ON UPDATE NO ACTION
             ON DELETE NO ACTION
     );

     INSERT INTO abnamro.tt(
                 id,current_price, last_update, created_date, name)
     SELECT '2','20',null,null,'faceAli'
     WHERE
         NOT EXISTS (SELECT id FROM abnamro.tt WHERE id = 2);
     INSERT INTO abnamro.tt(
                 id,current_price, last_update,created_date, name)
     SELECT '1','5.5',null,null,'apple'
     WHERE
         NOT EXISTS (SELECT id FROM abnamro.tt WHERE id = 1);
EOSQL