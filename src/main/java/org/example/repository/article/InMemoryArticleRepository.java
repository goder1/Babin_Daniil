package org.example.repository.article;

import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository {
  private final AtomicLong nextId = new AtomicLong(0);
  Map<Long, Article> data = new ConcurrentHashMap<>();

  @Override
  public ArticleId generateId() {
    return new ArticleId(nextId.incrementAndGet());
  }

  @Override
  public synchronized long create(Article article) {
    article = article.setId(new ArticleId(this.nextId.incrementAndGet()));
    data.put(this.nextId.longValue(), article);

    return this.nextId.longValue();
  }

  @Override
  public Article getById(long id) throws EntityNotFoundException {
    if (data.containsKey(id)) {
      return data.get(id);
    } else {
      throw new EntityNotFoundException("article with id:" + id + " doesn't exist or cannot be reached");
    }
  }

  @Override
  public List<Article> findAll() {
    List<Article> list = new ArrayList<>();
    for (Map.Entry<Long, Article> entry : data.entrySet()) {
      Article article = entry.getValue();
      list.add(article);
    }

    return list;
  }

  @Override
  public void delete(long id) throws EntityNotFoundException {
    if (data.containsKey(id)) {
      data.remove(id);
    } else {
      throw new EntityNotFoundException("article with id:" + id + " doesn't exist or cannot be reached");
    }
  }

  @Override
  public synchronized void update(Article article) throws EntityNotFoundException {
    if (!data.containsKey(article.getId().getId())) {
      throw new EntityNotFoundException("article with id:" + article.getId().getId() + " doesn't exist or cannot be reached");
    }

    data.put(article.getId().getId(), article);
  }
}