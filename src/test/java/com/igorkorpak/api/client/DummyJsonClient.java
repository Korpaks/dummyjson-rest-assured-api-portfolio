package com.igorkorpak.api.client;

import com.igorkorpak.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public final class DummyJsonClient {
  private DummyJsonClient() {}
  public static RequestSpecification request() {
    RequestSpecification request = RestAssured.given().baseUri(ApiConfig.baseUrl()).contentType(ContentType.JSON).accept(ContentType.JSON);
    return ApiConfig.relaxedTls() ? request.relaxedHTTPSValidation() : request;
  }
  public static Response get(String path) { return request().when().get(path); }
  public static Response get(String path, Map<String, ?> params) { return request().queryParams(params).when().get(path); }
  public static Response post(String path, Object body) { return request().body(body).when().post(path); }
  public static Response put(String path, Object body) { return request().body(body).when().put(path); }
  public static Response patch(String path, Object body) { return request().body(body).when().patch(path); }
  public static Response delete(String path) { return request().when().delete(path); }
  public static String loginToken() {
    return post("/auth/login", Map.of("username", ApiConfig.username(), "password", ApiConfig.password(), "expiresInMins", 5))
      .then().statusCode(200).extract().path("accessToken");
  }
  public static Response authorizedGet(String path, String token) { return request().auth().oauth2(token).when().get(path); }
}
