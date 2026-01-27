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
 * @author Oiltea
 * @since 1.0.0
 */
public class CaffeineTranslationService implements TranslationService {

  private final TranslationProvider provider;

  private final Cache<String, Map<String, String>> cache;

  /**
   * Creates a new CaffeineTranslationService with the specified translation provider and cache
   * specification.
   *
   * <p>This constructor initializes the service with a translation provider and builds a Caffeine
   * cache using the provided cache specification string. The cache specification string should
   * follow the format defined by Caffeine's specification parser.
   *
   * @param provider the translation provider to be used for translation operations
   * @param spec the cache specification string used to configure the Caffeine cache
   * @see com.github.benmanes.caffeine.cache.Caffeine#from(String)
   */
  public CaffeineTranslationService(TranslationProvider provider, String spec) {
    this.provider = provider;
    this.cache = Caffeine.from(spec).build();
  }

  @Override
  public String doTranslate(@NonNull String key, @NonNull String value) {
    Map<String, String> map = cache.get(key, provider::get);
    if (map == null) {
      return null;
    }
    return map.get(value);
  }
}
