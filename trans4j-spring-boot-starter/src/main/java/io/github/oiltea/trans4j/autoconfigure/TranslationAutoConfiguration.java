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

package io.github.oiltea.trans4j.autoconfigure;

import io.github.oiltea.trans4j.core.DefaultTranslationService;
import io.github.oiltea.trans4j.core.SimpleTranslationService;
import io.github.oiltea.trans4j.core.TranslationCacheProperties;
import io.github.oiltea.trans4j.core.TranslationProvider;
import io.github.oiltea.trans4j.core.TranslationService;
import io.github.oiltea.trans4j.jackson.TranslationJackson2Module;
import io.github.oiltea.trans4j.jackson.TranslationJackson3Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for translation functionality in Spring Boot applications.
 *
 * <p>This class provides automatic configuration for translation services based on the presence of
 * a {@link TranslationProvider} bean and configuration properties. It conditionally creates
 * different types of {@link TranslationService} beans depending on the cache configuration, and
 * registers Jackson modules for JSON serialization/deserialization with translation support.
 *
 * <p>The configuration supports two cache types: "none" for no caching (default translation
 * service) and "simple" for simple in-memory caching (default if no cache type is specified).
 *
 * @author Oiltea
 * @since 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(TranslationProvider.class)
@EnableConfigurationProperties(TranslationCacheProperties.class)
public class TranslationAutoConfiguration {

  /**
   * Creates a default TranslationService bean when the cache type is set to "none". This method is
   * conditionally enabled based on the configuration property "trans4j.cache.type". When the
   * property value is "none", this bean will be registered, providing a translation service without
   * caching capabilities.
   *
   * @param provider the translation provider used by the service
   * @return a new instance of DefaultTranslationService configured with the given provider
   * @since 1.0.0
   */
  @Bean
  @ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "none")
  public TranslationService defaultTranslationService(TranslationProvider provider) {
    log.debug("Register DefaultTranslationService");
    return new DefaultTranslationService(provider);
  }

  /**
   * Creates a SimpleTranslationService bean when the cache type is set to "simple" or not
   * specified. This method is conditionally registered as a Spring bean based on the configuration
   * property. The SimpleTranslationService provides basic translation functionality with caching
   * support.
   *
   * @param provider the translation provider used by the service
   * @return a configured instance of SimpleTranslationService
   * @since 1.0.0
   */
  @Bean
  @ConditionalOnProperty(
      prefix = "trans4j.cache",
      name = "type",
      havingValue = "simple",
      matchIfMissing = true)
  public TranslationService simpleTranslationService(TranslationProvider provider) {
    log.debug("Register SimpleTranslationService");
    return new SimpleTranslationService(provider);
  }

  /**
   * Configuration class for Jackson2 integration with translation support.
   *
   * <p>This configuration is conditionally loaded when the Jackson ObjectMapper class is available
   * on the classpath. It provides a custom Jackson module for handling translation operations
   * during JSON serialization/deserialization.
   *
   * @author Oiltea
   * @since 1.0.0
   */
  @Slf4j
  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
  static class Jackson2Configuration {

    /**
     * Creates and configures a {@link TranslationJackson2Module} bean for Jackson JSON
     * serialization/deserialization with translation support. This module is typically used to
     * integrate translation capabilities into the Jackson ObjectMapper, enabling automatic
     * translation of specific fields during serialization or deserialization processes.
     *
     * @param translationService the translation service used by the module to perform translations
     * @return a configured instance of {@link TranslationJackson2Module}
     */
    @Bean
    TranslationJackson2Module translationJackson2Module(TranslationService translationService) {
      log.debug("Register TranslationJackson2Module");
      return new TranslationJackson2Module(translationService);
    }
  }

  /**
   * Configuration class for Jackson3 integration with translation support.
   *
   * <p>This configuration is conditionally loaded when the Jackson ObjectMapper class is available
   * on the classpath. It provides a custom Jackson module for handling translation operations
   * during JSON serialization/deserialization.
   *
   * @author Oiltea
   * @since 1.0.0
   */
  @Slf4j
  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(name = "tools.jackson.databind.ObjectMapper")
  static class Jackson3Configuration {

    /**
     * Creates and configures a {@link TranslationJackson3Module} bean for Jackson JSON
     * serialization/deserialization with translation support. This module is typically used to
     * integrate translation capabilities into the Jackson ObjectMapper, enabling automatic
     * translation of specific fields during serialization or deserialization processes.
     *
     * @param translationService the translation service used by the module to perform translations
     * @return a configured instance of {@link TranslationJackson3Module}
     */
    @Bean
    TranslationJackson3Module translationJackson3Module(TranslationService translationService) {
      log.debug("Register TranslationJackson3Module");
      return new TranslationJackson3Module(translationService);
    }
  }
}
