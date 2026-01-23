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
import io.github.oiltea.trans4j.core.TranslateService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import tools.jackson.databind.BeanDescription.Supplier;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

/**
 * A custom Jackson serializer modifier that handles translation of annotated properties. This class
 * extends {@code ValueSerializerModifier} to intercept bean property serialization and apply
 * translation logic to properties marked with the {@link Translate} annotation. It replaces the
 * original property writer with a {@code TranslateJackson3PropertyWriter} that performs translation
 * based on the specified source property.
 *
 * <p>This modifier is typically used in internationalization scenarios where certain fields need to
 * be translated at serialization time, such as converting internal codes to human-readable
 * localized strings.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class TranslateJackson3BeanSerializerModifier extends ValueSerializerModifier {

  /**
   * The translation service used for text translation operations.
   *
   * <p>This service provides methods to translate text between different languages.
   *
   * @see TranslateService
   */
  private final TranslateService translateService;

  /**
   * Constructs a new TranslateJackson3BeanSerializerModifier with the specified translation
   * service. This modifier is used to customize JSON serialization behavior by integrating
   * translation capabilities.
   *
   * @param translateService the translation service to be used for serialization customization
   */
  public TranslateJackson3BeanSerializerModifier(TranslateService translateService) {
    this.translateService = translateService;
  }

  /**
   * Changes the list of bean property writers by processing properties annotated with
   * {@code @Translate}. For each property annotated with {@code @Translate}, this method replaces
   * the original writer with a {@link TranslateJackson3PropertyWriter} that handles translation
   * logic. The translation source property is determined by the {@code from} attribute of the
   * annotation. Properties without the annotation are added to the result list unchanged.
   *
   * @param config the serialization configuration
   * @param beanDesc a supplier providing the bean description
   * @param beanProperties the original list of bean property writers
   * @return a new list of bean property writers, with translated properties wrapped as necessary
   * @since 1.0
   */
  @Override
  public List<BeanPropertyWriter> changeProperties(
      SerializationConfig config, Supplier beanDesc, List<BeanPropertyWriter> beanProperties) {
    Map<String, BeanPropertyWriter> map =
        beanProperties.stream().collect(Collectors.toMap(BeanPropertyWriter::getName, w -> w));

    List<BeanPropertyWriter> writers = new ArrayList<>();
    for (BeanPropertyWriter writer : beanProperties) {
      Translate anno = writer.findAnnotation(Translate.class);
      if (anno != null) {
        writers.add(
            new TranslateJackson3PropertyWriter(
                translateService, writer, map.get(anno.from()), anno));
      } else {
        writers.add(writer);
      }
    }
    return writers;
  }
}
