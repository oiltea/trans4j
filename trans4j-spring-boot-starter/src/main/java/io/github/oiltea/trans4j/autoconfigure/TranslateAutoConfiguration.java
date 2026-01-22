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
import io.github.oiltea.trans4j.core.TranslateCacheProperties;
import io.github.oiltea.trans4j.core.TranslateProvider;
import io.github.oiltea.trans4j.core.TranslateService;
import io.github.oiltea.trans4j.jackson.TranslateJackson3Module;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TranslateCacheProperties.class)
public class TranslateAutoConfiguration {

  @Bean
  public TranslateJackson3Module translateJacksonModule(TranslateService service) {
    return new TranslateJackson3Module(service);
  }

  @Bean
  @ConditionalOnProperty(prefix = "trans4j.cache", name = "type", havingValue = "none")
  public TranslateService noCacheService(TranslateProvider provider) {
    return new DefaultTranslateService(provider);
  }
}
