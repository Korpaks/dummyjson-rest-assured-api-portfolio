package com.igorkorpak.api;

import com.igorkorpak.api.client.DummyJsonClient;
import com.igorkorpak.api.config.ApiConfig;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AuthApiTest extends BaseApiTest {
  @Test void validLoginReturnsTokensAndIdentity() {
    var r=DummyJsonClient.post("/auth/login",Map.of("username",ApiConfig.username(),"password",ApiConfig.password(),"expiresInMins",5));
    assertThat(r.statusCode()).isEqualTo(200); assertThat(r.path("accessToken").toString()).isNotBlank(); assertThat(r.path("refreshToken").toString()).isNotBlank(); assertThat(r.path("username").toString()).isEqualTo(ApiConfig.username());
  }
  @Test void currentUserRequiresAndAcceptsBearerToken() {
    var r=DummyJsonClient.authorizedGet("/auth/me",DummyJsonClient.loginToken()); assertThat(r.statusCode()).isEqualTo(200); assertThat(r.path("username").toString()).isEqualTo(ApiConfig.username());
  }
  @Test void missingTokenIsRejected() { assertThat(DummyJsonClient.get("/auth/me").statusCode()).isEqualTo(401); }
  @Test void invalidPasswordIsRejectedSafely() {
    var r=DummyJsonClient.post("/auth/login",Map.of("username",ApiConfig.username(),"password","intentionally-wrong")); assertThat(r.statusCode()).isEqualTo(400); assertThat(r.asString()).doesNotContain("accessToken");
  }
  @Test void missingUsernameIsRejected() {
    var r=DummyJsonClient.post("/auth/login",Map.of("password","not-a-real-secret")); assertThat(r.statusCode()).isBetween(400,499);
  }
}
