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
import org.jspecify.annotations.Nullable;

/**
 * Interface for translation services.
 *
 * <p>Provides a contract for translating text based on a key and an original value. Implementations
 * of this interface are responsible for defining the specific translation logic, which may involve
 * external APIs, databases, or internal mapping.
 *
 * @author oiltea
 * @version 1.0.0
 */
public interface TranslateService {

  /**
   * Translates a key-value pair into the corresponding localized string.
   *
   * <p>This method uses the provided key and value to look up and return the appropriate localized
   * translation. Both parameters must be non-null. If no translation is found, the method may
   * return null.
   *
   * @param key the non-null key used to identify the translation string
   * @param value the non-null value to be used in the translation lookup
   * @return the localized translation string, or {@code null} if no translation is found
   */
  @Nullable String translate(@NonNull String key, @NonNull String value);
}
