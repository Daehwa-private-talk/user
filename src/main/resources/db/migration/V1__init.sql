-- user
CREATE TABLE `user`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `password`   VARCHAR(100) NOT NULL,
    `name`       VARCHAR(100) NOT NULL,
    `nickname`   VARCHAR(100) NOT NULL,
    `is_enabled` TINYINT      NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;
