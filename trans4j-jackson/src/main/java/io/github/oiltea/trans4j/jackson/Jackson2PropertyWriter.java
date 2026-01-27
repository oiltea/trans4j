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
import java.util.Objects;

/**
 * A custom Jackson property writer that translates property values during JSON serialization.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class Jackson2PropertyWriter extends BeanPropertyWriter {

  private final TranslationService translationService;
  private final BeanPropertyWriter fromWriter;
  private final Translate translate;

  protected Jackson2PropertyWriter(
      TranslationService translationService,
      BeanPropertyWriter writer,
      BeanPropertyWriter fromWriter,
      Translate translate) {
    super(writer);
    this.translationService = translationService;
    this.fromWriter = fromWriter;
    this.translate = translate;
  }

  @Override
  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    String code = Objects.toString(fromWriter.get(bean), null);
    String value = translationService.translate(translate.key(), code);
    String finalValue = translate.nullPolicy().getHandler().apply(value);
    if (finalValue == null) {
      gen.writeNullField(getName());
    } else {
      gen.writeStringField(getName(), finalValue);
    }
  }
}
