package org.example.service.comment;

import org.example.entities.Article.ArticleId;
import org.example.entities.Comment.Comment;
import org.example.exceptions.CommentCreateException;
import org.example.exceptions.CommentDeleteException;
import org.example.exceptions.EntityNotFoundException;
import org.example.repository.article.ArticleRepository;
import org.example.repository.comment.CommentRepository;

import java.util.List;

public class CommentService {
  private final ArticleRepository articles;
  private final CommentRepository comments;

  public CommentService(ArticleRepository articles, CommentRepository comments) {
    this.articles = articles;
    this.comments = comments;
  }

  public long create(long articleId, String name) throws CommentCreateException {
    try {
      articles.getById(articleId);
    } catch (EntityNotFoundException e) {
      throw new CommentCreateException(e.getMessage(), e);
    }
    Comment comment = new Comment(new ArticleId(articleId), name);
    return comments.create(comment);
  }

  public void delete(long id) throws CommentDeleteException {
    try {
      comments.deleteByArticleId(id);
    } catch (EntityNotFoundException e) {
      throw new CommentDeleteException(e.getMessage(), e);
    }
  }

  public List<Comment> findAllByArticleId(long articleId) {
    return comments.findAllByArticleId(articleId);
  }

  public Long getCountByArticleId(long articleId) {
    return comments.getCountByArticleId(articleId);
  }
}