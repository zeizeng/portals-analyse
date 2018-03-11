
create table tieba(
    uuid char(32) not null,
    tieba_name varchar(50) not null,
    crawl_date datetime not null,
    super_category varchar(50) not null,
    low_category varchar(50) not null,
    url varchar(100) not null,
    focus int,
    post_total int,
    post_superior int,
    pic_num int,
    groups int,
    group_member int,
    primary key(uuid))ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

    