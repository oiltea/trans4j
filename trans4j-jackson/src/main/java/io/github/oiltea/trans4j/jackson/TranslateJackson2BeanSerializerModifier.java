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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import io.github.oiltea.trans4j.core.Translate;
import io.github.oiltea.trans4j.core.TranslateService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom Jackson serializer modifier that handles translation of annotated properties. This class
 * extends {@link BeanSerializerModifier} to intercept property serialization and apply translation
 * logic based on the {@link Translate} annotation. When a property is annotated with {@link
 * Translate}, it replaces the original property writer with a custom {@link
 * TranslateJackson2PropertyWriter} that performs translation using the provided {@link
 * TranslateService}.
 *
 * <p>The modifier scans all bean properties during serialization configuration, identifies
 * properties marked with {@link Translate}, and wraps them to enable dynamic translation of field
 * values from a source field (specified by {@link Translate#from()}) to the target property.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class TranslateJackson2BeanSerializerModifier extends BeanSerializerModifier {

  /**
   * The translation service used for text translation operations.
   *
   * <p>This service provides methods to translate text between different languages.
   *
   * @see TranslateService
   */
  private final TranslateService translateService;

  /**
   * Constructs a new TranslateJackson2BeanSerializerModifier with the specified translation
   * service. This modifier is used to customize the serialization process by integrating
   * translation capabilities.
   *
   * @param translateService the translation service to be used for translating serialized content
   */
  public TranslateJackson2BeanSerializerModifier(TranslateService translateService) {
    this.translateService = translateService;
  }

  /**
   * Changes the list of bean property writers by processing properties annotated with {@link
   * Translate}. For each property annotated with {@link Translate}, a custom {@link
   * TranslateJackson2PropertyWriter} is created and added to the result list. Other properties are
   * added as-is.
   *
   * @param config the serialization configuration
   * @param beanDesc the bean description
   * @param beanProperties the original list of bean property writers
   * @return a new list of bean property writers with translated properties replaced
   */
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
        writers.add(
            new TranslateJackson2PropertyWriter(translateService, map.get(anno.from()), anno));
      } else {
        writers.add(writer);
      }
    }
    return writers;
  }
}
