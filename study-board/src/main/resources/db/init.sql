-- 수업자료 정리 게시판의 MySQL 초기화 스크립트.

-- 1. 기존 테이블 삭제
-- 외래 키로 다른 테이블을 참조하는 테이블부터 생성의 역순으로 지운다.
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS study_posts;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS members;

-- 2. 테이블 생성
CREATE TABLE members
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nickname      VARCHAR(50)  NOT NULL,
    joined_at     DATETIME(6)  NOT NULL,
    withdrawn_at  DATETIME(6)  NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_members_email UNIQUE (email)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE categories
(
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE study_posts
(
    id            BIGINT        NOT NULL AUTO_INCREMENT,
    author_id     BIGINT        NOT NULL,
    category_id   BIGINT        NOT NULL,
    title         VARCHAR(200)  NOT NULL,
    body          TEXT          NOT NULL,
    reference_url VARCHAR(2048) NULL,
    created_at    DATETIME(6)   NOT NULL,
    updated_at    DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_study_posts_author FOREIGN KEY (author_id) REFERENCES members (id) ON DELETE RESTRICT,
    CONSTRAINT fk_study_posts_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT,
    CONSTRAINT ck_study_posts_body_length CHECK (CHAR_LENGTH(body) BETWEEN 1 AND 12000),
    INDEX ix_study_posts_author (author_id),
    INDEX ix_study_posts_created (created_at, id),
    INDEX ix_study_posts_category (category_id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE comments
(
    id            BIGINT        NOT NULL AUTO_INCREMENT,
    study_post_id BIGINT        NOT NULL,
    author_id     BIGINT        NOT NULL,
    body          VARCHAR(1000) NOT NULL,
    created_at    DATETIME(6)   NOT NULL,
    updated_at    DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comments_study_post FOREIGN KEY (study_post_id) REFERENCES study_posts (id) ON DELETE RESTRICT,
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES members (id) ON DELETE RESTRICT,
    INDEX ix_comments_author (author_id),
    INDEX ix_comments_post_created (study_post_id, created_at, id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- 3. 기본 데이터: 수업 분류
INSERT INTO categories (id, name)
VALUES (1, 'Spring Boot'),
       (2, 'JPA'),
       (3, 'Spring Data JPA');