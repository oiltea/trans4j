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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.oiltea.trans4j.core.TranslationService;

/**
 * Jackson 2 module for integrating translation functionality into JSON serialization.
 *
 * <p>This module extends {@link SimpleModule} to provide automatic translation of field values
 * during JSON serialization. It registers a custom {@link
 * TranslationJackson2BeanSerializerModifier} that intercepts the serialization process and applies
 * translation logic to annotated fields.
 *
 * <p>Typical usage involves adding this module to an {@link ObjectMapper} instance to enable
 * on-the-fly translation of specific fields in serialized JSON responses.
 *
 * @author Oiltea
 * @version 1.0.0
 */
public class TranslationJackson2Module extends SimpleModule {

  /**
   * The translation service used for text translation operations.
   *
   * <p>This service provides methods to translate text between different languages.
   *
   * @see TranslationService
   */
  private final TranslationService translationService;

  /**
   * Constructs a new TranslationJackson2Module with the specified translation service. This module
   * is used to integrate translation functionality with Jackson JSON processing.
   *
   * @param translationService the translation service to be used for text translation
   */
  public TranslationJackson2Module(TranslationService translationService) {
    this.translationService = translationService;
  }

  /**
   * Configures the Jackson module by adding a custom bean serializer modifier for translation
   * support.
   *
   * <p>This method registers a {@link TranslationJackson2BeanSerializerModifier} with the provided
   * {@link TranslationService} to the setup context, then delegates to the superclass
   * implementation.
   *
   * @param context the setup context used to configure the Jackson module
   * @see com.fasterxml.jackson.databind.Module#setupModule(SetupContext)
   */
  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(
        new TranslationJackson2BeanSerializerModifier(translationService));
    super.setupModule(context);
  }
}
