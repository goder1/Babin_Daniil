package org.example.entities.Comment;

import org.example.entities.Article.ArticleId;

public class Comment {
  private final CommentId id;
  private final ArticleId articleId;
  String text;

  public Comment(CommentId id, ArticleId articleId, String text) {
    this.id = id;
    this.articleId = articleId;
    this.text = text;
  }

  public Comment(ArticleId articleId, String text) {
    this.id = null;
    this.articleId = articleId;
    this.text = text;
  }

  public CommentId getId() {
    return id;
  }

  public ArticleId getArticleId() {
    return articleId;
  }

  public String getText() {
    return text;
  }

  public Comment setId(CommentId id) {
    return new Comment(id, articleId, text);
  }
}