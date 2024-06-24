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
   
 ```sql
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
  

2.t_u_auths_users   

​     DbKey is calculate by user_id,and its convinent to get from user_id.A users all grant details will in the same database

​     authAssocationId is used as primary key,becasue its generate rule is versionNo+DateStr+globalCounter.

​     eq: 120240612100008 counter will reset each day so it is global unique

​     If user dont have an auth,when grant apply will insert new record 

​     If user has previous record while status!=active this record will update instead of insert

```sql
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
```


## StoryDesign ##
1./role/access

   1.1 if user not exists should throw necessary expection msg.

​          user not exists means no userId record in db or user status is DEG.

```json
{
	"resultCode": "AUT0005",
	"resultDescription": "User_Not_Exist",
	"errorMsg": "123402"
}
```

   1.2 if user exists but status is frozen should return corresponding  msg.Status is frozen

​        the loginTime is same as lastLoginTime cause system did not record current login behaviour

```json
{
	"resultCode": "AUT0008",
	"resultDescription": "User_Not_Active",
	"accessResult": false,
	"lastLoginTime": "2024-06-17 23:59:59.000",
	"loginTime": "2024-06-17 23:59:59.000",
	"userStatus": "frozen"
}
```

 1.3 if a user try to access system with admin role while he just the role of user.

```json
{
	"resultCode": "AUT0003",
	"resultDescription": "User_Need_Admin_Permission",
	"errorMsg": "123401"
}
```

 1.4  should throw necessary ex when user id is not compitable with user name

​        if user pass userId is store in db while name is not same with db query record should return not exist

```json
{
	"resultCode": "AUT0005",
	"resultDescription": "User_Not_Exist",
	"errorMsg": "123401"
}
```

 1.5 success login

```json
{
	"resultCode": "000000",
	"resultDescription": "Success",
	"accessResult": true,
	"lastLoginTime": "2024-06-17 23:59:59.000",
	"loginTime": "2024-06-24 12:52:16.569",
	"userStatus": "active"
}
```

2 /admin/adduser

  1.1 we dont repeat to show the status check ex code with previous interface

​        successGrants means succesfully grant by current adminUser

​        this means the auth may insert a record or update a record

​        userPreviousHoldActive is  user grant auths that already hold and active.

```json

	"resultCode": "000000",
	"resultDescription": "Success",
	"adminUserId": "123456",
	"adminUserName": "Eric",
	"userId": "123406",
	"userName": "Peter",
	"successCount": 2,
	"successGrants": [{
		"authName": "authB",
		"associationNo": "1202406180007",
		"authStatus": "ACTIVE",
		"previousAdminUserId": "123403",
		"previousAdminUserName": "Nelson",
		"previousAuthStatus": "FORBIDDEN",
		"previousGrantDate": "2024-06-18 23:59:59.000",
		"grantTime": "2024-06-24 13:01:37.067"
	}, {
		"authName": "authC",
		"associationNo": "1202406241000",
		"authStatus": "ACTIVE",
		"grantTime": "2024-06-24 13:01:37.067"
	}],
	"userPreviousHoldActive": [{
		"authName": "authA",
		"associationNo": "1202406180006",
		"authStatus": "ACTIVE",
		"previousAdminUserId": "123456",
		"previousAdminUserName": "Eric",
		"previousAuthStatus": "ACTIVE",
		"previousGrantDate": "2024-06-18 23:59:59.000",
		"grantTime": "2024-06-18 23:59:59.000"
	}],
	"grantTime": "2024-06-24 13:01:37:067"
}
```



 1.2 here is another example

```json
{
	"resultCode": "000000",
	"resultDescription": "Success",
	"adminUserId": "123456",
	"adminUserName": "Eric",
	"userId": "123408",
	"userName": "Dick",
	"successCount": 1,
	"successGrants": [{
		"authName": "authB",
		"associationNo": "1202406180009",
		"authStatus": "ACTIVE",
		"previousAdminUserId": "123403",
		"previousAdminUserName": "Nelson",
		"previousAuthStatus": "FORBIDDEN",
		"previousGrantDate": "2024-06-18 23:59:59.000",
		"grantTime": "2024-06-24 13:09:20.351"
	}],
	"userPreviousHoldActive": [{
		"authName": "authA",
		"associationNo": "1202406180008",
		"authStatus": "ACTIVE",
		"previousAdminUserId": "123456",
		"previousAdminUserName": "Eric",
		"previousAuthStatus": "ACTIVE",
		"previousGrantDate": "2024-06-18 23:59:59.000",
		"grantTime": "2024-06-18 23:59:59.000"
	}],
	"grantTime": "2024-06-24 13:09:20:351"
}
```

 1.3 if the user try to apply auths that dont exists need return corret ex msg

```json
{
	"resultCode": "AUT0007",
	"resultDescription": "Auth_Not_Exists"
}
```

3 /query/user

1 if user exists and has the auth (include active,forbidden)

```json
{
	"resultCode": "000000",
	"resultDescription": "Success",
	"isExists": true,
	"authDetail": {
		"authName": "authB",
		"associationNo": "1202406180002",
		"authStatus": "FORBIDDEN",
		"previousAdminUserId": "123456",
		"previousAdminUserName": "Eric",
		"previousAuthStatus": "FORBIDDEN",
		"previousGrantDate": "2024-06-18 23:59:59.000",
		"grantTime": "2024-06-18 23:59:59.000"
	}
}
```

2 if user exits but auth has deg

```json
{
	"resultCode": "000000",
	"resultDescription": "Success",
	"isExists": false
}
```

3 if user not exists or auth is invalid or user dont hold this auth all indicates the result of not exists

```json
{
	"resultCode": "000000",
	"resultDescription": "Success",
	"isExists": false
}
```

