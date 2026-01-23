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

package io.github.oiltea.trans4j.jackson;

import io.github.oiltea.trans4j.core.SimpleTranslateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class TranslateJackson3ModuleTest {

  ObjectMapper mapper;

  @BeforeEach
  void init() {
    CustomTranslateProvider dictionaryProvider = new CustomTranslateProvider();
    SimpleTranslateService translateService = new SimpleTranslateService(dictionaryProvider);
    TranslateJackson3Module module = new TranslateJackson3Module(translateService);

    mapper = JsonMapper.builder().addModule(module).build();
  }

  @Test
  void should_translate_field_when_serializing() {
    UserVO vo = new UserVO();
    vo.setGenger("1");

    String json = mapper.writeValueAsString(vo);

    System.out.println(json);

    Assertions.assertTrue(json.contains("\"genger\":\"1\""));
    Assertions.assertTrue(json.contains("\"gengerText\":\"男\""));
  }

  @Test
  void should_not_affect_normal_fields() {
    UserVO vo = new UserVO();
    vo.setGenger("2");

    String json = mapper.writeValueAsString(vo);

    Assertions.assertTrue(json.contains("\"genger\":\"2\""));
  }

  @Test
  void should_skip_when_from_value_null() {
    UserVO vo = new UserVO();
    vo.setGenger(null);

    String json = mapper.writeValueAsString(vo);

    Assertions.assertTrue(json.contains("\"genger\":null"));
  }
}
