DROP TABLE IF EXISTS offers;
DROP SEQUENCE IF EXISTS id_sequence;

CREATE SEQUENCE id_sequence
START WITH 1
INCREMENT BY 1
MINVALUE 1;


CREATE TABLE `offers` (
  `id`int AUTO_INCREMENT  PRIMARY KEY,
  `created_dt` date NOT NULL DEFAULT CURDATE(),
  `currency` varchar(5) DEFAULT 'GBP',
  `description` varchar(200),
  `expiry_date` date DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `price` BIGINT NOT NULL,
  `product_id`int NOT NULL,
  `status` varchar(15) DEFAULT 'ACTIVE',
  `updated_dt` date NOT NULL DEFAULT CURDATE(),
  `version` int AUTO_INCREMENT NOT NULL
);


INSERT INTO `offers`(`id`, `created_dt`, `currency`, `description`, `name`, `price`, `product_id`, `status`, `updated_dt`, `version`,`expiry_date`)VALUES (next value for id_sequence, Curdate(), 'GBP', 'winter sale deals', 'winter wear', 10, 1, 'ACTIVE', Curdate(), 1,'2022-10-30'); 
INSERT INTO `offers`(`id`, `created_dt`, `currency`, `description`, `name`, `price`, `product_id`, `status`, `updated_dt`, `version`,`expiry_date`)VALUES (next value for id_sequence, Curdate(), 'GBP', 'Summer sale deals', 'summer wear', 20, 1, 'ACTIVE', Curdate(), 1,'2022-10-30'); 
INSERT INTO `offers`(`id`, `created_dt`, `currency`, `description`, `name`, `price`, `product_id`, `status`, `updated_dt`, `version`,`expiry_date`)VALUES (next value for id_sequence, Curdate(), 'GBP', 'autumn sale deals', 'autumn clothes',30,  1, 'ACTIVE', Curdate(), 1,'2022-10-30'); 
INSERT INTO `offers`(`id`, `created_dt`, `currency`, `description`, `name`, `price`, `product_id`, `status`, `updated_dt`, `version`,`expiry_date`)VALUES (next value for id_sequence, Curdate(), 'GBP', 'school open sale deals', 'school uniforms', 30, 1, 'ACTIVE', Curdate(), 1,'2022-10-30'); 
