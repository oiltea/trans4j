package io.github.oiltea.trans4j;

import io.github.oiltea.trans4j.core.TranslateProvider;
import java.util.Map;

public class CustomTranslateProvider implements TranslateProvider {

  @Override
  public Map<String, Map<Object, String>> load() {
    return Map.of("GENDER", Map.of("1", "男", "2", "女"));
  }

  @Override
  public Map<String, Map<Object, String>> refresh() {
    return Map.of();
  }

  @Override
  public Map<String, Map<Object, String>> refresh(String key) {
    return Map.of();
  }
}
