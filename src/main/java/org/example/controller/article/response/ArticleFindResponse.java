package org.example.controller.article.response;

import org.example.controller.comment.response.CommentResponse;
import org.example.entities.Article.Article;

import java.util.List;
import java.util.Set;

public class ArticleFindResponse {
  private long id;
  private String name;
  private Set<String> tags;
  private List<CommentResponse> comments;

  public ArticleFindResponse(Article article, List<CommentResponse> comments) {
    this.id = article.getId().getId();
    this.name = article.getName();
    this.tags = article.getTags();
    this.comments = comments;
  }

  public ArticleFindResponse() {
    super();
  }

  public ArticleFindResponse(long id, String name, Set<String> tags, List<CommentResponse> comments) {
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.comments = comments;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<String> getTags() {
    return tags;
  }

  public List<CommentResponse> getComments() {
    return comments;
  }
}