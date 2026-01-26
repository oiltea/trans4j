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

import io.github.oiltea.trans4j.core.handler.NullFailureHandler;
import io.github.oiltea.trans4j.core.handler.TranslationFailureHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark fields that require translation.
 *
 * <p>This annotation is applied to fields to indicate that their values should be translated from
 * one language to another.
 *
 * @author oiltea
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Translate {

  /**
   * Returns the key associated with this instance.
   *
   * @return the key as a String
   */
  String key();

  /**
   * Gets the source field name.
   *
   * <p>Returns the source field from which the object originates.
   *
   * @return the source field name
   */
  String from();

  /**
   * The failure handler class to use when translation fails.
   *
   * <p>Defaults to {@link NullFailureHandler} which returns null on failure.
   *
   * @return the failure handler class
   */
  Class<? extends TranslationFailureHandler> failureHandler() default NullFailureHandler.class;
}
