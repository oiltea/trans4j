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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.oiltea.trans4j.core.DefaultTranslationService;
import io.github.oiltea.trans4j.core.TranslationProvider;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TranslationJackson2ModuleTest {

  private ObjectMapper mapper;
  private TranslationProvider provider;

  @BeforeEach
  void setUp() {
    provider = Mockito.mock(TranslationProvider.class);
    when(provider.get("gender")).thenReturn(Map.of("1", "Male", "2", "Female"));
    when(provider.get("status")).thenReturn(Map.of("1", "Active", "2", "Inactive"));

    mapper =
        JsonMapper.builder()
            .addModule(new TranslationJackson2Module(new DefaultTranslationService(provider)))
            .build();
  }

  @Test
  @DisplayName("genderText -> Male, statusText -> Inactive")
  void normal_translate() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setGender("1");
    userDto.setStatus("2");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("Male", jsonNode.get("genderText").asText());
    assertEquals("Inactive", jsonNode.get("statusText").asText());
    verify(provider, times(1)).get("gender");
    verify(provider, times(1)).get("status");
  }

  @Test
  @DisplayName("genderText -> null, statusText -> empty")
  void should_handle_null_policy() throws Exception {
    UserDto userDto = new UserDto();

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("genderText").isNull());
    assertEquals("", jsonNode.get("statusText").asText());

    verify(provider, never()).get(anyString());
  }

  @Test
  @DisplayName("genderText -> null, statusText -> empty")
  void should_handle_null_policy_when_provider_return_null() throws Exception {
    reset(provider);

    UserDto userDto = new UserDto();
    userDto.setGender("0");
    userDto.setStatus("0");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("genderText").isNull());
    assertEquals("", jsonNode.get("statusText").asText());

    verify(provider, times(1)).get("gender");
    verify(provider, times(1)).get("status");
  }
}
