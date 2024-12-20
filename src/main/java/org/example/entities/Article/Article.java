package org.example.entities.Article;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Article {
  @Nullable
  private final ArticleId id;
  private final String name;
  private final Set<String> tags;

  public Article(@Nullable ArticleId id, String name, Set<String> tags) {
    this.id = id;
    this.name = name;
    this.tags = tags;
  }

  public Article(String name, Set<String> tags) {
    this.id = null;
    this.name = name;
    this.tags = tags;
  }

  public Article(@Nullable ArticleId id, String name, String tags) {
    this.id = id;
    this.name = name;
    this.tags = Arrays.stream(tags.split(",")).collect(Collectors.toSet());
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<String> getTags() {
    return tags;
  }

  public Article setId(ArticleId id) {
    return new Article(id, name, tags);
  }

  public Article withName(String name) {
    return new Article(id, name, tags);
  }

  public Article withTags(Set<String> tags) {
    return new Article(id, name, tags);
  }
}