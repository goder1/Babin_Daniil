package org.example.controller.article;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.controller.article.response.ArticleCreateResponse;
import org.example.controller.article.response.ArticleFindResponse;
import org.example.entities.Article.Article;
import org.example.exceptions.ArticleNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArticleControllerTest {
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
  void successfulArticleCreate() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                                                    { "name": "art", "tags": ["nothing", "works"]}
                                                  """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
    ArticleCreateResponse articleCreateResponse =
            objectMapper.readValue(response.body(), ArticleCreateResponse.class);
    assertTrue(articleCreateResponse.id() > 0);
  }

  @Test
  void successfulGetArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

//    String articleName = "art";
//    Set<String> articleTags = Set.of("nothing", "works");
//    Article article = new Article(articleName, articleTags);
//    article = articles.create(article);
//    long articleId = article.getId().getId();
//    assertEquals(1, articleId);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response1 = HttpClient.newHttpClient()
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
    assertEquals(201, response1.statusCode());
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles/1".formatted(service.port())
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
//    assertEquals(1, article.getId().getId());
//    assertEquals(articleName, article.getName());
//    assertEquals(articleTags, article.getTags());
  }

  @Test
  void getNotExistArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);


    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                            .GET()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    1984
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }

  @Test
  void successfulGetArticles() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);
    long articleId = articles.create(article);

    String articleName2 = "art";
    Set<String> articleTags2 = Set.of("nothing", "works");
    Article article2 = new Article(articleName2, articleTags2);
    long articleId2 = articles.create(article2);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                            .GET()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles".formatted(service.port())
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());

    List<ArticleFindResponse> responses = objectMapper.readValue(response.body(), new TypeReference<List<ArticleFindResponse>>() {
    });

    assertEquals(articleId, responses.get(0).getId());
    assertEquals(Set.of("nothing", "works"), responses.get(0).getTags());
    assertEquals(articleName, responses.get(0).getName());

    assertEquals(articleId2, (int) responses.get(1).getId());
    assertEquals(Set.of("nothing", "works"), responses.get(1).getTags());
    assertEquals(articleName2, responses.get(1).getName());
  }

  @Test
  void successfulDeleteArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);
    long articleId = articles.create(article);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    articleId
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
  }

  @Test
  void deleteNotExistsArticle() throws IOException, InterruptedException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    1984
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
    assertEquals(400, response.statusCode());
  }

  @Test
  void successfulUpdateArticle() throws IOException, InterruptedException, EntityNotFoundException, ArticleNotFoundException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "art";
    Set<String> articleTags = Set.of("nothing", "works");
    Article article = new Article(articleName, articleTags);
    long articleId = articles.create(article);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "name": "swordart", "tags": ["nothing squared", "works at all"]}
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

    assertEquals(201, response.statusCode());
    Article updatedArticle = articleService.findById(articleId);

    assertEquals("swordart", updatedArticle.getName());
    assertEquals(Set.of("nothing squared", "works at all"), updatedArticle.getTags());
  }

  @Test
  void updateNotExistsArticle() throws IOException, InterruptedException, EntityNotFoundException, ArticleNotFoundException {
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
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
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "name": "art", "tags": ["nothing", "works"]}
                                                  """
                                    )
                            )
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    1984
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }
}