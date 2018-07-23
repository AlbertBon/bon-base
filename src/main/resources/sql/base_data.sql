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

-- 正在导出表  wxmanage.menu 的数据：~4 rows (大约)
DELETE FROM `menu`;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`menu_id`, `gmt_create`, `gmt_modified`, `name`, `path`, `component`, `redirect`, `title`, `icon`, `hidden`, `always_show`, `data_path`, `parent`) VALUES
	(1, '2018-07-22 22:50:44', '2018-07-22 22:50:46', '系统管理', '/admin', '/layout/Layout', 'admin/user/list', '系统管理', 'fa fa-cogs', '', '', '1', 0),
	(2, '2018-07-22 22:50:44', '2018-07-22 22:50:44', '用户管理', 'user/list', '/admin/UserList', '', '用户管理', 'fa fa-users', '', '', '1/2', 1),
	(3, '2018-07-23 14:54:51', '2018-07-23 14:54:51', '角色管理', 'role/list', '/admin/RoleList', NULL, '角色管理', 'fa fa-user-circle-o', NULL, NULL, '1/3', 1),
	(4, '2018-07-23 14:58:58', '2018-07-23 14:58:58', '菜单管理', 'menu/list', '/admin/MenuList', NULL, '菜单管理', 'fa fa-bars', NULL, NULL, '1/4', 1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;

-- 正在导出表  wxmanage.permission 的数据：~4 rows (大约)
DELETE FROM `permission`;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` (`permission_id`, `gmt_create`, `gmt_modified`, `permission_flag`, `permission_name`, `type`, `object_id`) VALUES
	(1, '2018-06-06 11:19:08', '2018-06-06 11:19:08', 'sys:*', '【菜单】系统管理', '00', 1),
	(2, '2018-06-06 11:19:55', '2018-06-06 11:19:55', 'user:*', '【菜单】用户管理', '00', 2),
	(3, '2018-06-06 14:11:05', '2018-06-06 14:11:05', 'role:*', '【菜单】角色管理', '00', 3),
	(4, '2018-06-06 14:12:09', '2018-06-06 14:12:09', 'menu:*', '【菜单】菜单管理', '00', 4);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;

-- 正在导出表  wxmanage.role 的数据：~2 rows (大约)
DELETE FROM `role`;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `gmt_create`, `gmt_modified`, `role_name`, `role_flag`) VALUES
	(1, '2018-06-06 11:16:44', '2018-06-12 17:40:44', '系统管理员', 'admin'),
	(3, '2018-06-12 17:37:45', '2018-06-12 17:41:58', '角色1', 'js1');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 正在导出表  wxmanage.role_permission 的数据：~7 rows (大约)
DELETE FROM `role_permission`;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` (`role_permission_id`, `gmt_create`, `gmt_modified`, `permission_id`, `role_id`) VALUES
	(17, '2018-06-12 17:40:44', '2018-06-12 17:40:44', 2, 1),
	(18, '2018-06-12 17:40:46', '2018-06-12 17:40:46', 1, 1),
	(19, '2018-06-12 17:40:46', '2018-06-12 17:40:46', 3, 1),
	(20, '2018-06-12 17:40:47', '2018-06-12 17:40:47', 4, 1),
	(21, '2018-06-12 17:41:58', '2018-06-12 17:41:58', 2, 3),
	(22, '2018-06-12 17:41:58', '2018-06-12 17:41:58', 1, 3),
	(23, '2018-06-12 17:41:58', '2018-06-12 17:41:58', 3, 3);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;

-- 正在导出表  wxmanage.sys_base 的数据：~0 rows (大约)
DELETE FROM `sys_base`;
/*!40000 ALTER TABLE `sys_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_base` ENABLE KEYS */;

-- 正在导出表  wxmanage.user 的数据：~2 rows (大约)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `gmt_create`, `gmt_modified`, `name`, `phone`, `email`, `telephone`, `address`, `username`, `password`, `remark`, `wx_openid`, `app_id`, `secret_key`) VALUES
	(2, '2018-06-06 11:02:58', '2018-06-06 11:17:03', 'string', '13211112222', 'string', 'string', 'string', 'bon', '312fd2f3cabafc9417c35fd00efdaa5d', 'string', 'string', 'string', 'string'),
	(3, '2018-06-12 17:07:27', '2018-06-12 17:49:31', 'bon1', '', NULL, NULL, NULL, 'bon1', '312fd2f3cabafc9417c35fd00efdaa5d', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 正在导出表  wxmanage.user_role 的数据：~3 rows (大约)
DELETE FROM `user_role`;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_role_id`, `gmt_create`, `gmt_modified`, `user_id`, `role_id`) VALUES
	(1, '2018-06-06 11:17:03', '2018-06-06 11:17:03', 2, 1),
	(4, '2018-06-12 17:49:31', '2018-06-12 17:49:31', 3, 3),
	(5, '2018-06-12 17:49:31', '2018-06-12 17:49:31', 3, 1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
