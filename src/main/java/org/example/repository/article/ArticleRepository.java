package org.example.repository.article;

import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.exceptions.EntityNotFoundException;

import java.util.List;

public interface ArticleRepository {
  ArticleId generateId();
  List<Article> findAll();
  long create(Article article);
  void delete(long id) throws EntityNotFoundException;
  void update(Article article) throws EntityNotFoundException;
  Article getById(long id) throws EntityNotFoundException;
}
