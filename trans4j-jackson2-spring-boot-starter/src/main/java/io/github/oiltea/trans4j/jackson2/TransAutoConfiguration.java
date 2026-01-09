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
package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oiltea.trans4j.core.trans.DefaultTransService;
import io.github.oiltea.trans4j.core.trans.TransDictProvider;
import io.github.oiltea.trans4j.core.trans.TransService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
public class TransAutoConfiguration {

  @Bean
  @ConditionalOnBean(TransDictProvider.class)
  TransService transService(TransDictProvider transDictProvider) {
    return new DefaultTransService(transDictProvider);
  }

  @Bean
  @ConditionalOnBean(TransService.class)
  TransJacksonModule transJacksonModule(TransService transService) {
    return new TransJacksonModule(transService);
  }
}