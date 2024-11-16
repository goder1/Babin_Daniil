package org.example.user;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepositoryInterface {
  private final ConcurrentHashMap<String, User> hashMap = new ConcurrentHashMap<>();

  @Override
  public User findByMsisdn(String msisdn) {
    return hashMap.get(msisdn);
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    hashMap.put(msisdn, user);
  }
}