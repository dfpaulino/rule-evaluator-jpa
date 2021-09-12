
    create table action (
       id bigint not null auto_increment,
        create_time datetime,
        update_time datetime,
        data varchar(255) not null,
        priority integer not null,
        type varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table composite_to_predicate (
       composite_id bigint not null,
        predicate_id bigint not null,
        primary key (composite_id, predicate_id)
    ) engine=InnoDB;

    create table group_composite_to_group_composite (
       parent_id bigint not null,
        child_id bigint not null,
        primary key (parent_id, child_id)
    ) engine=InnoDB;

    create table group_composite (
       id bigint not null auto_increment,
        create_time datetime,
        update_time datetime,
        logical_operation varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table predicate_leaf (
       id bigint not null auto_increment,
        create_time datetime,
        update_time datetime,
        operation varchar(255),
        tag varchar(255),
        type varchar(255),
        value varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table rule (
       id bigint not null auto_increment,
        create_time datetime,
        update_time datetime,
        active bit not null,
        filter varchar(255),
        name varchar(255),
        priority integer not null,
        group_composite_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table rule_to_actions (
       rule_id bigint not null,
        action_id bigint not null
    ) engine=InnoDB;

    alter table composite_to_predicate
       add constraint UK_b8uchif7a7su7cuu2ap1hslky unique (predicate_id);

    alter table group_composite_to_group_composite
       add constraint UK_cfegofbbifl1git4b6c64jw9d unique (child_id);

    alter table rule_to_actions
       add constraint UK_lndxm0dwqr4dmfw4m155o7edt unique (action_id);

    alter table composite_to_predicate
       add constraint FK7ijnsoj46rndsjskblax3haph
       foreign key (predicate_id)
       references predicate_leaf (id);

    alter table composite_to_predicate
       add constraint FK5yfs0tqg1m6seypgahaag5ldu
       foreign key (composite_id)
       references group_composite (id);

    alter table group_composite_to_group_composite
       add constraint FKkw4l1hbhr9ppe6v03b39xeon1
       foreign key (child_id)
       references group_composite (id);

    alter table group_composite_to_group_composite
       add constraint FK8wrgj57n8xrd2cifl6bibpfrh
       foreign key (parent_id)
       references group_composite (id);

    alter table rule
       add constraint FKhbgqnw7497qm7n8haqpbhoguj
       foreign key (group_composite_id)
       references group_composite (id);

    alter table rule_to_actions
       add constraint FK9hddxn93n2csrod2q2ljspc77
       foreign key (action_id)
       references action (id);

    alter table rule_to_actions 
       add constraint FK6lndy25c7smca1jrwsk3t0up9 
       foreign key (rule_id) 
       references rule (id)
