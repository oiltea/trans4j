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

import io.github.oiltea.trans4j.core.TranslationService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * Jackson 3 module for integrating translation functionality into JSON serialization.
 *
 * <p>This module extends {@link SimpleModule} to add translation support during serialization by
 * registering a custom {@link Jackson3BeanSerializerModifier}. It is designed to automatically
 * translate specific fields in objects based on the provided {@link TranslationService}.
 *
 * <p>Typical usage involves constructing the module with a translation service instance and
 * registering it with an {@link ObjectMapper} to enable field-level translation during JSON output.
 *
 * @author Oiltea
 * @since 1.0.0
 */
public class TranslationJackson3Module extends SimpleModule {

  private final TranslationService translationService;

  public TranslationJackson3Module(TranslationService translationService) {
    this.translationService = translationService;
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addSerializerModifier(new Jackson3BeanSerializerModifier(translationService));
    super.setupModule(context);
  }
}
