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

import io.github.oiltea.trans4j.core.TranslationProvider;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@SuppressWarnings("unchecked")
class RedisTranslationServiceTest {
  private TranslationProvider provider;
  private StringRedisTemplate redis;
  private HashOperations ops;
  private RedisTranslationService service;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslationProvider.class);

    redis = Mockito.mock(StringRedisTemplate.class);
    ops = Mockito.mock(HashOperations.class);

    Mockito.when(redis.opsForHash()).thenReturn(ops);

    service = new RedisTranslationService(provider, redis, Duration.ofMinutes(10));
  }

  @Test
  void should_return_from_redis_when_hit() {
    Mockito.when(ops.get("trans4j:gender", "1")).thenReturn("male");

    String result = service.translate("gender", "1");

    Assertions.assertEquals("male", result);

    Mockito.verify(provider, Mockito.never()).get(Mockito.any());
    Mockito.verify(ops, Mockito.never()).putAll(Mockito.any(), Mockito.any());
    Mockito.verify(redis, Mockito.never()).expire(Mockito.any(), Mockito.any());
  }

  @Test
  void should_load_from_provider_and_put_to_redis_when_miss() {
    Mockito.when(ops.get("trans4j:gender", "1")).thenReturn(null);
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male", "2", "female"));

    String result = service.translate("gender", "1");

    Assertions.assertEquals("male", result);

    Mockito.verify(provider, Mockito.times(1)).get("gender");
    Mockito.verify(ops).putAll("trans4j:gender", Map.of("1", "male", "2", "female"));
    Mockito.verify(redis).expire(Mockito.eq("trans4j:gender"), Mockito.any(Duration.class));
  }

  @Test
  void should_return_null_when_provider_returns_empty() {
    Mockito.when(ops.get("trans4j:gender", "1")).thenReturn(null);
    Mockito.when(provider.get("gender")).thenReturn(Map.of());

    String result = service.translate("gender", "1");

    Assertions.assertNull(result);
  }

  @Test
  void should_return_null_when_value_not_found() {
    Mockito.when(ops.get("trans4j:gender", "3")).thenReturn(null);
    Mockito.when(provider.get("gender")).thenReturn(Map.of("1", "male"));

    String result = service.translate("gender", "3");

    Assertions.assertNull(result);
  }
}
