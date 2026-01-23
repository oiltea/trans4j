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
import java.util.Objects;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.BeanPropertyWriter;

/**
 * A custom Jackson property writer that translates property values during JSON serialization. This
 * class extends {@code BeanPropertyWriter} to intercept the serialization of specific properties,
 * translating their values using a {@link TranslateService} before writing them to the JSON output.
 * It is designed for use in scenarios where property values need to be localized or transformed
 * based on a translation key before being serialized.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class TranslateJackson3PropertyWriter extends BeanPropertyWriter {

  /**
   * The translation service used for text translation operations.
   *
   * <p>This service provides functionality for translating text between different languages.
   *
   * @see TranslateService
   */
  private final TranslateService translateService;

  /** The property writer used for serialization from the source object. */
  private final BeanPropertyWriter fromWriter;

  /**
   * The translation service instance used for text translation operations.
   *
   * <p>This field holds a reference to the translation service that provides functionality for
   * translating text between different languages.
   *
   * @see Translate
   */
  private final Translate translate;

  /**
   * Constructs a TranslateJackson3PropertyWriter with the specified translation service and
   * property writers. This writer is used to serialize properties with translation support,
   * delegating to the original writer for serialization while applying translation logic based on
   * the provided {@link Translate} annotation.
   *
   * @param translateService the service used to perform translations
   * @param writer the original property writer to delegate serialization to
   * @param fromWriter the property writer for the source field from which translation is performed
   * @param translate the annotation containing translation configuration
   */
  protected TranslateJackson3PropertyWriter(
      TranslateService translateService,
      BeanPropertyWriter writer,
      BeanPropertyWriter fromWriter,
      Translate translate) {
    super(writer);
    this.translateService = translateService;
    this.fromWriter = fromWriter;
    this.translate = translate;
  }

  /**
   * Serializes the property value of the specified bean as a JSON property.
   *
   * <p>If the value obtained from the bean is not null, it is translated using the translation
   * service and then written as a string property with the appropriate name to the JSON generator.
   *
   * @param bean the object from which to obtain the property value
   * @param g the JSON generator used to write the output
   * @param ctxt the serialization context
   * @throws Exception if an error occurs during serialization or translation
   */
  @Override
  public void serializeAsProperty(Object bean, JsonGenerator g, SerializationContext ctxt)
      throws Exception {
    Object value = fromWriter.get(bean);
    if (Objects.nonNull(value)) {
      String text = translateService.translate(translate.key(), Objects.toString(value));
      g.writeStringProperty(getName(), text);
    }
  }
}
