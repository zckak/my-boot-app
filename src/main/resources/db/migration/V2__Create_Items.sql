CREATE TABLE `items` (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  comment text DEFAULT NULL,
  image_uri text DEFAULT NULL,
  user_id int(10) unsigned NOT NULL,
  updated_at datetime DEFAULT NULL,
  created_at datetime DEFAULT NULL,
  PRIMARY KEY (id)
);