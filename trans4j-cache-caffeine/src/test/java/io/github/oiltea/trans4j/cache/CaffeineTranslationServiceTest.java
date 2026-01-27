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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.oiltea.trans4j.core.TranslationProvider;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CaffeineTranslationServiceTest {

  private TranslationProvider provider;
  private CaffeineTranslationService service;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslationProvider.class);
    service = new CaffeineTranslationService(provider, "maximumSize=100");
  }

  @Test
  void should_load_from_provider_when_cache_miss() {
    when(provider.get("gender")).thenReturn(Map.of("1", "male"));
    Assertions.assertEquals("male", service.translate("gender", "1"));
    verify(provider, times(1)).get("gender");
  }

  @Test
  void should_hit_cache_and_not_call_provider_again() {
    when(provider.get("gender")).thenReturn(Map.of("1", "male"));

    service.translate("gender", "1");
    service.translate("gender", "1");

    verify(provider, times(1)).get("gender");
  }

  @Test
  void should_return_null_when_value_not_found() {
    when(provider.get("gender")).thenReturn(Map.of("1", "male"));
    assertNull(service.translate("gender", "2"));
  }

  @Test
  void should_return_null_when_provider_returns_null() {
    when(provider.get("status")).thenReturn(null);
    assertNull(service.translate("status", "1"));
    verify(provider, times(1)).get("status");
  }
}
