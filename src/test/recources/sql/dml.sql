--assume user first register so all the time was same at the inital time
INSERT INTO `t_u_auths_users`VALUES(246912,'123456','Eric','2024-06-17 23:59:59','2024-06-17 23:59:59','2024-06-17 23:59:59','admin','active')
INSERT INTO `t_u_auths_users`VALUES(246802,'123401','Bob','2024-06-17 23:59:59','2024-06-17 23:59:59','2024-06-17 23:59:59','user','active')
INSERT INTO `t_u_auths_users`VALUES(246804,'123402','Lisa','2024-06-17 23:59:59','2024-06-17 23:59:59','2024-06-17 23:59:59','user','dereg')
INSERT INTO `t_u_auths_users`VALUES(246806,'123403','Nelson','2024-06-17 23:59:59','2024-06-17 23:59:59','2024-06-17 23:59:59','admin','frozen')
INSERT INTO `t_u_auths_users`VALUES(246808,'123404','Frank','2024-06-17 23:59:59','2024-06-17 23:59:59','2024-06-17 23:59:59','user','forzen')

INSERT INTO `t_u_auths_grant_detials` VALUES(246802,'1202406180001','123401','Bob','12345','Eric','2024-06-18 23:59:59','2024-06-18 23:59:59','autha','active')
INSERT INTO `t_u_auths_grant_detials` VALUES(246802,'1202406180002','123401','Bob','12345','Eric','2024-06-18 23:59:59','2024-06-18 23:59:59','authb','forbidden')
INSERT INTO `t_u_auths_grant_detials` VALUES(246802,'1202406180003','123401','Bob','12345','Eric','2024-06-18 23:59:59','2024-06-18 23:59:59','authc','dereg')

INSERT INTO `t_u_auths_grant_detials` VALUES(246804,'1202406180004','123402','Lisa','12345','Eric','2024-06-18 23:59:59','2024-06-18 23:59:59','authb','active')
INSERT INTO `t_u_auths_grant_detials` VALUES(246804,'1202406180005','123402','Lisa','123403','Nelson','2024-06-18 23:59:59','2024-06-18 23:59:59','authc','dereg')

INSERT INTO `t_u_auths_grant_detials` VALUES(246808,'1202406180004','123404','Frank','12345','Eric','2024-06-18 23:59:59','2024-06-18 23:59:59','autha','active')
INSERT INTO `t_u_auths_grant_detials` VALUES(246808,'1202406180005','123404','Frank','123403','Nelson','2024-06-18 23:59:59','2024-06-18 23:59:59','authb','active')
INSERT INTO  `t_u_auths_grant_detials` VALUES ()
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