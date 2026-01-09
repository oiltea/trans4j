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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import tools.jackson.databind.BeanDescription.Supplier;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

public class DictBeanSerializerModifier extends ValueSerializerModifier {

  private final DictResolver dictResolver;

  public DictBeanSerializerModifier(DictResolver dictResolver) {
    this.dictResolver = dictResolver;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config, Supplier beanDesc,
      List<BeanPropertyWriter> beanProperties) {
    Map<String, BeanPropertyWriter> writerMap = beanProperties.stream()
        .collect(Collectors.toMap(BeanPropertyWriter::getName, Function.identity()));

    for (BeanPropertyWriter writer : beanProperties) {
      Trans trans = writer.getAnnotation(Trans.class);
      if (trans != null) {
        String name = writer.getName();
        String[] refs = trans.refs();
        String[] propertyNames = refs.length > 0 ? refs : new String[]{name + trans.suffix()};
        for (String propertyName : propertyNames) {
          writerMap.put(propertyName,
              new DictResolvingPropertyWriter(writer, dictResolver, propertyName, trans));
        }
      }
    }
    return List.copyOf(writerMap.values());
  }
}