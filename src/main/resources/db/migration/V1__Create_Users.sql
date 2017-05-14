CREATE TABLE users (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  icon_uri text DEFAULT NULL,
  updated_at datetime DEFAULT NULL,
  created_at datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);