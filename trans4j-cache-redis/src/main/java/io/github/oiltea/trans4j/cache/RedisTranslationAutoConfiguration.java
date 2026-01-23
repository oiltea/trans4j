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

import io.github.oiltea.trans4j.core.TranslationCacheProperties;
import io.github.oiltea.trans4j.core.TranslationProvider;
import io.github.oiltea.trans4j.core.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis-based translation auto-configuration class.
 *
 * <p>This class provides auto-configuration for translation services using Redis as the cache
 * backend. It is conditionally loaded when the property `trans4j.cache.type` is set to `redis`. The
 * configuration creates a {@link StringRedisTemplate} bean and a {@link TranslationService} bean
 * that uses Redis for caching translation results, improving performance by reducing repeated calls
 * to the underlying translation provider.
 *
 * @author Oiltea
 * @version 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "redis")
public class RedisTranslationAutoConfiguration {

  /**
   * Creates and configures a {@link StringRedisTemplate} bean.
   *
   * <p>This method provides a {@link StringRedisTemplate} instance configured with the specified
   * {@link RedisConnectionFactory}. The {@link StringRedisTemplate} is a specialized {@link
   * org.springframework.data.redis.core.RedisTemplate} for common usage when keys and values are
   * {@link String}s.
   *
   * @param connectionFactory the {@link RedisConnectionFactory} to be used by the template for
   *     creating connections
   * @return a fully configured {@link StringRedisTemplate} instance
   * @since (version or date if applicable, otherwise omit)
   */
  @Bean
  @ConditionalOnMissingBean(StringRedisTemplate.class)
  @ConditionalOnBean(RedisConnectionFactory.class)
  StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    return new StringRedisTemplate(connectionFactory);
  }

  /**
   * Creates a Redis-based translation service bean when a {@link TranslationProvider} bean is
   * available. This service caches translation results in Redis to improve performance and reduce
   * external API calls.
   *
   * @param provider the translation provider used for actual translation operations
   * @param stringRedisTemplate the Redis template for cache operations
   * @param props configuration properties for the translation cache
   * @return a configured {@link TranslationService} implementation that uses Redis for caching
   * @since (version or date if applicable)
   */
  @Bean
  @ConditionalOnBean(TranslationProvider.class)
  TranslationService redisTranslationService(
      TranslationProvider provider,
      StringRedisTemplate stringRedisTemplate,
      TranslationCacheProperties props) {
    log.debug("Register RedisTranslationService");
    return new RedisTranslationService(
        provider, stringRedisTemplate, props.getRedis().getTimeToLive());
  }
}
