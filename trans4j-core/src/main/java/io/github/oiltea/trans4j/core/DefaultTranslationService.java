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
 * Default implementation of the TranslationService interface.
 *
 * <p>This class provides translation functionality by delegating to a configured
 * TranslationProvider. It serves as a concrete service that can be used to translate keys into
 * localized values based on the provided locale or context.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class DefaultTranslationService implements TranslationService {

  /**
   * The translation provider used for text translation operations.
   *
   * <p>This provider handles the actual translation logic and may support multiple translation
   * services.
   *
   * @see TranslationProvider
   */
  private final TranslationProvider provider;

  /**
   * Constructs a new DefaultTranslationService with the specified translation provider.
   *
   * @param provider the translation provider to be used by this service
   */
  public DefaultTranslationService(TranslationProvider provider) {
    this.provider = provider;
  }

  /**
   * Translates a key-value pair using the provider.
   *
   * <p>This method retrieves the translation for the given key and value from the provider. The key
   * and value must not be null.
   *
   * @param key the translation key, must not be null
   * @param value the value to be translated, must not be null
   * @return the translated string for the given key and value
   */
  @Override
  public String translate(@NonNull String key, @NonNull String value) {
    return provider.get(key).get(value);
  }
}
