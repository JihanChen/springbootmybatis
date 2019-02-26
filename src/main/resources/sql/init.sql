-- 初始化脚本
CREATE TABLE `gen_test_demo` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                               `name` varchar(100) NOT NULL COMMENT '名称',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='gen_test_demo';