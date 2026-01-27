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

package io.github.oiltea.trans4j.cache;

import io.github.oiltea.trans4j.core.TranslationProvider;
import io.github.oiltea.trans4j.core.TranslationService;
import java.time.Duration;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis-based translation service implementation that provides caching functionality for
 * translations.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class RedisTranslationService implements TranslationService {

  private static final String PREFIX = "trans4j:";

  private final TranslationProvider provider;
  private final StringRedisTemplate redisTemplate;
  private final Duration ttl;

  public RedisTranslationService(
      TranslationProvider provider, StringRedisTemplate redisTemplate, Duration ttl) {
    this.provider = provider;
    this.redisTemplate = redisTemplate;
    this.ttl = ttl;
  }

  @Nullable
  @Override
  public String doTranslate(@NonNull String key, @NonNull String value) {
    String cacheKey = PREFIX + key;

    HashOperations<String, String, String> ops = redisTemplate.opsForHash();

    String val = ops.get(cacheKey, value);
    if (val != null) {
      return val;
    }

    Map<String, String> map = provider.get(key);
    if (map == null) {
      return null;
    }
    ops.putAll(cacheKey, map);
    redisTemplate.expire(cacheKey, ttl);

    return map.get(value);
  }
}
