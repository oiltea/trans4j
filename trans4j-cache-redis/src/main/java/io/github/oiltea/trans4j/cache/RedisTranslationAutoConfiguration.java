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
 * @author Oiltea
 * @since 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "redis")
public class RedisTranslationAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(StringRedisTemplate.class)
  @ConditionalOnBean(RedisConnectionFactory.class)
  StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    log.debug("Register StringRedisTemplate for RedisTranslationService");
    return new StringRedisTemplate(connectionFactory);
  }

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
