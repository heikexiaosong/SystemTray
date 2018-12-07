create table motor_routine_test
(
  ID             int not null
    primary key,
  assembly_order int,
  production     varchar(32),
  notes          varchar(1000)
)
go

create table bearing_kit
(
  id          bigint not null
    primary key,
  identifier  varchar(32),
  bearing_de  varchar(32),
  bearing_nde varchar(32),
  color       varchar(32),
  notes       varchar(8000),
  color_code  int
)
go

create table bearing_kit_production
(
  id              bigint not null
    primary key,
  production      varchar(32),
  shaft_extention varchar(32),
  bearing_de      varchar(32),
  bearing_nde     varchar(32),
  bearing_kit_id  bigint,
  notes           varchar(8000)
)
go
