package entities;

import java.time.ZonedDateTime;

public class User {
  private Integer id;
  private String name;
  private String email;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  public User(Integer id, String name, String email, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
