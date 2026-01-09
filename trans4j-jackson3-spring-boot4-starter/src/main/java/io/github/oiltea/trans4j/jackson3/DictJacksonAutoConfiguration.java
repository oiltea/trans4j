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
package io.github.oiltea.trans4j.jackson3;

import io.github.oiltea.trans4j.core.cache.DictCache;
import io.github.oiltea.trans4j.core.cache.InMemoryDictCache;
import io.github.oiltea.trans4j.core.resolver.DefaultDictResolver;
import io.github.oiltea.trans4j.core.resolver.DictResolver;
import io.github.oiltea.trans4j.core.source.DictSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class DictJacksonAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  DictCache dictCache() {
    return new InMemoryDictCache();
  }

  @Bean
  @ConditionalOnMissingBean
  DictResolver dictResolver(DictSource dictSource, DictCache dictCache) {
    return new DefaultDictResolver(dictSource, dictCache);
  }

  @Bean
  @ConditionalOnMissingBean
  DictJacksonModule dictJacksonModule(DictResolver dictResolver) {
    return new DictJacksonModule(dictResolver);
  }
}