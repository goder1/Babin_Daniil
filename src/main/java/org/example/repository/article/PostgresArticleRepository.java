package org.example.repository.article;

import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.exceptions.EntityNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;


public class PostgresArticleRepository implements ArticleRepository {
  private final Handle handle;
  private final Jdbi jdbi;

  @Override
  public ArticleId generateId() {
    return (ArticleId) this.handle.createQuery("SELECT nextval('article_id_seq') AS value")
            .mapToMap()
            .first()
            .get("value");
  }

  public PostgresArticleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
    this.handle = jdbi.open();
  }

  @Override
  public long create(Article article) {
    long id = generateId().getId();
    this.handle.createUpdate("INSERT INTO article (id, name, tags) VALUES (:id, :name, :tags)")
            .bind("id", id)
            .bind("name", article.getName())
            .bind("tags", String.join(",", article.getTags()))
            .execute();
    return id;
  }

  @Override
  public Article getById(long id) throws EntityNotFoundException {
    try {
      return this.handle
              .createQuery("SELECT * FROM article WHERE id = :id")
              .bind("id", id)
              .map(
                      (rs, ctx) ->
                              new Article(
                                      new ArticleId(rs.getLong("id")),
                                      rs.getString("name"),
                                      rs.getString("tags")
                              )
              )
              .one();
    } catch (IllegalStateException e) {
      throw new EntityNotFoundException("Cannot find article by id=" + id);
    }
  }

  @Override
  public List<Article> findAll() {
    return this.handle
            .createQuery("SELECT * FROM article")
            .map(
                    (rs, ctx) ->
                            new Article(
                                    new ArticleId(rs.getLong("id")),
                                    rs.getString("name"),
                                    rs.getString("tags")
                            )
            )
            .list();
  }

  @Override
  public void delete(long id) throws EntityNotFoundException {
    int count = this.handle.createUpdate("DELETE FROM article WHERE id = :id").bind("id", id).execute();

    if (count == 0) {
      throw new EntityNotFoundException("Cannot find article by id=" + id);
    }
  }

  @Override
  public void update(Article article) throws EntityNotFoundException {
    int count = this.handle.createUpdate("UPDATE article SET name=:name, tags=:tags WHERE id = :id")
            .bind("id", article.getId())
            .bind("name", article.getName())
            .bind("tags", String.join(",", article.getTags()))
            .execute();

    if (count == 0) {
      throw new EntityNotFoundException("Cannot find article by id=" + article.getId());
    }
  }

  @Override
  public void createMultiple(List<Article> articles) {
    jdbi.inTransaction((Handle handle) -> {
      for (Article article : articles) {
        handle.createUpdate(
                        "INSERT INTO article (name, tags) VALUES (:name, :tags)")
                .bind("name", article.getName())
                .bind("tags", String.join(",", article.getTags()))
                .execute();
      }
      return null;
    });
  }
}