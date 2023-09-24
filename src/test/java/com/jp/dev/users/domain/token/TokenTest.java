package com.jp.dev.users.domain.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TokenTest {

  @Test
  public void testTokenConstructor() {
    String expectedToken = "abc123";
    Token token = new Token(expectedToken);
    assertEquals(expectedToken, token.getToken());
  }

  @Test
  public void testTokenGetterSetter() {
    String expectedToken = "xyz789";
    Token token = new Token();
    token.setToken(expectedToken);
    assertEquals(expectedToken, token.getToken());
  }
}
