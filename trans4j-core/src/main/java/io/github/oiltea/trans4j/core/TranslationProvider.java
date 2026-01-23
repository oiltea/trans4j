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
import org.jspecify.annotations.NonNull;

/**
 * Interface for translation providers that retrieve localized strings based on a given key.
 *
 * <p>Implementations of this interface are responsible for providing translation mappings,
 * typically from a resource bundle, database, or external service. The primary use case is to
 * support internationalization (i18n) by allowing dynamic lookup of translated text.
 *
 * @author oiltea
 * @version 1.0.0
 */
public interface TranslationProvider {

  /**
   * Retrieves the value associated with the specified key from the map. The key must not be null.
   *
   * @param key the key whose associated value is to be returned, must not be null
   * @return the value to which the specified key is mapped, guaranteed to be non-null
   */
  @NonNull Map<String, String> get(@NonNull String key);
}
