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
package io.github.oiltea.trans4j.jackson3;

import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.resolver.DictResolver;
import java.util.Objects;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.BeanPropertyWriter;

public class DictResolvingPropertyWriter extends BeanPropertyWriter {

  private final BeanPropertyWriter targetWriter;
  private final DictResolver dictResolver;
  private final String propertyName;
  private final Trans trans;

  public DictResolvingPropertyWriter(BeanPropertyWriter targetWriter, DictResolver dictResolver,
      String propertyName, Trans trans) {
    super(targetWriter);
    this.targetWriter = targetWriter;
    this.dictResolver = dictResolver;
    this.propertyName = propertyName;
    this.trans = trans;
  }

  @Override
  public void serializeAsProperty(Object bean, JsonGenerator gen, SerializationContext ctxt)
      throws Exception {
    Object value = targetWriter.get(bean);
    if (Objects.nonNull(value)) {
      gen.writeStringProperty(propertyName, dictResolver.resolve(trans.key(), value.toString()));
    } else {
      gen.writeNullProperty(propertyName);
    }
  }
}