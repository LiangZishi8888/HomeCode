# Project Documentation #
**************************
##  Configuration ##
***
+ DataSouce : H2db in-memory mode
+ SpringBootConfig : classpath:applicationContext.xml
+ MybatisConfig : classpath:mybatis-config.xml
+ Mappers : classpath:/mappers/.*
+ MockRequests : /src/test/mockRequest/mockReqs.json
+ EncryptRequestExample : /src/test/MockRequests/encryptMockReqs.json
+ DataSoucreInitScript :/sql/ddl.sql,/sql/dml.sql
+ UnitTestLocation :  com.demo.controller.AuthorityControllerTest(ALL)
+ EncryptAlorithum : SM4 symmetric
+ SysKeyLocation : Inside class com.demo.entity.crypto.CipherUtils
+ DecryptAnnotaion : DecryptRequest(Annotation)
## DbDesign ##
***
1. t_u_auths_users   
   in fact id and name both can used as primary key.And it is global unique
   If the database sharding,all the tables relative to the user business should
   in the same database to make transcation control easier,thus us id will be perfect
   for the same id caculate the dbKey is same and in the same database for different
   tables.   
   If no sharding,then index is flexible.Query like where id=?,where id=?and name=? using primary key is enough.
   Query like where id=?,where name=? ,where id=? and name=? must create
    idx id,idx name,seperatly.
    
 ```aidl
  `dbSplitKey` INT(11) NOT NULL,
  `user_id` VARCHAR(25),
  `account_name` VARCHAR(40) NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `last_update_time` TIMESTAMP,
  `last_login_time` TIMESTAMP,
  `role` VARCHAR(5) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY(`user_id`)
```
1. column 
+ each user has only a unique key of user_id
+ dbSplitKey is for drds and calculate by user_id
+ create_time only insert when a new user registered
+ last_login_time will only effect when user pass check
+ status have enum value ACTIVE FROZEN DEREG
+ last_update_time is designed for some one who has auths to change user status
+ it is reasonable name should be unique  
   

2. SchemaRecordExample


2.t_u_auths_users   

|dbKey|uid|name|createTime|lastUpdate|lastLogin|role|status|   

|:--------------|
## StoryDesign ##
###