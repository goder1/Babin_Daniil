package org.example.service.migrate;

import org.flywaydb.core.Flyway;

public class MigrateService {
  private final String dataSource;
  private final String user;
  private final String pass;

  public MigrateService(String dataSource, String user, String pass) {
    this.dataSource = dataSource;
    this.user = user;
    this.pass = pass;
  }

  public void migrate() {
    System.out.println("classpath:/../db/migrations");
    Flyway flyway =
            Flyway.configure()
                    .locations("classpath:db/migrations")
                    .dataSource(dataSource, user, pass)
                    .load();
    flyway.migrate();
  }
}