-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.5.28 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 wxmanage 的数据库结构
CREATE DATABASE IF NOT EXISTS `wxmanage` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `wxmanage`;


-- 导出  表 wxmanage.menu 结构
CREATE TABLE IF NOT EXISTS `menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(128) DEFAULT NULL COMMENT '菜单地址',
  `component` varchar(128) DEFAULT NULL COMMENT '视图文件路径',
  `redirect` varchar(128) DEFAULT NULL COMMENT '跳转地址（如果设置为noredirect会在面包屑导航中无连接）',
  `title` varchar(64) DEFAULT NULL COMMENT '菜单显示名称',
  `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
  `hidden` char(2) DEFAULT NULL COMMENT '00:true,01:false如果设置true，会在导航中隐藏',
  `always_show` char(2) DEFAULT NULL COMMENT '00:true,01:false没有子菜单也会显示在导航中',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- 正在导出表  wxmanage.menu 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`menu_id`, `gmt_create`, `gmt_modified`, `name`, `path`, `component`, `redirect`, `title`, `icon`, `hidden`, `always_show`) VALUES
	(1, '2018-07-22 22:50:44', '2018-07-22 22:50:46', '系统管理', '/admin', '/layout/Layout', 'admin/user/list', '系统管理', 'fa fa-cogs', '', ''),
	(2, '2018-07-22 22:50:44', '2018-07-22 22:50:44', '用户管理', 'user/list', '/admin/UserList', '', '用户管理', 'fa fa-users', '', ''),
	(3, '2018-07-23 14:54:51', '2018-07-23 14:54:51', '角色管理', 'role/list', '/admin/RoleList', NULL, '角色管理', 'fa fa-user-circle-o', NULL, NULL),
	(4, '2018-07-24 14:18:16', '2018-07-24 14:18:16', '系统表管理', 'sysTable', '/admin/SysTable', NULL, '系统表', 'fa fa-table', NULL, NULL),
	(5, '2018-08-06 20:14:20', '2018-08-06 20:14:20', '权限管理', 'permission', '/admin/Permission', NULL, '权限管理', 'fa fa-unlock-alt', NULL, NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;


-- 导出  表 wxmanage.permission 结构
CREATE TABLE IF NOT EXISTS `permission` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `permission_flag` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `permission_name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `type` char(2) DEFAULT NULL COMMENT '00:菜单权限',
  `object_id` bigint(20) DEFAULT NULL COMMENT '对应表id（菜单权限即为菜单id）',
  `object_parent` bigint(20) DEFAULT NULL COMMENT '对应表id的父id（菜单权限即为菜单id的父id）',
  `data_path` varchar(512) DEFAULT NULL COMMENT '数据库id地址',
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `permission_flag` (`permission_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- 正在导出表  wxmanage.permission 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` (`permission_id`, `gmt_create`, `gmt_modified`, `permission_flag`, `permission_name`, `type`, `object_id`, `object_parent`, `data_path`) VALUES
	(1, '2018-06-06 11:19:08', '2018-07-24 12:00:47', 'sys:menu', '【菜单】系统管理', '00', 1, 0, '1'),
	(2, '2018-06-06 11:19:55', '2018-07-24 12:01:24', 'userList:menu', '【菜单】用户管理', '00', 2, 1, '1/2'),
	(3, '2018-06-06 14:11:05', '2018-07-24 12:01:30', 'roleList:menu', '【菜单】角色管理', '00', 3, 1, '1/3'),
	(4, '2018-07-24 14:18:16', '2018-07-25 11:33:29', 'sysTable:menu', '【菜单】系统表管理', '00', 4, 1, '1/4'),
	(5, '2018-08-06 20:14:20', '2018-08-06 20:14:20', 'permission:menu', '【菜单】权限管理', '00', 5, 1, '1/5');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;


-- 导出  表 wxmanage.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `role_flag` varchar(32) DEFAULT NULL COMMENT '角色标识',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_flag` (`role_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 正在导出表  wxmanage.role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `gmt_create`, `gmt_modified`, `role_name`, `role_flag`) VALUES
	(1, '2018-06-06 11:16:44', '2018-08-08 21:19:51', '系统管理员', 'admin'),
	(2, '2018-06-12 17:37:45', '2018-07-25 15:49:08', '角色1', 'js1');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;


-- 导出  表 wxmanage.role_permission 结构
CREATE TABLE IF NOT EXISTS `role_permission` (
  `role_permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- 正在导出表  wxmanage.role_permission 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` (`role_permission_id`, `gmt_create`, `gmt_modified`, `permission_id`, `role_id`) VALUES
	(1, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 1, 2),
	(2, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 2, 2),
	(3, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 3, 2),
	(4, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 4, 2),
	(5, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 1, 1),
	(6, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 2, 1),
	(7, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 3, 1),
	(8, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 4, 1),
	(9, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 5, 1);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;


-- 导出  表 wxmanage.sys_base 结构
CREATE TABLE IF NOT EXISTS `sys_base` (
  `sys_base_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `table_name` varchar(64) DEFAULT NULL COMMENT '表名',
  `table_remark` varchar(128) DEFAULT NULL COMMENT '表备注',
  `field_name` varchar(64) DEFAULT NULL COMMENT '字段名',
  `field_type` varchar(32) DEFAULT NULL COMMENT '字段类型',
  `field_length` int(11) DEFAULT NULL COMMENT '字段长度',
  `is_null` tinyint(4) DEFAULT NULL COMMENT '1:是，0：否；是否可以为空',
  `is_unique` tinyint(4) DEFAULT NULL COMMENT '1:是，0：否；是否唯一',
  `is_unsigned` tinyint(4) DEFAULT NULL COMMENT '1:是，0：否；是否为无符号',
  `default_value` varchar(128) DEFAULT NULL COMMENT '默认值',
  `field_remark` varchar(255) DEFAULT NULL COMMENT '字段备注',
  `is_id` tinyint(4) DEFAULT NULL COMMENT '1:是，0：否；是否为id',
  PRIMARY KEY (`sys_base_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='数据库基础表，包含所有数据库信息';

-- 正在导出表  wxmanage.sys_base 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `sys_base` DISABLE KEYS */;
INSERT INTO `sys_base` (`sys_base_id`, `gmt_create`, `gmt_modified`, `table_name`, `table_remark`, `field_name`, `field_type`, `field_length`, `is_null`, `is_unique`, `is_unsigned`, `default_value`, `field_remark`, `is_id`) VALUES
	(1, '2018-07-24 15:46:16', '2018-07-24 15:46:16', 'test', '123', '23', 'BIGINT', 123, 1, 0, 0, NULL, NULL, 1);
/*!40000 ALTER TABLE `sys_base` ENABLE KEYS */;


-- 导出  表 wxmanage.test 结构
CREATE TABLE IF NOT EXISTS `test` (
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `23` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  PRIMARY KEY (`23`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='123';

-- 正在导出表  wxmanage.test 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;


-- 导出  表 wxmanage.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `phone` char(11) DEFAULT NULL COMMENT '手机',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(16) DEFAULT NULL COMMENT '电话',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `username` varchar(64) NOT NULL COMMENT '登录名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `wx_openid` varchar(32) DEFAULT NULL COMMENT '微信openid',
  `app_id` char(32) DEFAULT NULL COMMENT '应用id',
  `secret_key` char(64) DEFAULT NULL COMMENT '密钥',
  `salt` varchar(32) NOT NULL COMMENT '密码盐',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 正在导出表  wxmanage.user 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `gmt_create`, `gmt_modified`, `name`, `phone`, `email`, `telephone`, `address`, `username`, `password`, `remark`, `wx_openid`, `app_id`, `secret_key`, `salt`) VALUES
	(1, '2018-06-06 11:02:58', '2018-08-16 16:20:10', 'Albert', '15814371650', 'string', 'string', '天空之城', 'bon', '589b33c3fc225c1ead2038dd1e54b76b', 'string', 'string', 'string', 'string', '51a9179e10b148b7a01a67a55586ac65'),
	(2, '2018-06-12 17:07:27', '2018-07-24 16:30:36', 'bon1', '', NULL, NULL, NULL, 'bon1', '589b33c3fc225c1ead2038dd1e54b76b', NULL, NULL, NULL, NULL, '51a9179e10b148b7a01a67a55586ac65');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;


-- 导出  表 wxmanage.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户角色映射表';

-- 正在导出表  wxmanage.user_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_role_id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES
	(9, '2018-08-16 16:20:10', '2018-08-16 16:20:10', 1, 1),
	(10, '2018-08-16 16:20:10', '2018-08-16 16:20:10', 1, 2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
