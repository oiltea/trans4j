/*
 * Copyright © ${YEAR} oiltea
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

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.oiltea.trans4j.core.TranslateService;

public class TranslateJackson2Module extends SimpleModule {

  private final TranslateService translateService;

  public TranslateJackson2Module(TranslateService translateService) {
    this.translateService = translateService;
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(
        new TranslateJackson2BeanSerializerModifier(translateService));
    super.setupModule(context);
  }
}
