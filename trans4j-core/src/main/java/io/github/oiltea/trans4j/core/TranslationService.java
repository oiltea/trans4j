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

import org.jspecify.annotations.NonNull;

/**
 * Interface for translation services.
 *
 * <p>Provides a contract for translating text based on a key and an original value. Implementations
 * of this interface are responsible for defining the specific translation logic.
 *
 * @author oiltea
 * @since 1.0.0
 */
public interface TranslationService {

  /**
   * Translates a key-value pair using the underlying translation mechanism.
   *
   * <p>This method serves as a default implementation that performs null checks on the provided
   * arguments before delegating to the actual translation logic. If either the key or the value is
   * null, the method returns null.
   *
   * @param key the translation key, can be null
   * @param value the value associated with the key for translation, can be null
   * @return the translated string, or null if either the key or value is null
   */
  default String translate(String key, String value) {
    if (key == null || value == null) {
      return null;
    }
    return doTranslate(key, value);
  }

  /**
   * Translates the given key using the provided value.
   *
   * <p>This method performs translation by processing the key with the specified value.
   *
   * @param key the translation key to be processed, must not be null
   * @param value the value to be used for translation, must not be null
   * @return the translated string
   * @since 1.1.0
   */
  String doTranslate(@NonNull String key, @NonNull String value);
}
