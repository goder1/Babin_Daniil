package org.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.controller.article.ArticleController;
import org.example.controller.article.ArticleFreeMarkerController;
import org.example.controller.comment.CommentController;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.PostgresArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.PostgresCommentRepository;
import org.example.service.article.ArticleService;
import org.example.service.comment.CommentService;
import org.example.service.migrate.MigrateService;
import org.jdbi.v3.core.Jdbi;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.Spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    Config config = ConfigFactory.load();
    MigrateService migrateService = new MigrateService(
            config.getString("app.database.url"),
            config.getString("app.database.user"),
            config.getString("app.database.password")
    );
    migrateService.migrate();

    Jdbi jdbi = Jdbi.create(
            config.getString("app.database.url"),
            config.getString("app.database.user"),
            config.getString("app.database.password")
    );

    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articles = new PostgresArticleRepository(jdbi);
    CommentRepository comments = new PostgresCommentRepository(jdbi);
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    Application application =
            new Application(
                    List.of(
                            new ArticleController(
                                    service,
                                    new ArticleService(articles, null),
                                    new CommentService(articles, comments),
                                    objectMapper
                            ),
                            new CommentController(
                                    service,
                                    new CommentService(articles, comments),
                                    objectMapper
                            ),
                            new ArticleFreeMarkerController(
                                    service,
                                    new ArticleService(articles, null),
                                    new CommentService(articles, comments),
                                    freeMarkerEngine
                            )
                    )
            );
    application.start();
  }
}

