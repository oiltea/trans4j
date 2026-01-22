/*
 * Copyright © 2026 oiltea
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
import io.github.oiltea.trans4j.core.TranslateService;

public class CaffeineTranslateService implements TranslateService {

  private final TranslateService delegate;
  private final Cache<Object, Object> cache;

  public CaffeineTranslateService(TranslateService delegate, String spec) {
    this.delegate = delegate;
    this.cache = Caffeine.from(spec).build();
  }

  @Override
  public String translate(String key, String value) {
    String cacheKey = key + ":" + value;
    return cache.get(cacheKey, k -> delegate.translate(key, value)).toString();
  }
}
