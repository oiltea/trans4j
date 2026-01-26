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

import io.github.oiltea.trans4j.core.handler.TranslationFailureHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry and factory for {@link TranslationFailureHandler} instances.
 *
 * <p>This class manages handler instances using a singleton pattern per handler class, ensuring
 * efficient reuse of handler objects.
 *
 * @author oiltea
 * @since 1.1.0
 */
public final class TranslationFailureHandlers {

  private static final Map<Class<? extends TranslationFailureHandler>, TranslationFailureHandler>
      HANDLER_CACHE = new ConcurrentHashMap<>();

  private TranslationFailureHandlers() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Gets or creates a handler instance for the given handler class.
   *
   * @param handlerClass the handler class
   * @return the handler instance
   * @throws IllegalStateException if the handler cannot be instantiated
   */
  public static TranslationFailureHandler getHandler(
      Class<? extends TranslationFailureHandler> handlerClass) {
    return HANDLER_CACHE.computeIfAbsent(
        handlerClass,
        clazz -> {
          try {
            return clazz.getDeclaredConstructor().newInstance();
          } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to instantiate failure handler: " + clazz.getName(), e);
          }
        });
  }
}
