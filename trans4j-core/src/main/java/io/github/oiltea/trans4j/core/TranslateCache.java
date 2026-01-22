package io.github.oiltea.trans4j.core;

import java.util.Optional;

public interface TranslateCache {

  void put(String key, String value, String text);

  Optional<String> get(String key, String value);

  void remove(String key);

  void remove(String key, String value);

  void clear();
}
