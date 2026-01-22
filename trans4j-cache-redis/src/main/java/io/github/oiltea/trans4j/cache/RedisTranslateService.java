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

package io.github.oiltea.trans4j.cache;

import io.github.oiltea.trans4j.core.TranslateService;
import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisTranslateService implements TranslateService {

  private final TranslateService delegate;
  private final StringRedisTemplate redis;
  private final Duration ttl;

  public RedisTranslateService(TranslateService delegate, StringRedisTemplate redis, Duration ttl) {
    this.delegate = delegate;
    this.redis = redis;
    this.ttl = ttl;
  }

  private String buildKey(String type, Object key) {
    return "trans4j:" + type + ":" + key;
  }

  @Override
  public String translate(String key, String value) {
    String cacheKey = buildKey(key, value);

    String val = redis.opsForValue().get(cacheKey);

    if (val != null) {
      return val;
    }

    String result = delegate.translate(key, value);

    if (result != null) {
      redis.opsForValue().set(cacheKey, result, ttl);
    }
    return result;
  }
}
