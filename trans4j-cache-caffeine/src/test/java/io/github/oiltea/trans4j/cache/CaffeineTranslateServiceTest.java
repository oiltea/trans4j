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

package io.github.oiltea.trans4j.cache;

import io.github.oiltea.trans4j.core.TranslateProvider;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CaffeineTranslateServiceTest {

  private TranslateProvider provider;
  private CaffeineTranslateService service;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslateProvider.class);
    service = new CaffeineTranslateService(provider, "maximumSize=100");
  }

  @Test
  void should_load_from_provider_when_cache_miss() {
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male"));

    String result = service.translate("gender", "1");

    Assertions.assertEquals("male", result);

    Mockito.verify(provider, Mockito.times(1)).get("gender");
  }

  @Test
  void should_hit_cache_and_not_call_provider_again() {
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male"));

    service.translate("gender", "1");
    service.translate("gender", "1");

    Mockito.verify(provider, Mockito.times(1)).get("gender");
  }

  @Test
  void should_return_null_when_value_not_found() {
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male"));

    String result = service.translate("gender", "2");

    Assertions.assertNull(result);
  }

  @Test
  void should_return_null_when_provider_returns_empty() {
    Mockito.when(provider.get("gender")).thenReturn(Map.of());

    String result = service.translate("gender", "1");

    Assertions.assertNull(result);
  }

  @Test
  void should_cache_per_key_independently() {
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male"));
    Mockito.when(provider.get("status")).thenReturn(Map.of("1", "enabled"));

    service.translate("gender", "1");
    service.translate("status", "1");

    Mockito.verify(provider, Mockito.times(1)).get("gender");
    Mockito.verify(provider, Mockito.times(1)).get("status");
  }
}
