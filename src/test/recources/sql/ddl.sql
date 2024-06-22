CREATE TABLE  IF NOT EXISTS `t_u_auths_users`
(
  `dbSplitKey` INT(11) NOT NULL,
  `user_id` VARCHAR(25),
  `account_name` VARCHAR(40) NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `last_update_time` TIMESTAMP,
  `last_login_time` TIMESTAMP,
  `role` VARCHAR(5) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY(`user_id`)
);


-- table record details of auth grant
CREATE TABLE  IF NOT EXISTS `t_u_auths_grant_detials`
(
  `dbSplitKey` INT(11) NOT NULL,
  `auth_association_id` VARCHAR(25) ,
  `user_id` VARCHAR(25) NOT NULL,
  `user_name` VARCHAR(25) NOT NULL,
  `admin_user_id` VARCHAR(25) NOT NULL,
  `admin_user_name` VARCHAR(25) NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `last_modify_time` TIMESTAMP,
  `auth_category` VARCHAR(10) NOT NULL,
  `status` VARCHAR(10) NOT NULL,
   PRIMARY KEY(`auth_association_id`)
);
