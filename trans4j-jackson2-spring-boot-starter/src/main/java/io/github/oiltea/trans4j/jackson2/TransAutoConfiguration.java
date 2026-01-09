package io.github.oiltea.trans4j.jackson2;

import io.github.oiltea.trans4j.core.trans.Converter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TransAutoConfiguration {

  @Bean
  TransJacksonModule transJacksonModule(Converter converter) {
    return new TransJacksonModule(converter);
  }

}