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
-- 正在导出表  wxmanage.menu 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`menu_id`, `gmt_create`, `gmt_modified`, `name`, `path`, `component`, `redirect`, `title`, `icon`, `hidden`, `always_show`, `data_path`, `parent`) VALUES
	(1, '2018-07-22 22:50:44', '2018-07-22 22:50:46', '系统管理', '/admin', '/layout/Layout', 'admin/user/list', '系统管理', 'fa fa-cogs', '', '', '1', 0),
	(2, '2018-07-22 22:50:44', '2018-07-22 22:50:44', '用户管理', 'user/list', '/admin/UserList', '', '用户管理', 'fa fa-users', '', '', '1/2', 1),
	(3, '2018-07-23 14:54:51', '2018-07-23 14:54:51', '角色管理', 'role/list', '/admin/RoleList', NULL, '角色管理', 'fa fa-user-circle-o', NULL, NULL, '1/3', 1),
	(4, '2018-07-23 14:58:58', '2018-07-23 14:58:58', '菜单管理', 'menu/list', '/admin/MenuList', NULL, '菜单管理', 'fa fa-bars', NULL, NULL, '1/4', 1),
	(5, '2018-07-24 14:18:16', '2018-07-24 14:18:16', '系统表管理', 'sysTable', '/admin/SysTable', NULL, '系统表', 'fa fa-table', NULL, NULL, '1/5', 1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;

-- 正在导出表  wxmanage.permission 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` (`permission_id`, `gmt_create`, `gmt_modified`, `permission_flag`, `permission_name`, `type`, `object_id`, `object_parent`) VALUES
	(1, '2018-06-06 11:19:08', '2018-07-24 12:00:47', 'sys:menu', '【菜单】系统管理', '00', 1, 0),
	(2, '2018-06-06 11:19:55', '2018-07-24 12:01:24', 'userList:menu', '【菜单】用户管理', '00', 2, 1),
	(3, '2018-06-06 14:11:05', '2018-07-24 12:01:30', 'roleList:menu', '【菜单】角色管理', '00', 3, 1),
	(4, '2018-06-06 14:12:09', '2018-07-24 12:01:38', 'menuList:menu', '【菜单】菜单管理', '00', 4, 1),
	(5, '2018-07-24 14:18:16', '2018-07-25 11:33:29', 'sysTable:menu', '【菜单】系统表管理', '00', 5, 1);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;

-- 正在导出表  wxmanage.role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `gmt_create`, `gmt_modified`, `role_name`, `role_flag`) VALUES
	(1, '2018-06-06 11:16:44', '2018-07-25 16:15:21', '系统管理员', 'admin'),
	(3, '2018-06-12 17:37:45', '2018-07-25 15:49:08', '角色1', 'js1');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 正在导出表  wxmanage.role_permission 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` (`role_permission_id`, `gmt_create`, `gmt_modified`, `permission_id`, `role_id`) VALUES
	(64, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 1, 3),
	(65, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 2, 3),
	(66, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 3, 3),
	(67, '2018-07-25 15:49:08', '2018-07-25 15:49:08', 5, 3),
	(72, '2018-07-25 16:15:21', '2018-07-25 16:15:21', 1, 1),
	(73, '2018-07-25 16:15:21', '2018-07-25 16:15:21', 2, 1),
	(74, '2018-07-25 16:15:21', '2018-07-25 16:15:21', 3, 1),
	(75, '2018-07-25 16:15:21', '2018-07-25 16:15:21', 4, 1),
	(76, '2018-07-25 16:15:21', '2018-07-25 16:15:21', 5, 1);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;

-- 正在导出表  wxmanage.sys_base 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `sys_base` DISABLE KEYS */;
INSERT INTO `sys_base` (`sys_base_id`, `gmt_create`, `gmt_modified`, `table_name`, `table_remark`, `field_name`, `field_type`, `field_length`, `is_null`, `is_unique`, `is_unsigned`, `default_value`, `field_remark`, `is_id`) VALUES
	(1, '2018-07-24 15:46:16', '2018-07-24 15:46:16', 'test', '123', '23', 'BIGINT', 123, 1, 0, 0, NULL, NULL, 1);
/*!40000 ALTER TABLE `sys_base` ENABLE KEYS */;

-- 正在导出表  wxmanage.user 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `gmt_create`, `gmt_modified`, `name`, `phone`, `email`, `telephone`, `address`, `username`, `password`, `remark`, `wx_openid`, `app_id`, `secret_key`) VALUES
	(2, '2018-06-06 11:02:58', '2018-06-06 11:17:03', 'string', '13211112222', 'string', 'string', 'string', 'bon', '312fd2f3cabafc9417c35fd00efdaa5d', 'string', 'string', 'string', 'string'),
	(3, '2018-06-12 17:07:27', '2018-07-24 16:30:36', 'bon1', '', NULL, NULL, NULL, 'bon1', '312fd2f3cabafc9417c35fd00efdaa5d', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 正在导出表  wxmanage.user_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_role_id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES
	(1, '2018-06-06 11:17:03', '2018-06-06 11:17:03', 2, 1),
	(6, '2018-07-24 16:30:37', '2018-07-24 16:30:37', 3, 3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
