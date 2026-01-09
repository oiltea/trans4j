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

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.trans.TransService;

public class TransAnnotationIntrospector extends JacksonAnnotationIntrospector {

  private final TransService transService;

  public TransAnnotationIntrospector(TransService transService) {
    this.transService = transService;
  }

  @Override
  public Object findSerializer(Annotated a) {
    Trans trans = a.getAnnotation(Trans.class);
    if (trans != null) {
      return new TransSerializer(transService, trans);
    }
    return super.findSerializer(a);
  }
}