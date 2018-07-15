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

-- 导出  表 sys_base 结构
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库基础表，包含所有数据库信息';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
