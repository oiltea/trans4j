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
package io.github.oiltea.trans4j.core.resolver;

import io.github.oiltea.trans4j.core.cache.DictCache;
import io.github.oiltea.trans4j.core.source.DictSource;

public class DefaultDictResolver implements DictResolver {

  private final DictSource dictSource;
  private final DictCache dictCache;

  public DefaultDictResolver(DictSource dictSource, DictCache dictCache) {
    this.dictSource = dictSource;
    this.dictCache = dictCache;
  }

  @Override
  public String resolve(String key, String value) {
    String name = dictCache.get(key, value).orElse(null);
    if (name != null) {
      return name;
    }

    String resolved = dictSource.get(key, value);
    if (resolved != null) {
      dictCache.put(key, value, resolved);
    }
    return resolved;
  }
}