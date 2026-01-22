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

import io.github.oiltea.trans4j.core.Translate;
import io.github.oiltea.trans4j.core.TranslateService;
import java.util.Objects;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.BeanPropertyWriter;

public class TranslateJackson3PropertyWriter extends BeanPropertyWriter {

  private final TranslateService translateService;
  private final BeanPropertyWriter fromWriter;
  private final Translate translate;

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

  @Override
  public void serializeAsProperty(Object bean, JsonGenerator g, SerializationContext ctxt)
      throws Exception {
    Object fromValue = fromWriter.get(bean);
    String text = translateService.translate(translate.key(), Objects.toString(fromValue));
    g.writeStringProperty(getName(), text);
  }
}
