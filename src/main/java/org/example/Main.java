package org.example;

import org.example.controller.article.ArticleController;
import org.example.controller.article.ArticleFreeMarkerController;
import org.example.controller.comment.CommentController;
import org.example.repository.article.ArticleRepository;
import org.example.repository.article.InMemoryArticleRepository;
import org.example.repository.comment.CommentRepository;
import org.example.repository.comment.InMemoryCommentRepository;
import org.example.service.article.ArticleService;
import org.example.service.comment.CommentService;
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
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articles = new InMemoryArticleRepository();
    CommentRepository comments = new InMemoryCommentRepository();
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    Application application =
            new Application(
                    List.of(
                            new ArticleController(
                                    service,
                                    new ArticleService(articles, comments),
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
                                    new ArticleService(articles, comments),
                                    new CommentService(articles, comments),
                                    freeMarkerEngine
                            )
                    )
            );
    application.start();
  }
}

