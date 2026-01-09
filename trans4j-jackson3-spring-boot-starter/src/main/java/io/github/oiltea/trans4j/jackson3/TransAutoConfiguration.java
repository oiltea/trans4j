package io.github.oiltea.trans4j.jackson3;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TransAutoConfiguration {

  @Bean
  TransJacksonModule transJacksonModule() {
    return new TransJacksonModule();
  }

}