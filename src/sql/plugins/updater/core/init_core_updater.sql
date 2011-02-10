--
-- Dumping data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'UPDATER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('UPDATER_MANAGEMENT','updater.adminFeature.updates_management.name',1,'jsp/admin/plugins/updater/ManageUpdates.jsp','updater.adminFeature.updates_management.description',0,'updater',NULL,NULL,NULL,4);


--
-- Dumping data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'UPDATER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('UPDATER_MANAGEMENT',1);
