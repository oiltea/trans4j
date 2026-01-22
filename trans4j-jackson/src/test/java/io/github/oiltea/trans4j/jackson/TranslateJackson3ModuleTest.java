package io.github.oiltea.trans4j.jackson;

import io.github.oiltea.trans4j.core.DafaultTranslateService;
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
    DafaultTranslateService translateService = new DafaultTranslateService(dictionaryProvider);
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
