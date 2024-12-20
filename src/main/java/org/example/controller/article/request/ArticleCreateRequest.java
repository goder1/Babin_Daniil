package org.example.controller.article.request;

import java.util.Set;

public record ArticleCreateRequest(String name, Set<String> tags) {}