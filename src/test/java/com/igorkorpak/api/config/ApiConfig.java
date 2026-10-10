package com.igorkorpak.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConfig {
  private static final Properties P = new Properties();
  static {
    try (InputStream in = ApiConfig.class.getResourceAsStream("/config.properties")) {
      if (in == null) throw new IllegalStateException("config.properties is missing");
      P.load(in);
    } catch (IOException e) { throw new ExceptionInInitializerError(e); }
  }
  private ApiConfig() {}
  public static String baseUrl() { return env("DUMMYJSON_BASE_URL", "base.url"); }
  public static String username() { return env("DUMMYJSON_USERNAME", "public.demo.username"); }
  public static String password() { return env("DUMMYJSON_PASSWORD", "public.demo.password"); }
  public static boolean relaxedTls() { return Boolean.parseBoolean(System.getenv().getOrDefault("DUMMYJSON_RELAXED_TLS", "false")); }
  private static String env(String name, String key) { return System.getenv().getOrDefault(name, P.getProperty(key)); }
}
