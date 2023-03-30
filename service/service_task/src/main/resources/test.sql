create table if not exists employee(
                                       id int,
                                       name string,
                                       age int,
                                       dept_id int
)
    partitioned by (dept_id int)
    row format delimited fields terminated by '\t'
    stored as textfile;

-- 添加一个技术部的分区
alter table employee add partition (dept_id=1) location '/user/hive/warehouse/employee/dept_id=1';