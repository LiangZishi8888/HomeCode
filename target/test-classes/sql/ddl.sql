CREATE TABLE  IF NOT EXISTS `t_u_auths_users`
(
  `dbSplitKey` INT(11) PRIMARY KEY,
  `user_id` VARCHAR(25),
  `account_name` VARCHAR(40),
  `create_time` VARCHAR(20) NOT NULL,
  `last_update_time` VARCHAR(20),
  `last_login_time` VARCHAR(20),
  `role` VARCHAR(5),
  `status` VARCHAR(20)
);


-- table record details of auth grant
CREATE TABLE  IF NOT EXISTS `t_u_auths_grant_detials`
(
  `dbSplitKey` INT(11),
  `auth_association_id` VARCHAR(25) PRIMARY KEY,
  `user_id` VARCHAR(25),
  `user_name` VARCHAR(25),
  `admin_user_id` VARCHAR(25),
  `admin_user_name` VARCHAR(25),
  `create_time` VARCHAR(10),
  `last_modify_time` VARCHAR(10),
  `auth_category` VARCHAR(10),
  `status` VARCHAR(10)
);
