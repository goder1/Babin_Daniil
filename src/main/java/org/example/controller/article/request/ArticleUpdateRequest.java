package org.example.controller.article.request;

import java.util.Set;

public record ArticleUpdateRequest(String name, Set<String> tags) {}