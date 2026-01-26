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

package io.github.oiltea.trans4j.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.oiltea.trans4j.core.TranslationProvider;
import io.github.oiltea.trans4j.core.TranslationService;
import java.util.Map;
import org.jspecify.annotations.NonNull;

/**
 * Caffeine-based translation service implementation that provides caching functionality for
 * translations.
 *
 * <p>This service uses Caffeine cache to store translation mappings retrieved from a {@link
 * TranslationProvider}, improving performance by reducing repeated calls to the underlying
 * provider. It implements the {@link TranslationService} interface to provide translation
 * capabilities with automatic caching.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class CaffeineTranslationService implements TranslationService {

  /**
   * The translation provider used for text translation operations.
   *
   * <p>This provider handles all translation requests and manages the translation process.
   *
   * @see TranslationProvider
   */
  private final TranslationProvider provider;

  /**
   * Cache instance for storing key-value pairs where keys are strings and values are maps of
   * strings.
   *
   * <p>The cache is final and thread-safe, used to improve performance by reducing repeated
   * computations or data fetching.
   *
   * @see Cache
   */
  private final Cache<String, Map<String, String>> cache;

  /**
   * Creates a new CaffeineTranslationService with the specified translation provider and cache
   * specification.
   *
   * <p>This constructor initializes the service with a translation provider and configures a
   * Caffeine cache using the provided specification string. The cache specification must follow
   * Caffeine's format.
   *
   * @param provider the translation provider to be used for translation operations
   * @param spec the cache specification string for configuring the Caffeine cache
   * @see <a
   *     href="https://github.com/ben-manes/caffeine/wiki/Specification">https://github.com/ben-manes/caffeine/wiki/Specification</a>
   */
  public CaffeineTranslationService(TranslationProvider provider, String spec) {
    this.provider = provider;
    this.cache = Caffeine.from(spec).build();
  }

  /**
   * Translates a value using the specified key and value.
   *
   * <p>This method looks up a translation map in the cache using the provided key. If the map
   * exists, it returns the translation for the given value. If the map is not found in the cache,
   * it returns null.
   *
   * @param key the key used to retrieve the translation map from the cache
   * @param value the value to be translated within the retrieved map
   * @return the translated string, or null if the translation map is not found or the value is not
   *     present in the map
   */
  @Override
  public String doTranslate(@NonNull String key, @NonNull String value) {
    Map<String, String> map = cache.get(key, provider::get);
    if (map != null) {
      return map.get(value);
    }
    return null;
  }
}
