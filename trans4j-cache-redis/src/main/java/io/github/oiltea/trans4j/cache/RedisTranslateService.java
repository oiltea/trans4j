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

import io.github.oiltea.trans4j.core.TranslateProvider;
import io.github.oiltea.trans4j.core.TranslateService;
import java.time.Duration;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis-based implementation of the {@link TranslateService} interface.
 *
 * <p>This service provides translation functionality with Redis caching support. It caches
 * translation mappings in Redis hashes to improve performance and reduce calls to the underlying
 * translation provider. Each translation key is prefixed with "trans4j:" to avoid collisions in the
 * Redis namespace.
 *
 * <p>The service automatically fetches translations from the provider when a cache miss occurs,
 * stores the entire mapping for the given key in Redis, and sets a time-to-live (TTL) for the cache
 * entry.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class RedisTranslateService implements TranslateService {

  /**
   * The prefix used for keys in the translation cache.
   *
   * <p>This constant is prepended to cache keys to create a namespace and avoid collisions with
   * other cached data.
   */
  private static final String PREFIX = "trans4j:";

  /** The translation provider used for performing text translations. */
  private final TranslateProvider provider;

  /**
   * Redis template for String operations.
   *
   * <p>Used for performing Redis operations with String values.
   *
   * @see StringRedisTemplate
   */
  private final StringRedisTemplate redis;

  /**
   * The time-to-live duration for the cached data.
   *
   * <p>Specifies how long the cached data remains valid before it expires.
   */
  private final Duration ttl;

  /**
   * Constructs a new RedisTranslateService with the specified dependencies. This service provides
   * translation functionality with Redis caching support.
   *
   * @param provider the translation provider used for actual translation operations
   * @param redis the Redis template for caching translated content
   * @param ttl the time-to-live duration for cached entries in Redis
   */
  public RedisTranslateService(
      TranslateProvider provider, StringRedisTemplate redis, Duration ttl) {
    this.provider = provider;
    this.redis = redis;
    this.ttl = ttl;
  }

  /**
   * Translates a value using the specified key by first checking the Redis cache. If the
   * translation is not found in the cache, it retrieves all translations for the key from the
   * provider, stores them in the cache with a TTL, and then returns the translation for the given
   * value. The cache key is constructed by prefixing the provided key.
   *
   * @param key the translation key used to identify the set of translations, must not be null
   * @param value the specific value to translate within the key's translation set, must not be null
   * @return the translated string for the given value, or {@code null} if the translation is not
   *     found after updating the cache from the provider
   */
  @Override
  public String translate(@NonNull String key, @NonNull String value) {
    String cacheKey = PREFIX + key;

    HashOperations<String, String, String> ops = redis.opsForHash();

    String val = ops.get(cacheKey, value);
    if (val != null) {
      return val;
    }

    Map<String, String> map = provider.get(key);
    ops.putAll(cacheKey, map);
    redis.expire(cacheKey, ttl);

    return map.get(value);
  }
}
