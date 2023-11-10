-- 关闭外键约束检查
set foreign_key_checks = 0;

INSERT INTO user (id, username, avatar, created_time, gender, state, organization_id, role_id) VALUES (1, 'admin', 'avatar.jpg', '2023-01-05 17:16:11.000000', 0, 0, 1, 1);
INSERT INTO user (id, username, avatar, created_time, gender, state, organization_id, role_id) VALUES (2, 'guest', 'avatar.jpg', '2023-01-05 17:16:11', 0, 0, 1, 4);

INSERT INTO user_credential (id, credential, identifier, identity_type, user_id) VALUES (1, 'a66abb5684c45962d887564f08346e8d', 'admin', 0, 1);
INSERT INTO user_credential (id, credential, identifier, identity_type, user_id) VALUES (2, 'fe4ceeb01d43a6c29d8f4fe93313c6c1', 'guest', 0, 2);

INSERT INTO organization (id, name, parent_ids, type, parent_id) VALUES (0, '根节点', '/', 0, null);
INSERT INTO organization (id, name, parent_ids, type, parent_id) VALUES (1, '管理员', '/1/', 0, 1);
INSERT INTO organization (id, name, parent_ids, type, parent_id) VALUES (2, 'VIP', '/1/', 0, 1);
INSERT INTO organization (id, name, parent_ids, type, parent_id) VALUES (3, '普通用户', '/1/', 0, 1);
INSERT INTO organization (id, name, parent_ids, type, parent_id) VALUES (4, '游客', '/1/', 0, 0);

INSERT INTO role (id, available, description, name) VALUES (1, true, '管理员可以对企业内的所有用户进行管理，请谨慎修改管理员权限', '管理员');
INSERT INTO role (id, available, description, name) VALUES (2, true, 'VIP用户', 'VIP');
INSERT INTO role (id, available, description, name) VALUES (3, true, '普通的用户', '普通用户');
INSERT INTO role (id, available, description, name) VALUES (4, false, '只能查看的角色', '游客');

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 4);


INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (1, null, '根节点', null, '*', null, null, null);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (2, 'Odometer', '主页', null, 'dashboard', 0, '/dashboard', 1);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (3, 'SetUp', '系统管理', null, 'sys', 0, '/sys', 1);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (4, 'User', '用户管理', null, 'user:view', 0, '/users', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (5, 'Tickets', '角色管理', null, 'role:view', 0, '/roles', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (6, 'Collection', '权限资源', null, 'resource:view', 0, '/resources', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (7, null, '查看用户', null, 'user:view', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (8, null, '新增用户', null, 'user:create', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (9, null, '修改用户', null, 'user:update', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (10, null, '删除用户', null, 'user:delete', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (11, null, '查看角色', null, 'role:view', 1, null, 5);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (12, null, '新增角色', null, 'role:create', 1, null, 5);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (13, null, '修改角色', null, 'role:update', 1, null, 5);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (14, null, '删除角色', null, 'role:delete', 1, null, 5);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (15, null, '查看资源', null, 'resource:view', 1, null, 6);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (16, null, '新增资源', null, 'resource:create', 1, null, 6);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (17, null, '修改资源', null, 'resource:update', 1, null, 6);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (18, null, '删除资源', null, 'resource:delete', 1, null, 6);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (19, null, '新增组织架构', null, 'organization:create', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (20, null, '修改组织架构', null, 'organization:update', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (21, null, '删除组织架构', null, 'organization:delete', 1, null, 4);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (22, 'Timer', '操作日志', null, 'log:view', 0, '/logs', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (23, null, '清空日志', null, 'log:clean', 1, null, 22);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (24, null, '查看日志', null, 'log:view', 1, null, 22);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (25, 'Files', '对象存储', null, 'storage:view', 0, '/storage', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (26, null, '查看对象存储', null, 'storage:view', 1, null, 25);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (27, null, '新增对象存储', null, 'storage:create', 1, null, 25);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (28, null, '更新对象存储', null, 'storage:update', 1, null, 25);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (29, null, '删除对象存储', null, 'storage:delete', 1, null, 25);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id) VALUES (30, null, '设置默认存储', null, 'storage:markAsDefault', 1, null, 25);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id)
VALUES (32, null, '内容权限', null, 'content:view', 0, '/content', 3);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id)
VALUES (33, null, '科技', null, 'content:tech:view', 1, null, 32);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id)
VALUES (34, null, '生活', null, 'content:life:view', 1, null, 32);
INSERT INTO resource (id, icon, name, parent_ids, permission, type, url, parent_id)
VALUES (35, null, '娱乐', null, 'content:entertainment:view', 1, null, 32);



INSERT INTO role_resource (role_id, resource_id) VALUES (1, 2);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 3);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 4);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 5);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 6);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 7);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 8);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 9);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 10);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 11);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 12);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 13);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 14);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 15);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 16);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 17);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 18);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 19);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 20);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 21);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 22);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 23);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 24);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 25);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 26);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 27);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 28);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 29);
INSERT INTO role_resource (role_id, resource_id) VALUES (1, 30);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 2);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 3);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 6);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 15);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 16);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 17);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 18);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 22);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 23);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 24);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 25);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 26);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 27);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 28);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 29);
INSERT INTO role_resource (role_id, resource_id) VALUES (2, 30);
INSERT INTO role_resource (role_id, resource_id) VALUES (3, 2);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 2);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 3);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 4);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 5);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 6);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 7);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 11);
INSERT INTO role_resource (role_id, resource_id) VALUES (4, 15);

INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '二次元', '二次元');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '萌系搞笑', '萌系搞笑');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '短片', '短片');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', 'MAD·AMV', 'MAD·AMV');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', 'MMD·3D', 'MMD·3D');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '新番', '新番');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '声优', '声优');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '手书', '手书');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '日剧', '日剧');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '国产动画', '国产动画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '完结动画', '完结动画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '连载动画', '连载动画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '资讯', '资讯');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '布袋戏', '布袋戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '剧场版', '剧场版');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '国产原创相关', '国产原创相关');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '布袋戏', '布袋戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '特摄', '特摄');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '国产电影', '国产电影');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '电视剧', '电视剧');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '国创', '国创');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '翻唱', '翻唱');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', 'VOCALOID·UTAU', 'VOCALOID·UTAU');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '演奏', '演奏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', 'OP/ED/OST', 'OP/ED/OST');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '宅舞', '宅舞');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '三次元舞蹈', '三次元舞蹈');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '舞蹈教程', '舞蹈教程');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '单机游戏', '单机游戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '电子竞技', '电子竞技');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '手机游戏', '手机游戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '网络游戏', '网络游戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '桌游棋牌', '桌游棋牌');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', 'GMV', 'GMV');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '科普', '科普');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '技术', '技术');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '演讲', '演讲');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '公开课', '公开课');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '航空航天', '航空航天');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '数码', '数码');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '汽车', '汽车');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '日常', '日常');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '搞笑', '搞笑');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '美食圈', '美食圈');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '动物圈', '动物圈');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '手工', '手工');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '绘画', '绘画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '鬼畜', '鬼畜');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '音MAD', '音MAD');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '人力', '人力');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '美妆', '美妆');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '服饰', '服饰');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '健身', '健身');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '广告', '广告');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '综艺', '综艺');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '明星', '明星');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '影视杂谈', '影视杂谈');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '影视剪辑', '影视剪辑');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '短片', '短片');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '预告', '预告');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '华语电影', '华语电影');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '欧美电影', '欧美电影');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '日本电影', '日本电影');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '其他国家电影', '其他国家电影');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '国产剧', '国产剧');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '海外剧', '海外剧');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '科学科普', '科学科普');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '社科人文', '社科人文');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '财经', '财经');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '校园学习', '校园学习');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '职业职场', '职业职场');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '法律', '法律');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '纪录片', '纪录片');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '影视相关', '影视相关');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '情感', '情感');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '教程', '教程');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '漫画', '漫画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '游戏', '游戏');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '动画', '动画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '绘画', '绘画');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '唱见', '唱见');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '聊天', '聊天');
INSERT INTO `label` (`available`, `description`, `name`) VALUES (b'1', '手游', '手游');

INSERT INTO storage_config (type, id, is_default, name, address, storage_path, access_key, bucket_name, endpoint, secret_key, create_time, create_user, storage_id) VALUES (0, 1, true, '本地', 'storage/fetch/', 'files', null, null, null, null, '2023-07-10 17:00:48.000000', 'admin', 'SsIPzgpd9rFgxJhe3yUxk');

-- 开启外键约束检查
set foreign_key_checks = 1;
