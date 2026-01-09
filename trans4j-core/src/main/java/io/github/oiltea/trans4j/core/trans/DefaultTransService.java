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
package io.github.oiltea.trans4j.core.trans;

import java.util.Collections;
import java.util.Map;

public class DefaultTransService implements TransService {

  private final TransDictProvider transDictProvider;

  private volatile Map<String, Map<String, String>> allDictsCache;

  public DefaultTransService(TransDictProvider transDictProvider) {
    this.transDictProvider = transDictProvider;
  }

  @Override
  public String trans(String value) {
    getDict(value);
    return value;
  }

  private Map<String, String> getDict(String type) {
    // 优先从全量字典缓存获取
    if (allDictsCache != null && allDictsCache.containsKey(type)) {
      return allDictsCache.get(type);
    }

    // 按类型查询
    Map<String, String> dict = transDictProvider.getDict(type);
    return dict != null ? dict : Collections.emptyMap();
  }
}