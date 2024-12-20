package org.example.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.controller.comment.response.CommentCreateResponse;
import org.example.entities.Article.Article;
import org.example.entities.Article.ArticleId;
import org.example.entities.Comment.Comment;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.PostgresArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.PostgresCommentRepository;
import org.example.service.comment.CommentService;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
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

@Testcontainers
class CommentControllerTest {
  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:14");

  private Service service;
  private static Jdbi jdbi;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway =
            Flyway.configure()
                    .outOfOrder(true)
                    .locations("classpath:db/migrations")
                    .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                    .load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void createNotExistsArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
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
                                                      { "articleId": 1337, "text": "comment"}
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
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "damn";
    Set<String> articleTags = Set.of("ge", "nsh");
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
                                                      { "articleId": 1, "text": "comment"}
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
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "damn";
    Set<String> articleTags = Set.of("ge", "nsh");
    Article article = new Article(articleName, articleTags);

    long articleId = articles.create(article);
    comments.create(new Comment(null, new ArticleId(articleId), "comment"));

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
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), articleId)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
  }

  @Test
  void deleteNotExistsComment() throws IOException, InterruptedException {
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
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
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), 1337)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }
}