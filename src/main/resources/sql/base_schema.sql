
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` ( 
  `user_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`user_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `name`  varchar(32)    COMMENT ''姓名'',
  `phone`  char(11)    COMMENT ''手机'',
  `email`  varchar(128)    COMMENT ''邮箱'',
  `telephone`  varchar(16)    COMMENT ''电话'',
  `address`  varchar(64)    COMMENT ''地址'',
  `username`  varchar(64)    COMMENT ''登录名'',
  `password`  varchar(64)    COMMENT ''密码'',
  `remark`  varchar(255)    COMMENT ''备注'',
  `wx_openid`  varchar(32)    COMMENT ''微信openid'',
  `app_id`  char(32)    COMMENT ''应用id'',
  `secret_key`  char(64)    COMMENT ''密钥'',
  `salt`  varchar(32)    COMMENT ''密码盐'',
  `is_admin`  tinyint   DEFAULT ''0''  COMMENT ''是否是管理员''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''用户表'';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` ( 
  `role_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`role_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `role_name`  varchar(32)    COMMENT ''角色名称'',
  `role_flag`  varchar(32)    COMMENT ''角色标识''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''角色表'';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` ( 
  `user_role_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`user_role_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `user_id`  bigint    COMMENT ''用户id'',
  `role_id`  bigint    COMMENT ''角色id''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''用户角色映射表'';

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` ( 
  `permission_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`permission_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `permission_flag`  varchar(50)    COMMENT ''权限标识'',
  `permission_name`  varchar(32)    COMMENT ''权限名称'',
  `type`  char(2)    COMMENT ''00:菜单权限；01：接口url权限'',
  `object_id`  bigint    COMMENT ''对应表id（菜单权限即为菜单id）'',
  `parent_id`  bigint    COMMENT ''父权限id（permission表中的id值）'',
  `data_path`  varchar(512)    COMMENT ''数据库id地址'',
  `sort`  int   DEFAULT ''999''  COMMENT ''排序值''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''权限表'';

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` ( 
  `role_permission_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`role_permission_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `permission_id`  bigint    COMMENT ''权限id'',
  `role_id`  bigint    COMMENT ''角色id''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''角色权限表'';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` ( 
  `menu_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`menu_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `name`  varchar(64)    COMMENT ''菜单名称'',
  `path`  varchar(128)    COMMENT ''菜单地址'',
  `component`  varchar(128)    COMMENT ''视图文件路径'',
  `redirect`  varchar(128)    COMMENT ''跳转地址（如果设置为noredirect会在面包屑导航中无连接）'',
  `title`  varchar(64)    COMMENT ''菜单显示名称'',
  `icon`  varchar(64)    COMMENT ''菜单图标'',
  `hidden`  tinyint(2)    COMMENT ''1:true,0:false如果设置true，会在导航中隐藏'',
  `always_show`  tinyint(2)    COMMENT ''1:true,0:false没有子菜单也会显示在导航中''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''菜单表'';

DROP TABLE IF EXISTS `sys_base`;
CREATE TABLE `sys_base` ( 
  `base_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`base_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `table_name`  varchar(64)    COMMENT ''表名'',
  `table_remark`  varchar(128)    COMMENT ''表备注'',
  `field_name`  varchar(64)    COMMENT ''字段名'',
  `field_type`  varchar(32)    COMMENT ''字段类型'',
  `field_length`  int    COMMENT ''字段长度'',
  `is_null`  tinyint    COMMENT ''1:是，0：否；是否可以为空'',
  `is_unique`  tinyint    COMMENT ''1:是，0：否；是否唯一'',
  `is_unsigned`  tinyint    COMMENT ''1:是，0：否；是否为无符号'',
  `default_value`  varchar(128)    COMMENT ''默认值'',
  `field_remark`  varchar(255)    COMMENT ''字段备注'',
  `is_id`  tinyint    COMMENT ''1:是，0：否；是否为id'',
  `modules`  varchar(50)    COMMENT ''模块名称''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''数据库基础表，包含所有数据库信息'';

DROP TABLE IF EXISTS `sys_url`;
CREATE TABLE `sys_url` ( 
  `url_id`  bigint NOT NULL AUTO_INCREMENT COMMENT ''ID'',PRIMARY KEY (`url_id`),
  `gmt_create`  datetime   COMMENT ''创建时间'',
  `gmt_modified`  datetime   COMMENT ''最后一次更新时间'',
  `url_name`  varchar(64)    COMMENT ''路径名称'',
  `url_path`  varchar(128)    COMMENT ''路径地址'',
  `url_remark`  varchar(255)    COMMENT ''路径备注''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment=''系统接口路径表'';

