///*
// * Copyright © 2026 oiltea
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package io.github.oiltea.trans4j.autoconfigure;
//
//import io.github.oiltea.trans4j.core.TranslateProvider;
//import io.github.oiltea.trans4j.core.handler.TransHandlerRegistry;
//import io.github.oiltea.trans4j.core.DictService;
//import io.github.oiltea.trans4j.core.DictSource;
//import io.github.oiltea.trans4j.core.cache.DictCache;
//import io.github.oiltea.trans4j.core.cache.InMemoryDictCache;
//import io.github.oiltea.trans4j.core.handler.DictTransHandler;
//import io.github.oiltea.trans4j.core.impl.DefaultDictService;
//import java.util.List;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration(proxyBeanMethods = false)
//@ConditionalOnBean(DictSource.class)
//public class DictAutoConfiguration {
//
//  @Bean
//  @ConditionalOnMissingBean
//  public DictCache dictCache() {
//    return new InMemoryDictCache();
//  }
//
//  @Bean
//  @ConditionalOnMissingBean
//  public DictService dictService(DictSource dictSource, DictCache dictCache) {
//    return new DefaultDictService(dictSource, dictCache);
//  }
//
//  @Bean
//  public DictTransHandler dictTransHandler(DictService dictService, TransHandlerRegistry registry) {
//    DictTransHandler handler = new DictTransHandler(dictService);
//    registry.register(handler);
//    return handler;
//  }
//
//  @Bean
//  public List<TranslateProvider> translateProviders(List<TranslateProvider> providers) {
//    return providers.stream()
//        .map(CaffeineCachingTranslateProvider::new) // ⭐ 自动加缓存
//        .toList();
//  }
//}
