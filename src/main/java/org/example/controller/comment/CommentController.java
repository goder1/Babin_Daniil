package org.example.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.Controller;
import org.example.controller.ErrorResponse;
import org.example.controller.comment.request.CommentCreateRequest;
import org.example.controller.comment.response.CommentCreateResponse;
import org.example.exceptions.CommentCreateException;
import org.example.exceptions.CommentDeleteException;
import org.example.service.comment.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

  private final Service service;
  private final ObjectMapper objectMapper;
  private final CommentService commentService;

  public CommentController(
          Service service, CommentService commentService, ObjectMapper objectMapper) {
    this.service = service;
    this.commentService = commentService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createComment();
    delete();
  }

  private void createComment() {
    service.post(
            "/api/comments",
            (Request request, Response response) -> {
              response.type("application/json");
              CommentCreateRequest commentCreateRequest =
                      objectMapper.readValue(request.body(), CommentCreateRequest.class);
              try {
                long commentId = commentService.create(commentCreateRequest.articleId(), commentCreateRequest.text());
                LOG.debug("Created comment with id: {}", commentId);
                response.status(201);
                return objectMapper.writeValueAsString(new CommentCreateResponse(commentId));
              } catch (CommentCreateException e) {
                LOG.warn("Article not found.", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              }
            }
    );
  }

  private void delete() {
    service.delete(
            "/api/comments/:id",
            (Request request, Response response) -> {
              response.type("application/json");
              try {
                commentService.delete(Long.parseLong(request.params(":id")));
                LOG.debug("Deleted comment with id: {}", request.params(":id"));
                response.status(201);
                return objectMapper.writeValueAsString("Ok");
              } catch (CommentDeleteException e) {
                LOG.warn("Comment not found.", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              }
            }
    );
  }
}