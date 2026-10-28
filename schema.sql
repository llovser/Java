CREATE TABLE IF NOT EXISTS clothes (
                                       id            INTEGER AUTO_INCREMENT PRIMARY KEY,
                                       type          VARCHAR(20)    NOT NULL,
                                       brand         VARCHAR(100)   NOT NULL,
                                       clothes_type  VARCHAR(100)   NOT NULL,
                                       size          VARCHAR(5)     NOT NULL,
                                       color         VARCHAR(50)    NOT NULL,
                                       material      VARCHAR(100)   NOT NULL,
                                       price         NUMERIC(12, 2) NOT NULL,
                                       quantity      INTEGER        NOT NULL,
                                       length        INTEGER,
                                       fit           VARCHAR(20),
                                       sleeve        VARCHAR(20),
                                       neck_size     INTEGER,
                                       season        VARCHAR(20),
                                       hooded        BOOLEAN,
                                       occasion      VARCHAR(20)
);