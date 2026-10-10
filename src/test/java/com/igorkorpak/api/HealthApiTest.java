package com.igorkorpak.api;

import com.igorkorpak.api.client.DummyJsonClient;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HealthApiTest extends BaseApiTest {
  @Test void healthEndpointReturnsOk() {
    var r=DummyJsonClient.get("/test");
    assertThat(r.statusCode()).isEqualTo(200); assertThat(r.path("status").toString()).isEqualTo("ok");
  }
  @Test void responseIsJson() {
    var r=DummyJsonClient.get("/products/1");
    assertThat(r.contentType()).contains("application/json");
  }
  @Test void unknownRouteReturnsNotFound() {
    var r=DummyJsonClient.get("/definitely-not-a-real-route");
    assertThat(r.statusCode()).isEqualTo(404); assertThat(r.path("message").toString()).isNotBlank();
  }
}
