package org.example.user;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepositoryInterface {
  private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

  @Override
  public User findByMsisdn(String msisdn) {
    return users.get(msisdn);
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    users.put(msisdn, user);
  }
}