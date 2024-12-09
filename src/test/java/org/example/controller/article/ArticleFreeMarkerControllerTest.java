package org.example.controller.article;

import org.example.Application;
import org.example.FreeMarkerEngine;
import org.example.TemplateFactory;
import org.example.entities.Article.Article;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleFreeMarkerControllerTest {
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
  void successfulGetArticles() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    String name = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(name, articleTags);
    long articleId = articles.create(article);

    String name2 = "swordart";
    Set<String> articleTags2 = Set.of("nothinggg", "worksss");
    Article article2 = new Article(name2, articleTags2);
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
    assertThat(response.body()).contains("<td>%s</td>".formatted(name));
    assertThat(response.body()).contains("<td>%s</td>".formatted(name2));
  }
}