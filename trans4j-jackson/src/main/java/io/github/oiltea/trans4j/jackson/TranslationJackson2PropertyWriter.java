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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import io.github.oiltea.trans4j.core.Translate;
import io.github.oiltea.trans4j.core.TranslationService;

/**
 * A custom Jackson property writer that translates property values during JSON serialization.
 *
 * <p>This class extends {@link BeanPropertyWriter} to intercept the serialization of specific bean
 * properties and apply translation to their values based on the configured {@link Translate}
 * annotation. It uses a {@link TranslationService} to perform the actual translation.
 *
 * <p>This writer is typically used in scenarios where certain fields of an object need to be
 * serialized in a different language or format, such as internationalization (i18n) support in JSON
 * APIs.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class TranslationJackson2PropertyWriter extends BeanPropertyWriter {

  /**
   * Service for translating text between languages.
   *
   * <p>Provides functionality to convert text from one language to another using configured
   * translation APIs.
   *
   * @see TranslationService
   */
  private final TranslationService translationService;

  /**
   * The writer for the source property during bean copying or mapping operations.
   *
   * <p>This field holds a reference to the property writer that is used to read values from the
   * source bean when copying properties to a destination bean.
   *
   * @see BeanPropertyWriter
   */
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
   * Constructs a new TranslationJackson2PropertyWriter with the specified translation service and
   * property writers. This writer is used to translate property values during JSON serialization.
   *
   * @param translationService the service used for translating property values
   * @param writer the original property writer to delegate serialization to
   * @param fromWriter the property writer for the source property from which translation is
   *     performed
   * @param translate the translation annotation containing configuration for the translation
   *     process
   */
  protected TranslationJackson2PropertyWriter(
      TranslationService translationService,
      BeanPropertyWriter writer,
      BeanPropertyWriter fromWriter,
      Translate translate) {
    super(writer);
    this.translationService = translationService;
    this.fromWriter = fromWriter;
    this.translate = translate;
  }

  /**
   * Serializes a field from the given bean as a JSON field.
   *
   * <p>Retrieves the field value from the bean, translates it using a translation service, and
   * writes it as a string field to the JSON generator.
   *
   * @param bean the object from which to get the field value
   * @param gen the JSON generator to write the field to
   * @param prov the serializer provider
   * @throws Exception if an error occurs during serialization or translation
   */
  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    String key = translate.key();
    Object value = fromWriter.get(bean);
    if (key != null && value != null) {
      gen.writeStringField(getName(), translationService.translate(key, value.toString()));
    }
  }
}
