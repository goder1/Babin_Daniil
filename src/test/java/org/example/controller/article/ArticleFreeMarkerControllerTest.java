package org.example.controller.article;

import org.example.Application;
import org.example.FreeMarkerEngine;
import org.example.TemplateFactory;
import org.example.entities.Article.Article;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.PostgresArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.PostgresCommentRepository;
import org.example.service.article.ArticleService;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class ArticleFreeMarkerControllerTest {
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
  void successfulGetArticles() throws IOException, InterruptedException {
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
    ArticleService articleService = new ArticleService(articles, null);
    CommentService commentService = new CommentService(articles, comments);
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    String articleName = "damn";
    Set<String> articleTags = Set.of("ge", "nsh");
    Article article = new Article(articleName, articleTags);

    long articleId = articles.create(article);

    String articleName2 = "damn2";
    Set<String> articleTags2 = Set.of("ge2", "nsh2");
    Article article2 = new Article(articleName2, articleTags2);

    long articleId2 = articles.create(article2);

    Application application = new Application(
            List.of(
                    new ArticleFreeMarkerController(
                            service,
                            articleService,
                            commentService,
                            freeMarkerEngine
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/".formatted(service.port())
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(200, response.statusCode());
    assertThat(response.body()).contains("<td>%s</td>".formatted(articleName));
    assertThat(response.body()).contains("<td>%s</td>".formatted(articleName2));
  }
}