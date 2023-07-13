-- user
DROP TABLE IF EXISTS `daehwa_user`;

CREATE TABLE IF NOT EXISTS `daehwa_user`
(
    `id`                        INT          NOT NULL AUTO_INCREMENT,
    `email`                     VARCHAR(255) NOT NULL,
    `password`                  VARCHAR(100) NOT NULL,
    `name`                      VARCHAR(100) NOT NULL,
    `nickname`                  VARCHAR(100) NOT NULL,
    `refresh_token`             VARCHAR(100) NULL,
    `refresh_token_expired_at`  DATETIME     NULL,
    `sign_in_at`                DATETIME     NULL,
    `role`                      VARCHAR(20)  NOT NULL,
    `is_enabled`                TINYINT      NOT NULL,
    `is_deleted`                TINYINT      NOT NULL,
    `created_at`                DATETIME     NOT NULL,
    `updated_at`                DATETIME     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY uq_refresh_token (refresh_token)
) ENGINE = InnoDB;
