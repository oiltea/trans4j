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

package io.github.oiltea.trans4j.core;

import static org.junit.jupiter.api.Assertions.*;

import io.github.oiltea.trans4j.core.handler.EmptyStringFailureHandler;
import io.github.oiltea.trans4j.core.handler.NullFailureHandler;
import io.github.oiltea.trans4j.core.handler.OriginalValueFailureHandler;
import io.github.oiltea.trans4j.core.handler.TranslationFailureHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TranslationFailureHandlerTest {

  @Test
  @DisplayName("NullFailureHandler should return null")
  void null_handler_should_return_null() {
    TranslationFailureHandler handler = new NullFailureHandler();
    assertNull(handler.handle("key", "value"));
  }

  @Test
  @DisplayName("EmptyStringFailureHandler should return empty string")
  void empty_string_handler_should_return_empty_string() {
    TranslationFailureHandler handler = new EmptyStringFailureHandler();
    assertEquals("", handler.handle("key", "value"));
  }

  @Test
  @DisplayName("OriginalValueFailureHandler should return original value")
  void original_value_handler_should_return_original_value() {
    TranslationFailureHandler handler = new OriginalValueFailureHandler();
    assertEquals("value", handler.handle("key", "value"));
  }

  @Test
  @DisplayName("OriginalValueFailureHandler should return null when original is null")
  void original_value_handler_should_handle_null() {
    TranslationFailureHandler handler = new OriginalValueFailureHandler();
    assertNull(handler.handle("key", null));
  }

  @Test
  @DisplayName("TranslationFailureHandlers should cache handler instances")
  void handlers_should_be_cached() {
    TranslationFailureHandler handler1 =
        TranslationFailureHandlers.getHandler(NullFailureHandler.class);
    TranslationFailureHandler handler2 =
        TranslationFailureHandlers.getHandler(NullFailureHandler.class);
    assertSame(handler1, handler2);
  }

  @Test
  @DisplayName("Custom handler implementation should work")
  void custom_handler_implementation_should_work() {
    // Custom handler that returns uppercase original value
    TranslationFailureHandler customHandler = (key, originalValue) -> "DEFAULT";

    assertEquals("DEFAULT", customHandler.handle("key", "value"));
    assertEquals("DEFAULT", customHandler.handle("key", null));
  }
}
