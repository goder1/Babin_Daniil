package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.controller.article.ArticleController;
import org.example.controller.article.response.ArticleCreateResponse;
import org.example.controller.comment.CommentController;
import org.example.controller.comment.response.CommentCreateResponse;
import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.entities.Comment.Comment;
import org.example.exceptions.EntityNotFoundException;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.InMemoryArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.InMemoryCommentRepository;
import org.example.service.article.ArticleService;
import org.example.service.comment.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class E2ETest {
  private Service service;
  ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void e2eTest() throws IOException, InterruptedException, EntityNotFoundException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
                            commentService,
                            objectMapper
                    ),
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    List<Comment> articleComments;

    long articleId = createArticle();
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(0, articleComments.size());
    long commentId = addComment(articleId);
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(1, articleComments.size());
    updateArticle(articleId);
    deleteComment(commentId);
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(0, articleComments.size());

    Article article = articles.getById(articleId);
    assertEquals("swordart", article.getName());
    assertEquals(Set.of("noth", "wrk"), article.getTags());
  }

  private void deleteComment(long commentId) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), commentId)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
  }

  private void updateArticle(long articleId) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                      { "name": "swordart", "tags": ["noth", "wrk"]}
                                                    """
                                    )
                            )
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    articleId
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
  }

  private long addComment(long articleId) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                      { "articleId": 1, "text": "bla-bla"}
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    CommentCreateResponse commentCreateResponse =
            objectMapper.readValue(response.body(), CommentCreateResponse.class);

    return commentCreateResponse.id();
  }


  private long createArticle() throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                      { "name": "art", "tags": ["nothing", "works"]}
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    ArticleCreateResponse articleCreateResponse =
            objectMapper.readValue(response.body(), ArticleCreateResponse.class);
    return articleCreateResponse.id();
  }
}