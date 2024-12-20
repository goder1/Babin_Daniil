package org.example.repository.comment;

import org.example.entities.Article.ArticleId;
import org.example.entities.Comment.Comment;
import org.example.entities.Comment.CommentId;
import org.example.exceptions.EntityNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class PostgresCommentRepository implements CommentRepository {

  private final Handle handle;
  private final Jdbi jdbi;

  public PostgresCommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
    this.handle = jdbi.open();
  }

  @Override
  public CommentId generateId() {
    return (CommentId) this.handle.createQuery("SELECT nextval('comment_id_seq') AS value")
            .mapToMap()
            .first()
            .get("value");
  }

  @Override
  public long create(Comment comment) {
    return jdbi.inTransaction((Handle handle) -> {
      long id = generateId().getId();
      handle.createQuery("SELECT id FROM article WHERE id = :article_id FOR UPDATE")
              .bind("article_id", comment.getArticleId())
              .mapToMap()
              .first();

      handle.createUpdate("INSERT INTO comment (id, text, article_id) VALUES (:id, :text, :article_id)")
              .bind("id", id)
              .bind("text", comment.getText())
              .bind("article_id", comment.getArticleId())
              .execute();

      handle.createUpdate(
                      "UPDATE article SET trending=(select count(id) from comment where article_id = :article_id) > 3 WHERE id = :article_id"
              )
              .bind("article_id", comment.getArticleId())
              .execute();

      return id;
    });
  }

  @Override
  public List<Comment> findAllByArticleId(long id) {
    return this.handle
            .createQuery("SELECT * FROM comment WHERE article_id = :article_id")
            .bind("article_id", id)
            .map(
                    (rs, ctx) ->
                            new Comment(
                                    new CommentId(rs.getLong("id")),
                                    new ArticleId(id),
                                    rs.getString("text")
                            )
            )
            .list();
  }

  @Override
  public void deleteByArticleId(long id) throws EntityNotFoundException {
    try {
      jdbi.inTransaction((Handle handle) -> {
        Comment comment = handle
                .createQuery("SELECT * FROM comment WHERE id = :id")
                .bind("id", id)
                .map(
                        (rs, ctx) ->
                                new Comment(
                                        new CommentId(rs.getLong("id")),
                                        new ArticleId(rs.getLong("article_id")),
                                        rs.getString("text")
                                )
                )
                .one();
        handle.createUpdate("DELETE FROM comment WHERE id = :id").bind("id", id).execute();

        handle.createUpdate(
                        "UPDATE article SET trending=(select count(id) from comment where article_id = :article_id) > 3 WHERE id = :article_id"
                )
                .bind("article_id", comment.getArticleId())
                .execute();
        return id;
      });
    } catch (IllegalStateException e) {
      throw new EntityNotFoundException("Cannot find comment by id=" + id);
    }
  }

  @Override
  public long getCountByArticleId(long id) {
    return (long) this.handle.createQuery("SELECT count(id) AS value FROM comment WHERE article_id = :article_id")
            .bind("article_id", id)
            .mapToMap()
            .first()
            .get("value");
  }
}