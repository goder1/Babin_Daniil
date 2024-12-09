package org.example.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.controller.comment.response.CommentCreateResponse;
import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.entities.Comment.Comment;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.InMemoryArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.InMemoryCommentRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentControllerTest {
  private Service service;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void createNotExistsArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                      { "articleId": 1984, "text": "comment"}
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }

  @Test
  void successfulCommentCreate() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);
    long articleId = articles.create(article);

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

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

    assertEquals(201, response.statusCode());
    CommentCreateResponse commentCreateResponse =
            objectMapper.readValue(response.body(), CommentCreateResponse.class);
    assertTrue(commentCreateResponse.id() > 0);
  }

  @Test
  void successfulCommentDelete() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);
    long articleId = articles.create(article);
    comments.create(new Comment(new ArticleId(articleId), "bla-bla"));

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), 1)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
  }

  @Test
  void deleteNotExistsComment() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), 1984)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }
}