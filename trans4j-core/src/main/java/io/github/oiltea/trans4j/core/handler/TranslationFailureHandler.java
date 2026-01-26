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

package io.github.oiltea.trans4j.core.handler;

import org.jspecify.annotations.Nullable;

/**
 * Interface for handling translation failures.
 *
 * <p>Implementations of this interface define how to handle cases where translation fails or a
 * value is not found in the translation map.
 *
 * @author oiltea
 * @since 1.1.0
 */
@FunctionalInterface
public interface TranslationFailureHandler {

  /**
   * Handles a translation failure and returns an appropriate fallback value.
   *
   * @param key the translation key
   * @param originalValue the original untranslated value
   * @return the fallback value to use, or null
   */
  @Nullable String handle(String key, @Nullable String originalValue);
}
