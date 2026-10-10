package com.igorkorpak.api;

import com.igorkorpak.api.client.DummyJsonClient;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class ResourceApiTest extends BaseApiTest {
  @Test void userCartsBelongToRequestedUser() {
    List<Integer> ids=DummyJsonClient.get("/carts/user/5").path("carts.userId"); assertThat(ids).isNotEmpty().containsOnly(5);
  }
  @Test void userPostsBelongToRequestedUser() {
    List<Integer> ids=DummyJsonClient.get("/posts/user/5").path("posts.userId"); assertThat(ids).isNotEmpty().containsOnly(5);
  }
  @Test void userTodosBelongToRequestedUser() {
    List<Integer> ids=DummyJsonClient.get("/todos/user/13").path("todos.userId"); assertThat(ids).isNotEmpty().containsOnly(13);
  }
  @Test void postSearchReturnsMatchingContent() {
    List<Map<String,Object>> posts=DummyJsonClient.get("/posts/search",Map.of("q","love")).path("posts");
    assertThat(posts).isNotEmpty().allMatch(p->(p.get("title")+" "+p.get("body")).toLowerCase().contains("love"));
  }
  @Test void todoPaginationIsConsistent() {
    var r=DummyJsonClient.get("/todos",Map.of("limit",4,"skip",3)); assertThat((List<?>)r.path("todos")).hasSize(4); assertThat((Integer)r.path("skip")).isEqualTo(3); assertThat((Integer)r.path("limit")).isEqualTo(4);
  }
}
