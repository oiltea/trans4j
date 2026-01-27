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
 * Translation data source provider interface
 *
 * <p>Provide basic data needed for translation
 *
 * @author oiltea
 * @since 1.0.0
 */
public interface TranslationProvider {

  /**
   * Retrieves the value associated with the specified key from the map.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   */
  Map<String, String> get(@NonNull String key);
}
