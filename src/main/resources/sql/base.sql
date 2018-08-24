-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.5.28 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 bon_base 的数据库结构
DROP DATABASE IF EXISTS `bon_base`;
CREATE DATABASE IF NOT EXISTS `bon_base` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bon_base`;
USE `bon_base`;

-- 导出  表 bon_base.sys_base 结构
CREATE TABLE IF NOT EXISTS `sys_base` (
  `base_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
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
  `modules` varchar(50) DEFAULT NULL COMMENT '模块名称',
  PRIMARY KEY (`base_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库基础表（包含所有数据库信息）';

-- 正在导出表  bon_base.sys_base 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `sys_base` DISABLE KEYS */;
INSERT INTO `sys_base` (`base_id`, `gmt_create`, `gmt_modified`, `table_name`, `table_remark`, `field_name`, `field_type`, `field_length`, `is_null`, `is_unique`, `is_unsigned`, `default_value`, `field_remark`, `is_id`, `modules`) VALUES
	(1, '2018-08-18 12:38:55', '2018-08-18 12:38:55', 'test', '123', 'test_id', 'BIGINT', 20, 0, 0, 0, 'ID', NULL, 1, 'app');
/*!40000 ALTER TABLE `sys_base` ENABLE KEYS */;

-- 导出  表 bon_base.sys_menu 结构
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(128) DEFAULT NULL COMMENT '菜单地址',
  `component` varchar(128) DEFAULT NULL COMMENT '视图文件路径',
  `redirect` varchar(128) DEFAULT NULL COMMENT '跳转地址（如果设置为noredirect会在面包屑导航中无连接）',
  `title` varchar(64) DEFAULT NULL COMMENT '菜单显示名称',
  `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
  `hidden` tinyint(4) DEFAULT 0 COMMENT '1:true,0:false;如果设置true，会在导航中隐藏',
  `always_show` tinyint(4) DEFAULT 0 COMMENT '1:true,0:false;如果设置true，没有子菜单也会显示在导航中',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- 正在导出表  bon_base.sys_menu 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`menu_id`, `gmt_create`, `gmt_modified`, `name`, `path`, `component`, `redirect`, `title`, `icon`, `hidden`, `always_show`) VALUES
	(1, '2018-07-22 22:50:44', '2018-07-22 22:50:46', '系统管理', '/admin', '/layout/Layout', 'admin/user/list', '系统管理', 'fa fa-cogs', 0, 0),
	(2, '2018-07-22 22:50:44', '2018-07-22 22:50:44', '用户管理', 'user/list', '/admin/UserList', NULL, '用户管理', 'fa fa-users', 0, 0),
	(3, '2018-07-23 14:54:51', '2018-07-23 14:54:51', '角色管理', 'role/list', '/admin/RoleList', NULL, '角色管理', 'fa fa-user-circle-o', 0, 0),
	(4, '2018-07-24 14:18:16', '2018-07-24 14:18:16', '系统表管理', 'sysTable', '/admin/SysTable', NULL, '系统表', 'fa fa-table', 0, 0),
	(5, '2018-08-06 20:14:20', '2018-08-06 20:14:20', '权限管理', 'permission', '/admin/Permission', NULL, '权限管理', 'fa fa-unlock-alt', 0, 0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- 导出  表 bon_base.sys_permission 结构
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `permission_flag` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `permission_name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `type` char(2) DEFAULT NULL COMMENT '00:菜单权限；01：接口url权限',
  `object_id` bigint(20) DEFAULT NULL COMMENT '对应表id（菜单权限即为菜单id）',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限id（permission表中的id值）',
  `data_path` varchar(512) DEFAULT NULL COMMENT '数据库id地址',
  `sort` int(11) DEFAULT 1 COMMENT '排序值',
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `permission_flag` (`permission_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- 正在导出表  bon_base.sys_permission 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` (`permission_id`, `gmt_create`, `gmt_modified`, `permission_flag`, `permission_name`, `type`, `object_id`, `parent_id`, `data_path`, `sort`) VALUES
	(1, '2018-06-06 11:19:08', '2018-07-24 12:00:47', 'menu:admin:main', '【菜单】系统管理', '00', 1, 0, '1', 1),
	(2, '2018-06-06 11:19:55', '2018-07-24 12:01:24', 'menu:admin:user', '【菜单】用户管理', '00', 2, 1, '1/2', 2),
	(3, '2018-06-06 14:11:05', '2018-07-24 12:01:30', 'menu:admin:role', '【菜单】角色管理', '00', 3, 1, '1/3', 3),
	(4, '2018-07-24 14:18:16', '2018-07-25 11:33:29', 'menu:admin:sysBase', '【菜单】系统表管理', '00', 4, 1, '1/4', 1),
	(5, '2018-08-06 20:14:20', '2018-08-06 20:14:20', 'menu:admin:permission', '【菜单】权限管理', '00', 5, 1, '1/5', 4);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;

-- 导出  表 bon_base.sys_role 结构
CREATE TABLE IF NOT EXISTS `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `role_flag` varchar(32) DEFAULT NULL COMMENT '角色标识',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_flag` (`role_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- 正在导出表  bon_base.sys_role 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`role_id`, `gmt_create`, `gmt_modified`, `role_name`, `role_flag`) VALUES
	(1, '2018-06-06 11:16:44', '2018-08-08 21:19:51', '系统管理员', 'admin'),
	(2, '2018-06-12 17:37:45', '2018-07-25 15:49:08', '角色1', 'js1');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- 导出  表 bon_base.sys_role_permission 结构
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `role_permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`role_permission_id`)
) ENGINE=InnoD DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- 正在导出表  bon_base.sys_role_permission 的数据：~14 rows (大约)
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` (`role_permission_id`, `gmt_create`, `gmt_modified`, `permission_id`, `role_id`) VALUES
	(1, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 1, 2),
	(2, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 2, 2),
	(3, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 3, 2),
	(4, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 4, 2),
	(5, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 1, 1),
	(6, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 2, 1),
	(7, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 3, 1),
	(8, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 4, 1),
	(9, '2018-08-08 21:19:51', '2018-08-08 21:19:51', 5, 1);
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;

