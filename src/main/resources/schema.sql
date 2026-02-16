CREATE TABLE IF NOT EXISTS blueprints (
  author VARCHAR(100) NOT NULL,
  name   VARCHAR(100) NOT NULL,
  PRIMARY KEY (author, name)
);

CREATE TABLE IF NOT EXISTS blueprint_points (
  id     BIGSERIAL PRIMARY KEY,
  author VARCHAR(100) NOT NULL,
  name   VARCHAR(100) NOT NULL,
  idx    INT NOT NULL,
  x      INT NOT NULL,
  y      INT NOT NULL,
  CONSTRAINT fk_bp
    FOREIGN KEY (author, name)
    REFERENCES blueprints(author, name)
    ON DELETE CASCADE,
  CONSTRAINT uq_point_idx UNIQUE (author, name, idx)
);
