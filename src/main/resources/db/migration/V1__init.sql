-- user
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT,
    `email`                    VARCHAR(255) NOT NULL,
    `password`                 VARCHAR(100) NOT NULL,
    `name`                     VARCHAR(100) NOT NULL,
    `nickname`                 VARCHAR(100) NOT NULL,
    `nonce`                    VARCHAR(100) NULL,
    `refresh_token_expired_at` DATETIME     NULL,
    `is_enabled`               TINYINT      NOT NULL,
    `is_deleted`               TINYINT      NOT NULL,
    `created_at`               DATETIME     NOT NULL,
    `updated_at`               DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;
