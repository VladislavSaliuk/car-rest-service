create sequence public.car_id_seq
    start with 1 increment by 1;

create sequence public.car_model_id_seq
    start with 1 increment by 1;

create sequence public.category_id_seq
    start with 1 increment by 1;

create sequence public.manufacturer_id_seq
    start with 1 increment by 1;

create table public.car_models (
    car_model_id bigint DEFAULT nextval('public.car_model_id_seq') not null ,
    car_model_name varchar(255) unique,
    primary key (car_model_id)
);

create table public.categories (
    category_id bigint DEFAULT nextval('public.category_id_seq') not null ,
    category_name varchar(255) unique,
    primary key (category_id)
);

create table public.manufacturers (
     manufacturer_id bigint DEFAULT nextval('public.manufacturer_id_seq') not null ,
     manufacturer_name varchar(255) unique,
     primary key (manufacturer_id)
);

create table public.cars (
    car_id bigint DEFAULT nextval('public.car_id_seq') not null ,
    manufacturer_id bigint,
    manufacture_year integer,
    car_model_id bigint unique,
    category_id bigint,
    primary key (car_id),
    constraint Fk_cars_car_model_id foreign key (car_model_id)
    references public.car_models,
    constraint Fk_cars_category_id foreign key (category_id)
    references public.categories,
    constraint Fk_cars_manufacturer_id foreign key (manufacturer_id)
    references public.manufacturers
);