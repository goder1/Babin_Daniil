package org.example.controller.comment.request;

public record CommentCreateRequest(long articleId, String text) {}