-- 导出  表 bon_base.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `phone` char(11) DEFAULT NULL COMMENT '手机',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(16) DEFAULT NULL COMMENT '电话',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `username` varchar(64) DEFAULT NULL COMMENT '登录名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `wx_openid` varchar(32) DEFAULT NULL COMMENT '微信openid',
  `app_id` char(32) DEFAULT NULL COMMENT '应用id',
  `secret_key` char(64) DEFAULT NULL COMMENT '密钥',
  `salt` varchar(32) DEFAULT NULL COMMENT '密码盐',
  `is_admin` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是管理员',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 正在导出表  bon_base.sys_user 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`user_id`, `gmt_create`, `gmt_modified`, `name`, `phone`, `email`, `telephone`, `address`, `username`, `password`, `remark`, `wx_openid`, `app_id`, `secret_key`, `salt`, `is_admin`) VALUES
	(1, '2018-06-06 11:02:58', '2018-08-17 12:01:43', 'Albert', '13211112222', 'string', 'string', '天空之城', 'bon', '589b33c3fc225c1ead2038dd1e54b76b', 'string', 'string', 'string', 'string', '51a9179e10b148b7a01a67a55586ac65', 1),
	(2, '2018-06-12 17:07:27', '2018-08-17 13:00:46', 'bon1', '', NULL, NULL, NULL, 'bon1', '589b33c3fc225c1ead2038dd1e54b76b', NULL, NULL, NULL, NULL, '51a9179e10b148b7a01a67a55586ac65', 0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- 导出  表 bon_base.sys_user_role 结构
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色映射表';

-- 正在导出表  bon_base.sys_user_role 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`user_role_id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES
	(1, '2018-08-17 12:01:43', '2018-08-17 12:01:43', 1, 1),
	(2, '2018-08-17 12:01:43', '2018-08-17 12:01:43', 1, 2),
	(3, '2018-08-17 13:00:46', '2018-08-17 13:00:46', 2, 2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

-- 导出  表 bon_base.sys_url 结构
CREATE TABLE IF NOT EXISTS `sys_url` (
  `url_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `url_name` varchar(64) DEFAULT NULL COMMENT '路径名称',
  `url_path` varchar(128) DEFAULT NULL COMMENT '路径地址',
  `url_remark` varchar(255) DEFAULT NULL COMMENT '路径备注',
  PRIMARY KEY (`url_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统接口路径表';

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
