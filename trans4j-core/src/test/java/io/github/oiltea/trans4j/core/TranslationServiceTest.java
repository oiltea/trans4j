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

package io.github.oiltea.trans4j.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TranslationServiceTest {

  private TranslationProvider provider;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslationProvider.class);
    when(provider.get("gender")).thenReturn(Map.of("1", "Male", "2", "Female"));
    when(provider.get("status")).thenReturn(Map.of("1", "Active", "2", "Inactive"));
  }

  @Nested
  @DisplayName("DefaultTranslationService Tests")
  class DefaultTranslationServiceTests {

    private DefaultTranslationService service;

    @BeforeEach
    void setUp() {
      service = new DefaultTranslationService(provider);
    }

    @Test
    @DisplayName("Should return null when provider return null")
    void should_return_null_when_provider_return_null() {
      when(provider.get("gender")).thenReturn(null);
      assertNull(service.translate("gender", "1"));
      verify(provider, times(1)).get("gender");
      reset(provider);
    }

    @Test
    @DisplayName("Should return null when value is null")
    void should_return_null_when_value_is_null() {
      assertNull(service.translate("gender", null));
      verify(provider, never()).get("gender");
    }

    @Test
    @DisplayName("Should translate gender value correctly")
    void should_translate_gender_value() {
      assertEquals("Male", service.translate("gender", "1"));
      verify(provider, times(1)).get("gender");
    }

    @Test
    @DisplayName("Should return null for unmapped value")
    void should_return_null_for_unmapped_value() {
      assertNull(service.translate("gender", "0"));
      verify(provider, times(1)).get("gender");
    }

    @Test
    @DisplayName("Should call provider every time for same key")
    void should_call_provider_every_time() {
      service.translate("gender", "1");
      service.translate("gender", "2");
      service.translate("gender", "1");

      // Provider should be called 3 times (no caching)
      verify(provider, times(3)).get("gender");
    }

    @Test
    @DisplayName("Should handle multiple different keys")
    void should_handle_multiple_keys() {
      assertEquals("Male", service.translate("gender", "1"));
      assertEquals("Active", service.translate("status", "1"));

      verify(provider, times(1)).get("gender");
      verify(provider, times(1)).get("status");
    }
  }

  @Nested
  @DisplayName("SimpleTranslationService Tests")
  class SimpleTranslationServiceTests {

    private SimpleTranslationService service;

    @BeforeEach
    void setUp() {
      service = new SimpleTranslationService(provider);
    }

    @Test
    @DisplayName("Should translate gender value correctly")
    void should_translate_gender_value() {
      assertEquals("Male", service.translate("gender", "1"));
      verify(provider, times(1)).get("gender");
    }

    @Test
    @DisplayName("Should translate status value correctly")
    void should_translate_status_value() {
      assertEquals("Active", service.translate("status", "1"));
      verify(provider, times(1)).get("status");
    }

    @Test
    @DisplayName("Should return null for unmapped value")
    void should_return_null_for_unmapped_value() {
      assertNull(service.translate("gender", "0"));
    }

    @Test
    @DisplayName("Should cache translation map for same key")
    void should_cache_translation_map() {
      service.translate("gender", "1");
      service.translate("gender", "2");
      service.translate("gender", "1");

      // Provider should be called only once (caching enabled)
      verify(provider, times(1)).get("gender");
    }

    @Test
    @DisplayName("Should cache independently for different keys")
    void should_cache_independently_for_different_keys() {
      service.translate("gender", "1");
      service.translate("gender", "2");
      service.translate("status", "1");
      service.translate("status", "2");

      // Each key should be fetched only once
      verify(provider, times(1)).get("gender");
      verify(provider, times(1)).get("status");
    }

    @Test
    @DisplayName("Should handle multiple different keys")
    void should_handle_multiple_keys() {
      assertEquals("Male", service.translate("gender", "1"));
      assertEquals("Active", service.translate("status", "1"));

      verify(provider, times(1)).get("gender");
      verify(provider, times(1)).get("status");
    }

    @Test
    @DisplayName("Should be thread-safe with concurrent access")
    void should_be_thread_safe() throws InterruptedException {
      int threadCount = 10;
      Thread[] threads = new Thread[threadCount];

      for (int i = 0; i < threadCount; i++) {
        threads[i] =
            new Thread(
                () -> {
                  for (int j = 0; j < 100; j++) {
                    service.translate("gender", "1");
                    service.translate("status", "1");
                  }
                });
        threads[i].start();
      }

      for (Thread thread : threads) {
        thread.join();
      }

      // Provider should be called only once per key despite concurrent access
      verify(provider, times(1)).get("gender");
      verify(provider, times(1)).get("status");
    }
  }

  @Nested
  @DisplayName("Comparison Tests")
  class ComparisonTests {

    @Test
    @DisplayName("DefaultTranslationService should not cache provider calls")
    void default_service_should_not_cache() {
      DefaultTranslationService defaultService = new DefaultTranslationService(provider);

      defaultService.translate("gender", "1");
      defaultService.translate("gender", "1");
      defaultService.translate("gender", "1");

      verify(provider, times(3)).get("gender");
    }

    @Test
    @DisplayName("SimpleTranslationService should cache provider calls")
    void simple_service_should_cache() {
      SimpleTranslationService simpleService = new SimpleTranslationService(provider);

      simpleService.translate("gender", "1");
      simpleService.translate("gender", "1");
      simpleService.translate("gender", "1");

      verify(provider, times(1)).get("gender");
    }

    @Test
    @DisplayName("Both services should return same translation results")
    void both_services_should_return_same_results() {
      DefaultTranslationService defaultService = new DefaultTranslationService(provider);
      SimpleTranslationService simpleService = new SimpleTranslationService(provider);

      assertEquals(defaultService.translate("gender", "1"), simpleService.translate("gender", "1"));
      assertEquals(defaultService.translate("status", "1"), simpleService.translate("status", "1"));
      assertEquals(defaultService.translate("gender", "0"), simpleService.translate("gender", "0"));
    }
  }
}
