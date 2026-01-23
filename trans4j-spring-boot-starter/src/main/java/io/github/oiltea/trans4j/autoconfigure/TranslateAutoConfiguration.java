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

package io.github.oiltea.trans4j.autoconfigure;

import io.github.oiltea.trans4j.core.DefaultTranslateService;
import io.github.oiltea.trans4j.core.SimpleTranslateService;
import io.github.oiltea.trans4j.core.TranslateCacheProperties;
import io.github.oiltea.trans4j.core.TranslateProvider;
import io.github.oiltea.trans4j.core.TranslateService;
import io.github.oiltea.trans4j.jackson.TranslateJackson2Module;
import io.github.oiltea.trans4j.jackson.TranslateJackson3Module;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for translation functionality in Spring Boot applications.
 *
 * <p>This class provides automatic configuration for translation services based on the presence of
 * a {@link TranslateProvider} bean and configuration properties. It conditionally creates different
 * types of {@link TranslateService} beans depending on the cache configuration, and registers
 * Jackson modules for JSON serialization/deserialization with translation support.
 *
 * <p>The configuration supports two cache types: "none" for no caching (default translation
 * service) and "simple" for simple in-memory caching (default if no cache type is specified).
 *
 * @author Oiltea
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(TranslateProvider.class)
@EnableConfigurationProperties(TranslateCacheProperties.class)
public class TranslateAutoConfiguration {

  /**
   * Creates a default TranslateService bean when the cache type is set to "none". This method is
   * conditionally enabled based on the configuration property "trans4j.cache.type". When the
   * property value is "none", this bean will be registered, providing a translation service without
   * caching capabilities.
   *
   * @param provider the translation provider used by the service
   * @return a new instance of DefaultTranslateService configured with the given provider
   * @since (version or date if applicable, otherwise omit)
   */
  @Bean
  @ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "none")
  public TranslateService defaultTranslateService(TranslateProvider provider) {
    return new DefaultTranslateService(provider);
  }

  /**
   * Creates a SimpleTranslateService bean when the cache type is set to "simple" or not specified.
   * This method is conditionally registered as a Spring bean based on the configuration property.
   * The SimpleTranslateService provides basic translation functionality with caching support.
   *
   * @param provider the translation provider used by the service
   * @return a configured instance of SimpleTranslateService
   * @since 1.0
   */
  @Bean
  @ConditionalOnProperty(
      prefix = "trans4j.cache",
      name = "type",
      havingValue = "simple",
      matchIfMissing = true)
  public TranslateService simpleTranslateService(TranslateProvider provider) {
    return new SimpleTranslateService(provider);
  }

  /**
   * Creates and configures a TranslateJackson2Module bean for JSON serialization/deserialization
   * with translation support. This module integrates with the provided TranslateService to enable
   * automatic field translation during the Jackson processing lifecycle.
   *
   * @param translateService the translation service used by the module to perform text translations
   * @return a configured instance of TranslateJackson2Module
   * @since (version or date if applicable, otherwise omit)
   */
  @Bean
  public TranslateJackson2Module translateJackson2Module(TranslateService translateService) {
    return new TranslateJackson2Module(translateService);
  }

  /**
   * Creates and configures a TranslateJackson3Module bean for JSON serialization/deserialization
   * with translation support. This bean integrates the provided {@link TranslateService} into the
   * Jackson ObjectMapper for automatic field translation.
   *
   * @param translateService the translation service used by the module to perform text translations
   * @return a configured instance of {@link TranslateJackson3Module}
   */
  @Bean
  public TranslateJackson3Module translateJackson3Module(TranslateService translateService) {
    return new TranslateJackson3Module(translateService);
  }
}
