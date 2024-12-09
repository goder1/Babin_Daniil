package org.example.controller.comment.response;

import org.example.entities.Comment.Comment;

public class CommentResponse {
  private final long id;
  private final String text;

  public CommentResponse(Comment comment) {
    this.id = comment.getId().getId();
    this.text = comment.getText();
  }

  public long getId() {
    return id;
  }

  public String getText() {
    return text;
  }
}