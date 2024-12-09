package org.example.controller.article;

import org.example.FreeMarkerEngine;
import org.example.controller.Controller;
import org.example.entities.Article.Article;
import org.example.service.article.ArticleService;
import org.example.service.comment.CommentService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleFreeMarkerController implements Controller {
  private final Service service;
  private final ArticleService articleService;
  private final CommentService commentService;
  private final FreeMarkerEngine freeMarkerEngine;

  public ArticleFreeMarkerController(
          Service service,
          ArticleService articleService,
          CommentService commentService,
          FreeMarkerEngine freeMarkerEngine
  ) {
    this.service = service;
    this.articleService = articleService;
    this.commentService = commentService;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getAllArticles();
  }

  private void getAllArticles() {
    service.get(
            "/",
            (Request request, Response response) -> {
              response.type("text/html; charset=utf-8");
              List<Article> articles = articleService.findAll();
              List<Map<String, String>> articlesList = new ArrayList<>();
              for (Article article : articles) {
                Long commentCount = commentService.getCountByArticleId(article.getId().getId());
                articlesList.add(Map.of("name", article.getName(), "commentCount", commentCount.toString()));
              }

              Map<String, Object> model = new HashMap<>();
              model.put("articles", articlesList);
              return freeMarkerEngine.render(new ModelAndView(model, "index.ftl"));
            }
    );
  }
}