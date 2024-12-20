CREATE TABLE if NOT EXISTS comment
(
    id serial PRIMARY KEY,
    article_id INTEGER NOT NULL CONSTRAINT comment_article_id_fk REFERENCES article ON DELETE CASCADE,
    text VARCHAR NOT NULL
);