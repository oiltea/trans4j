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

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for translation cache settings.
 *
 * <p>This class provides configuration properties for cache settings used in translation
 * operations. It supports multiple cache types (Caffeine and Redis) with configurable parameters
 * for each cache implementation. The properties are bound to the "trans4j.cache" configuration
 * prefix.
 *
 * @author Oiltea
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("trans4j.cache")
public class TranslationCacheProperties {
  /** The type of cache. */
  private CacheType type;

  /**
   * Caffeine instance for building cache configurations.
   *
   * <p>This instance is used to create and configure cache builders with various settings such as
   * expiration policies, maximum size, and other cache-specific options.
   *
   * @see Caffeine
   */
  private final Caffeine caffeine = new Caffeine();

  /**
   * Redis client instance for performing Redis operations.
   *
   * <p>This instance is used for all Redis-related operations such as caching, session storage, and
   * pub/sub messaging.
   *
   * @see Redis
   */
  private final Redis redis = new Redis();

  /**
   * Configuration class for Caffeine cache settings.
   *
   * <p>This class provides configuration options for Caffeine cache specifications, allowing
   * customization of cache behavior such as size limits, expiration policies, and other
   * cache-specific settings. The {@code spec} property can be used to define a Caffeine
   * specification string for advanced configuration.
   *
   * @author Oiltea
   * @version 1.0.0
   */
  @Getter
  @Setter
  public static class Caffeine {
    /**
     * The specification string for this component.
     *
     * <p>May be {@code null} if no specific specification is required.
     */
    private @Nullable String spec;
  }

  /**
   * Configuration class for Redis-related settings.
   *
   * <p>This class encapsulates configuration properties for Redis caching, including time-to-live
   * (TTL) duration and key prefix customization. It is typically used as a nested configuration
   * class within a larger configuration structure to manage Redis-specific behavior.
   *
   * @author Oiltea
   * @version 1.0.0
   */
  @Getter
  @Setter
  public static class Redis {
    /**
     * The time-to-live duration for the cached item.
     *
     * <p>Specifies how long the item should remain valid in the cache before it expires. A {@code
     * null} value indicates no explicit expiration time is set.
     */
    private @Nullable Duration timeToLive;

    /**
     * The prefix used for keys in the cache or storage.
     *
     * <p>This value can be {@code null}, indicating no prefix should be applied.
     */
    private @Nullable String keyPrefix;
  }
}
