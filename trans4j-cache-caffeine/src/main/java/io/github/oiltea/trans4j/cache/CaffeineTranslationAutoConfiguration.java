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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for Caffeine-based translation caching.
 *
 * <p>This configuration is activated when the property {@code trans4j.cache.type} is set to
 * "caffeine". It provides a {@link TranslationService} bean that uses Caffeine as the underlying
 * cache implementation for caching translation data, improving performance by reducing calls to the
 * underlying {@link TranslationProvider}.
 *
 * @author Oiltea
 * @version 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "caffeine")
public class CaffeineTranslationAutoConfiguration {

  /**
   * Creates a TranslationService bean using Caffeine cache implementation. This bean is only
   * created if a TranslationProvider bean is available in the application context. The service
   * wraps the provided TranslationProvider with a caching layer configured according to the given
   * Caffeine specification from the properties.
   *
   * @param provider the underlying translation provider to be cached
   * @param props the properties containing the Caffeine cache specification
   * @return a new instance of CaffeineTranslationService configured with the given provider and
   *     cache spec
   * @since (version or date if applicable, otherwise omit)
   */
  @Bean
  @ConditionalOnBean(TranslationProvider.class)
  TranslationService caffeineTranslationService(
      TranslationProvider provider, TranslationCacheProperties props) {
    log.debug("Register CaffeineTranslationService");
    return new CaffeineTranslationService(provider, props.getCaffeine().getSpec());
  }
}
