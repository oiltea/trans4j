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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.resolver.DictResolver;
import java.util.Objects;

public class DictResolvingPropertyWriter extends BeanPropertyWriter {

  private final BeanPropertyWriter targetWriter;
  private final DictResolver dictResolver;
  private final Trans trans;

  public DictResolvingPropertyWriter(BeanPropertyWriter targetWriter, DictResolver dictResolver,
      Trans trans) {
    super(targetWriter);
    this.targetWriter = targetWriter;
    this.dictResolver = dictResolver;
    this.trans = trans;
  }

  @Override
  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {

    Object value = targetWriter.get(bean);
    String name = targetWriter.getName();

    targetWriter.serializeAsField(bean, gen, prov);

    String key = trans.key();
    String[] refs = trans.refs().length > 0 ? trans.refs() : new String[]{name + trans.suffix()};

    for (String targetFieldName : refs) {
      if (Objects.nonNull(value)) {
        gen.writeStringField(targetFieldName, dictResolver.resolve(key, value.toString()));
      } else {
        gen.writeNullField(targetFieldName);
      }
    }
  }
}