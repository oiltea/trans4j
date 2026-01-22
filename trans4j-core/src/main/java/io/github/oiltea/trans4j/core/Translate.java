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

package io.github.oiltea.trans4j.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark fields that require translation.
 *
 * <p>This annotation is applied to fields to indicate that their values should be translated from
 * one language to another. It specifies the translation key, source field, failure handling
 * strategy, and an optional default value.
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
   * Returns the default fail strategy to be used when a failure occurs.
   *
   * <p>This method specifies the default behavior for handling failures in the context where this
   * annotation is applied. The default value is {@code FailStrategy.NULL}, which indicates that
   * null should be returned or used in case of a failure.
   *
   * @return the default fail strategy
   */
  FailStrategy failStrategy() default FailStrategy.NULL;

  /**
   * Returns the default value for this element.
   *
   * <p>This method specifies the default value that should be used when no explicit value is
   * provided.
   *
   * @return the default value as a String
   */
  String defaultValue() default "";
}
