package org.example.repository.comment;

import org.example.entities.Comment.Comment;
import org.example.entities.Comment.CommentId;
import org.example.exceptions.EntityNotFoundException;

import java.util.List;

public interface CommentRepository {
  CommentId generateId();
  List<Comment> findAllByArticleId(long id);
  long create(Comment comment);
  void deleteByArticleId(long articleId);
  long getCountByArticleId(long id);
}