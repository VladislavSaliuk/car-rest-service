
truncate table public.cars restart identity cascade;
truncate table public.car_models restart identity cascade;
truncate table public.categories restart identity cascade;
truncate table public.manufacturers restart identity cascade;

alter sequence public.car_id_seq restart with 1;
alter sequence public.car_model_id_seq restart with 1;
alter sequence public.category_id_seq restart with 1;
alter sequence public.manufacturer_id_seq restart with 1;
