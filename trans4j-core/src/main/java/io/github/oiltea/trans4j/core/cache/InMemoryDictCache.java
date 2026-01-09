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
package io.github.oiltea.trans4j.core.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDictCache implements DictCache {

  private static final Map<String, Map<String, String>> CACHE = new ConcurrentHashMap<>();

  @Override
  public Optional<String> get(String key, String value) {
    return Optional.ofNullable(CACHE.get(key)).map(o -> o.get(value));
  }

  @Override
  public void put(String key, String value, String text) {
    CACHE.put(key, CACHE.getOrDefault(key, Map.of(value, text)));
  }
}