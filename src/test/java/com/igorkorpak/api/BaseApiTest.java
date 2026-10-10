package com.igorkorpak.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseApiTest {
  @BeforeAll static void configureDiagnostics() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }
}
