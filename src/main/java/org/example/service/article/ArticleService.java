package org.example.service.article;

import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.exceptions.ArticleDeleteException;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.ArticleUpdateException;
import org.example.exceptions.EntityNotFoundException;
import org.example.repository.article.ArticleRepository;
import org.example.repository.comment.CommentRepository;

import java.util.List;
import java.util.Set;

public class ArticleService {
  private final ArticleRepository articles;
  private final CommentRepository comments;

  public ArticleService(ArticleRepository articles, CommentRepository comments) {
    this.articles = articles;
    this.comments = comments;
  }

  public List<Article> findAll() {
    return articles.findAll();
  }

  public Article findById(long id) throws ArticleNotFoundException {
    try {
      return articles.getById(id);
    } catch (EntityNotFoundException e) {
      throw new ArticleNotFoundException(e.getMessage(), e);
    }
  }

  public long create(String name, Set<String> tags) {
    Article article = new Article(name, tags);
    return articles.create(article);
  }

  public void delete(long id) throws ArticleDeleteException {
    try {
      articles.getById(id);
    } catch (EntityNotFoundException e) {
      throw new ArticleDeleteException(e.getMessage(), e);
    }
  }

  public void deleteCommentsByArticleId(long id) throws ArticleDeleteException {
    comments.deleteByArticleId(id);
  }

  public void update(long articleId, String name, Set<String> tags) throws ArticleUpdateException {
    Article article;
    try {
      article = articles.getById(articleId);
    } catch (EntityNotFoundException e) {
      throw new ArticleUpdateException(e.getMessage(), e);
    }

    try {
      articles.update(article.withName(name).withTags(tags));
    } catch (EntityNotFoundException e) {
      throw new ArticleUpdateException(e.getMessage(), e);
    }
  }

  public void createMultiple(List<Article> articleList) {
    articles.createMultiple(articleList);
  }
}