package org.example.controller.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.Controller;
import org.example.controller.ErrorResponse;
import org.example.controller.article.request.ArticleCreateRequest;
import org.example.controller.article.request.ArticleUpdateRequest;
import org.example.controller.article.response.ArticleCreateResponse;
import org.example.controller.article.response.ArticleFindResponse;
import org.example.controller.comment.response.CommentResponse;
import org.example.entities.Article.Article;
import org.example.entities.Comment.Comment;
import org.example.exceptions.ArticleDeleteException;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.ArticleUpdateException;
import org.example.service.article.ArticleService;
import org.example.service.comment.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.ArrayList;
import java.util.List;

public class ArticleController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

  private final Service service;
  private final ArticleService articleService;
  private final ObjectMapper objectMapper;
  private final CommentService commentService;

  public ArticleController(
          Service service,
          ArticleService articleService,
          CommentService commentService,
          ObjectMapper objectMapper
  ) {
    this.service = service;
    this.articleService = articleService;
    this.objectMapper = objectMapper;
    this.commentService = commentService;
  }

  @Override
  public void initializeEndpoints() {
    createArticle();
    findArticle();
    findAll();
    delete();
    update();
  }

  private void createArticle() {
    service.post(
            "/api/articles",
            (Request request, Response response) -> {
              try {
                response.type("application/json");
                String body = request.body();
                ArticleCreateRequest articleCreateRequest =
                        objectMapper.readValue(body, ArticleCreateRequest.class);
                long articleId =
                        articleService.create(articleCreateRequest.name(), articleCreateRequest.tags());
                response.status(201);
                LOG.debug("Created article with id {}", articleId);
                return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
              } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              }
            }
    );
  }

  private void findArticle() {
    service.get(
            "/api/articles/:id",
            (Request request, Response response) -> {
              try {
                response.type("application/json");
                Article article;
                article = articleService.findById(Long.parseLong(request.params(":id")));
                List<Comment> comments = commentService.findAllByArticleId(article.getId().getId());

                List<CommentResponse> commentResponseList = new ArrayList<>();
                for (Comment comment : comments) {
                  commentResponseList.add(new CommentResponse(comment));
                }
                ArticleFindResponse articleResponse = new ArticleFindResponse(article, commentResponseList);
                LOG.debug("Article found with id {}", article.getId());
                response.status(201);
                return objectMapper.writeValueAsString(articleResponse);
              } catch (ArticleNotFoundException e) {
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

  private void findAll() {
    service.get(
            "/api/articles",
            (Request request, Response response) -> {
              try {
                response.type("application/json");
                List<Article> articles;
                articles = articleService.findAll();
                List<ArticleFindResponse> listResponse = new ArrayList<>();

                for (Article article : articles) {
                  List<Comment> comments = commentService.findAllByArticleId(article.getId().getId());
                  List<CommentResponse> commentResponseList = new ArrayList<>();
                  for (Comment comment : comments) {
                    commentResponseList.add(new CommentResponse(comment));
                  }
                  listResponse.add(new ArticleFindResponse(article, commentResponseList));
                }
                LOG.debug("Articles found.");
                response.status(201);
                return objectMapper.writeValueAsString(listResponse);
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
            "/api/articles/:id",
            (Request request, Response response) -> {
              response.type("application/json");
              try {
                articleService.delete(Long.parseLong(request.params(":id")));
                articleService.deleteCommentsByArticleId(Long.parseLong(request.params(":id")));
                LOG.debug("Deleted article with id {}", request.params(":id"));
                response.status(201);
                return objectMapper.writeValueAsString("Ok");
              } catch (ArticleDeleteException e) {
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

  private void update() {
    service.put(
            "/api/articles/:id",
            (Request request, Response response) -> {
              response.type("application/json");
              String body = request.body();
              ArticleUpdateRequest articleUpdateRequest =
                      objectMapper.readValue(body, ArticleUpdateRequest.class);
              try {
                articleService.update(
                        Long.parseLong(request.params(":id")),
                        articleUpdateRequest.name(),
                        articleUpdateRequest.tags()
                );
              } catch (ArticleUpdateException e) {
                LOG.warn("Article not found.", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              }

              response.status(201);
              return objectMapper.writeValueAsString("Ok");
            }
    );
  }
}