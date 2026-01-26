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
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    when(provider.get("status")).thenReturn(Map.of("active", "Active", "inactive", "Inactive"));
    when(provider.get("role")).thenReturn(Map.of("admin", "Administrator", "user", "Regular User"));

    mapper =
        JsonMapper.builder()
            .addModule(new TranslationJackson2Module(new DefaultTranslationService(provider)))
            .build();
  }

  @Test
  @DisplayName("Should translate gender field with value '1' to 'Male'")
  void should_translate_gender_male() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setGender("1");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("1", jsonNode.get("gender").asText());
    assertEquals("Male", jsonNode.get("genderText").asText());
    verify(provider, times(1)).get("gender");
  }

  @Test
  @DisplayName("Should translate gender field with value '2' to 'Female'")
  void should_translate_gender_female() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(2L);
    userDto.setGender("2");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("2", jsonNode.get("gender").asText());
    assertEquals("Female", jsonNode.get("genderText").asText());
    verify(provider, times(1)).get("gender");
  }

  @Test
  @DisplayName("Should handle null gender value gracefully")
  void should_handle_null_gender() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(3L);
    userDto.setGender(null);

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("gender").isNull());
    assertTrue(jsonNode.get("genderText").isNull());
  }

  @Test
  @DisplayName("Should handle unmapped gender value with NULL handler")
  void should_handle_unmapped_gender_value() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(4L);
    userDto.setGender("3");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("3", jsonNode.get("gender").asText());
    assertTrue(jsonNode.get("genderText").isNull());
  }

  @Test
  @DisplayName("Should serialize all fields correctly")
  void should_serialize_all_fields() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(5L);
    userDto.setGender("1");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertNotNull(jsonNode.get("id"));
    assertEquals(5L, jsonNode.get("id").asLong());
    assertNotNull(jsonNode.get("gender"));
    assertNotNull(jsonNode.get("genderText"));
  }

  @Test
  @DisplayName("Should not call provider when gender is null")
  void should_not_call_provider_for_null_value() throws JsonProcessingException {
    reset(provider);
    when(provider.get("gender")).thenReturn(Map.of("1", "Male", "2", "Female"));

    UserDto userDto = new UserDto();
    userDto.setId(6L);
    userDto.setGender(null);

    mapper.writeValueAsString(userDto);

    verify(provider, never()).get(anyString());
  }

  @Test
  @DisplayName("Should return empty string on failure with EMPTY_STRING handler")
  void should_return_empty_string_on_failure() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(7L);
    userDto.setStatus("unknown");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("unknown", jsonNode.get("status").asText());
    assertEquals("", jsonNode.get("statusText").asText());
  }

  @Test
  @DisplayName("Should translate successfully with EMPTY_STRING handler")
  void should_translate_with_empty_string_handler() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(8L);
    userDto.setStatus("active");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("active", jsonNode.get("status").asText());
    assertEquals("Active", jsonNode.get("statusText").asText());
  }

  @Test
  @DisplayName("Should handle null with EMPTY_STRING handler")
  void should_handle_null_with_empty_string_handler() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(9L);
    userDto.setStatus(null);

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("status").isNull());
    assertEquals("", jsonNode.get("statusText").asText());
  }

  @Test
  @DisplayName("Should return original value on failure with ORIGINAL_VALUE handler")
  void should_return_original_value_on_failure() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(10L);
    userDto.setRole("superadmin");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("superadmin", jsonNode.get("role").asText());
    assertEquals("superadmin", jsonNode.get("roleText").asText());
  }

  @Test
  @DisplayName("Should translate successfully with ORIGINAL_VALUE handler")
  void should_translate_with_original_value_handler() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(11L);
    userDto.setRole("admin");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("admin", jsonNode.get("role").asText());
    assertEquals("Administrator", jsonNode.get("roleText").asText());
  }

  @Test
  @DisplayName("Should handle null with ORIGINAL_VALUE handler")
  void should_handle_null_with_original_value_handler() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(16L);
    userDto.setRole(null);

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertTrue(jsonNode.get("role").isNull());
    assertTrue(jsonNode.get("roleText").isNull());
  }

  @Test
  @DisplayName("Should handle multiple translations in single object")
  void should_handle_multiple_translations() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(12L);
    userDto.setGender("1");
    userDto.setStatus("active");
    userDto.setRole("admin");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("Male", jsonNode.get("genderText").asText());
    assertEquals("Active", jsonNode.get("statusText").asText());
    assertEquals("Administrator", jsonNode.get("roleText").asText());
  }

  @Test
  @DisplayName("Should handle mixed success and failure translations")
  void should_handle_mixed_translations() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setId(13L);
    userDto.setGender("1");
    userDto.setStatus("unknown");
    userDto.setRole("superadmin");

    String json = mapper.writeValueAsString(userDto);
    JsonNode jsonNode = mapper.readTree(json);

    assertEquals("Male", jsonNode.get("genderText").asText());
    assertEquals("", jsonNode.get("statusText").asText());
    assertEquals("superadmin", jsonNode.get("roleText").asText());
  }
}
