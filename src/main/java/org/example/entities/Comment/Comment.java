package org.example.entities.Comment;

import org.example.entities.Article.ArticleId;
import org.jetbrains.annotations.Nullable;

public class Comment {
  private final CommentId id;
  private final ArticleId articleId;
  String text;

  public Comment(@Nullable CommentId id, ArticleId articleId, String text) {
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