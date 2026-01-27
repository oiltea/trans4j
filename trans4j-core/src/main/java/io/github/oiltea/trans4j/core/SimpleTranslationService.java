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
 * Memory-based translation service implementation that provides caching functionality for
 * translations.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class SimpleTranslationService implements TranslationService {

  private final TranslationProvider provider;

  private final ConcurrentHashMap<String, Map<String, String>> cache = new ConcurrentHashMap<>();

  public SimpleTranslationService(TranslationProvider provider) {
    this.provider = provider;
  }

  @Nullable
  @Override
  public String doTranslate(@NonNull String key, @NonNull String value) {
    return cache.computeIfAbsent(key, provider::get).get(value);
  }
}
