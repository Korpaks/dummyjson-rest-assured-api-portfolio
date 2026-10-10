package com.igorkorpak.api;

import com.igorkorpak.api.client.DummyJsonClient;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProductReadApiTest extends BaseApiTest {
  @Test void getsProductByIdAndValidatesSchema() {
    var r=DummyJsonClient.get("/products/1");
    r.then().statusCode(200).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/product.json"));
    assertThat((Integer)r.path("id")).isEqualTo(1); assertThat(r.path("title").toString()).isNotBlank();
  }
  @Test void productPageHonorsLimitAndSkip() {
    var r=DummyJsonClient.get("/products",Map.of("limit",5,"skip",10));
    r.then().statusCode(200).body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/products-page.json"));
    assertThat((List<?>)r.path("products")).hasSize(5); assertThat((Integer)r.path("skip")).isEqualTo(10);
  }
  @Test void selectReturnsOnlyRequestedProductFields() {
    var item=(Map<String,Object>)DummyJsonClient.get("/products",Map.of("limit",1,"select","title,price")).path("products[0]");
    assertThat(item).containsKeys("id","title","price").doesNotContainKey("description");
  }
  @Test void ascendingPriceSortIsMonotonic() {
    List<Float> prices=DummyJsonClient.get("/products",Map.of("limit",20,"sortBy","price","order","asc")).path("products.price");
    assertThat(prices).isSorted();
  }
  @Test void descendingPriceSortIsMonotonic() {
    List<Float> prices=DummyJsonClient.get("/products",Map.of("limit",20,"sortBy","price","order","desc")).path("products.price");
    assertThat(prices).isSortedAccordingTo(Comparator.reverseOrder());
  }
  @Test void searchReturnsRelevantProducts() {
    List<Map<String,Object>> products=DummyJsonClient.get("/products/search",Map.of("q","phone")).path("products");
    assertThat(products).isNotEmpty().allMatch(product->product.toString().toLowerCase().contains("phone"));
  }
  @Test void categoryFilterReturnsOnlyRequestedCategory() {
    List<String> categories=DummyJsonClient.get("/products/category/smartphones").path("products.category");
    assertThat(categories).isNotEmpty().containsOnly("smartphones");
  }
  @Test void categoriesEndpointReturnsUniqueNonBlankSlugs() {
    List<String> slugs=DummyJsonClient.get("/products/category-list").then().statusCode(200).extract().as(List.class);
    assertThat(slugs).isNotEmpty().doesNotHaveDuplicates().allMatch(s->!s.isBlank());
  }
  @Test void missingProductReturns404() {
    var r=DummyJsonClient.get("/products/999999"); assertThat(r.statusCode()).isEqualTo(404); assertThat(r.path("message").toString()).containsIgnoringCase("not found");
  }
}
