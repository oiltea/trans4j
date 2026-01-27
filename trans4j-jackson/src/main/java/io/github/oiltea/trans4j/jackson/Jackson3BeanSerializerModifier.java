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

import io.github.oiltea.trans4j.core.Translate;
import io.github.oiltea.trans4j.core.TranslationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tools.jackson.databind.BeanDescription.Supplier;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

/**
 * Custom Jackson serializer modifier that handles translation of annotated properties.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class Jackson3BeanSerializerModifier extends ValueSerializerModifier {

  private final TranslationService translationService;

  public Jackson3BeanSerializerModifier(TranslationService translationService) {
    this.translationService = translationService;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(
      SerializationConfig config, Supplier beanDesc, List<BeanPropertyWriter> beanProperties) {
    Map<String, BeanPropertyWriter> map =
        beanProperties.stream().collect(Collectors.toMap(BeanPropertyWriter::getName, w -> w));

    List<BeanPropertyWriter> writers = new ArrayList<>();
    for (BeanPropertyWriter writer : beanProperties) {
      Translate anno = writer.findAnnotation(Translate.class);
      if (anno == null) {
        writers.add(writer);
      } else {
        BeanPropertyWriter fromWriter = map.get(anno.from());
        writers.add(new Jackson3PropertyWriter(translationService, writer, fromWriter, anno));
      }
    }
    return writers;
  }
}
