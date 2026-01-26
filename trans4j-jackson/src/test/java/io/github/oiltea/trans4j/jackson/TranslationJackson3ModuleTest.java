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

package io.github.oiltea.trans4j.jackson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import io.github.oiltea.trans4j.core.DefaultTranslationService;
import io.github.oiltea.trans4j.core.TranslationProvider;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

class TranslationJackson3ModuleTest {

  private ObjectMapper mapper;
  private TranslationProvider provider;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslationProvider.class);
    when(provider.get("gender")).thenReturn(Map.of("1", "Male", "2", "Female"));
    when(provider.get("status")).thenReturn(Map.of("1", "Active", "2", "Inactive"));

    mapper =
        JsonMapper.builder()
            .addModule(new TranslationJackson3Module(new DefaultTranslationService(provider)))
            .build();
  }

  @Test
  @DisplayName("Should translate gender '1' to 'Male' using NULL policy for genderText")
  void should_translate_gender_male() {
    UserDto userDto = new UserDto();
    userDto.setGender("1");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("1", jsonNode.get("gender").asString());
    assertEquals("Male", jsonNode.get("genderText").asString());
    verify(provider, times(1)).get("gender");
  }

  @Test
  @DisplayName("Should handle null source value: genderText -> null, statusText -> empty")
  void should_handle_null_source_value_by_policy() {
    UserDto userDto = new UserDto();

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("gender").isNull());
    assertTrue(jsonNode.get("genderText").isNull());
    assertTrue(jsonNode.get("status").isNull());
    assertEquals("", jsonNode.get("statusText").asString());

    verify(provider, never()).get(anyString());
  }
}
