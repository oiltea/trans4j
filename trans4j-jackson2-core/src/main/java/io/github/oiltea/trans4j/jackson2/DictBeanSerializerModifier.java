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
import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.resolver.DictResolver;
import java.util.ArrayList;
import java.util.List;

public class DictBeanSerializerModifier extends BeanSerializerModifier {

  private final DictResolver dictResolver;

  public DictBeanSerializerModifier(DictResolver dictResolver) {
    this.dictResolver = dictResolver;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
      BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

    List<BeanPropertyWriter> result = new ArrayList<>();
    for (BeanPropertyWriter writer : beanProperties) {
      result.add(writer);
      Trans trans = writer.getAnnotation(Trans.class);
      if (trans != null) {
        result.add(new DictResolvingPropertyWriter(writer, dictResolver, trans));
      }
    }
    return result;
  }
}