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
package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import io.github.oiltea.trans4j.core.TranslateService;
import io.github.oiltea.trans4j.core.Translate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslateBeanSerializerModifier extends BeanSerializerModifier {

  private final TranslateService translateService;

  public TranslateBeanSerializerModifier(TranslateService translateService) {
    this.translateService = translateService;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(
      SerializationConfig config,
      BeanDescription beanDesc,
      List<BeanPropertyWriter> beanProperties) {
    Map<String, BeanPropertyWriter> map =
        beanProperties.stream().collect(Collectors.toMap(BeanPropertyWriter::getName, w -> w));

    List<BeanPropertyWriter> writers = new ArrayList<>();
    for (BeanPropertyWriter writer : beanProperties) {
      Translate anno = writer.getAnnotation(Translate.class);
      if (anno != null) {
        writers.add(new TranslatePropertyWriter(translateService, map.get(anno.from()), anno));
      } else {
        writers.add(writer);
      }
    }
    return writers;
  }
}
