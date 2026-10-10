package com.igorkorpak.api;

import com.igorkorpak.api.client.DummyJsonClient;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ProductMutationApiTest extends BaseApiTest {
  @Test void createsSimulatedProduct() {
    var r=DummyJsonClient.post("/products/add",Map.of("title","Portfolio Test Product","price",19.99,"stock",5));
    assertThat(r.statusCode()).isEqualTo(201); assertThat((Integer)r.path("id")).isPositive(); assertThat(r.path("title").toString()).isEqualTo("Portfolio Test Product");
  }
  @Test void updatesProductWithPut() {
    var r=DummyJsonClient.put("/products/1",Map.of("title","Updated by API portfolio","price",88.25));
    assertThat(r.statusCode()).isEqualTo(200); assertThat(r.path("title").toString()).isEqualTo("Updated by API portfolio");
  }
  @Test void partiallyUpdatesProductWithPatch() {
    var r=DummyJsonClient.patch("/products/1",Map.of("stock",7)); assertThat(r.statusCode()).isEqualTo(200); assertThat((Integer)r.path("stock")).isEqualTo(7);
  }
  @Test void deletesProductInSimulation() {
    var r=DummyJsonClient.delete("/products/1"); assertThat(r.statusCode()).isEqualTo(200); assertThat((Boolean)r.path("isDeleted")).isTrue(); assertThat(r.path("deletedOn").toString()).isNotBlank();
  }
  @Test void cannotUpdateMissingProduct() {
    var r=DummyJsonClient.patch("/products/999999",Map.of("title","Never persisted")); assertThat(r.statusCode()).isEqualTo(404);
  }
}
