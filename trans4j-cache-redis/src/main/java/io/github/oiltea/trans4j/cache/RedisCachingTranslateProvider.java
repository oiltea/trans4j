package io.github.oiltea.trans4j.cache;

import io.github.oiltea.trans4j.core.TranslateCache;
import io.github.oiltea.trans4j.core.TranslateProvider;
import java.util.Map;

public class RedisCachingTranslateProvider implements TranslateProvider {

  private final TranslateProvider delegate;
  private final TranslateCache translateCache;

  public RedisCachingTranslateProvider(TranslateProvider delegate, TranslateCache translateCache) {
    this.delegate = delegate;
    this.translateCache = translateCache;
  }

  @Override
  public Map<String, Map<Object, String>> load() {
    return Map.of();
  }
}
