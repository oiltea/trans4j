/*
 * Copyright © 2026 Oiltea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.oiltea.trans4j.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A simple implementation of the {@link TranslationService} interface that provides translation
 * services with caching capabilities. This service delegates translation requests to a configured
 * {@link TranslationProvider} and caches the results to improve performance for repeated requests.
 *
 * <p>The service uses a concurrent hash map to store translations, ensuring thread-safe access in
 * multi-threaded environments. Each translation key is associated with a map of value translations,
 * which are lazily loaded from the provider upon the first request.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class SimpleTranslationService implements TranslationService {

  /**
   * The translation provider instance used for text translation operations.
   *
   * <p>This field holds the reference to the provider that implements the translation logic.
   *
   * @see TranslationProvider
   */
  private final TranslationProvider provider;

  /**
   * Thread-safe cache storing nested maps of strings.
   *
   * <p>The outer map's key is a cache identifier, and the inner map holds key-value pairs. Uses
   * {@link ConcurrentHashMap} for concurrent access.
   */
  private final ConcurrentHashMap<String, Map<String, String>> cache = new ConcurrentHashMap<>();

  /**
   * Constructs a new SimpleTranslationService with the specified translation provider.
   *
   * @param provider the translation provider to be used by this service
   */
  public SimpleTranslationService(TranslationProvider provider) {
    this.provider = provider;
  }

  /**
   * Translates a key-value pair using a caching mechanism.
   *
   * <p>This method first checks if the translation for the given key is already present in the
   * cache. If not, it retrieves the translation map for the key using the provider and stores it in
   * the cache. Finally, it returns the translated value associated with the provided value from the
   * retrieved map.
   *
   * @param key the translation key used to identify the specific translation map
   * @param value the value to be translated using the map associated with the key
   * @return the translated string corresponding to the provided value for the given key
   */
  @Nullable
  @Override
  public String doTranslate(@NonNull String key, @NonNull String value) {
    return cache.computeIfAbsent(key, provider::get).get(value);
  }
}
