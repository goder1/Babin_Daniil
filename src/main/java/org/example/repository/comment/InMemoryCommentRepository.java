package org.example.repository.comment;

import org.example.entities.Comment.Comment;
import org.example.entities.Comment.CommentId;
import org.example.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentRepository implements CommentRepository {
  private final AtomicLong nextId = new AtomicLong(0);
  Map<Long, Comment> data = new ConcurrentHashMap<>();

  @Override
  public CommentId generateId() {
    return new CommentId(nextId.incrementAndGet());
  }

  @Override
  public synchronized long create(Comment comment) {
    comment = comment.setId(new CommentId(this.nextId.incrementAndGet()));
    data.put(this.nextId.longValue(), comment);
    return this.nextId.longValue();
  }

  @Override
  public List<Comment> findAllByArticleId(long articleId) {
    List<Comment> list = new ArrayList<>();
    for (Map.Entry<Long, Comment> entry : data.entrySet()) {
      Comment comment = entry.getValue();
      if (comment.getArticleId().getId() == articleId) {
        list.add(comment);
      }
    }

    return list;
  }

  @Override
  public void delete(long id) throws EntityNotFoundException {
    if (data.containsKey(id)) {
      data.remove(id);
    } else {
      throw new EntityNotFoundException("Comment with id:" + id + " doesn't exist or cannot be reached");
    }
  }

  @Override
  public void deleteByArticleId(long articleId) {
    for (Map.Entry<Long, Comment> entry : data.entrySet()) {
      Long key = entry.getKey();
      Comment comment = entry.getValue();
      if (comment.getArticleId().getId() == articleId) {
        data.remove(key);
      }
    }
  }

  @Override
  public long getCountByArticleId(long articleId) {
    long result = 0;
    for (Map.Entry<Long, Comment> entry : data.entrySet()) {
      Comment comment = entry.getValue();
      if (comment.getArticleId().getId() == articleId) {
        result++;
      }
    }

    return result;
  }
}