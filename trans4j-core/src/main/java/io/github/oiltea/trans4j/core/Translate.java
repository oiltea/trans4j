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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import java.util.function.Function;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

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
  @NonNull String key();

  /**
   * Gets the source field name.
   *
   * <p>Returns the source field from which the object originates.
   *
   * @return the source field name
   */
  @NonNull String from();

  /**
   * Returns the null policy for this annotation.
   *
   * <p>The null policy determines how null values should be handled.
   *
   * @return the null policy, defaults to {@code NullPolicy.NULL}
   * @since 1.1.0
   */
  NullPolicy nullPolicy() default NullPolicy.NULL;

  /**
   * Enumeration defining policies for handling null or empty values in string processing.
   *
   * <p>This enum provides strategies for converting null values to either null references or empty
   * strings, allowing flexible null handling in various string manipulation scenarios.
   *
   * @author Oiltea
   * @since 1.1.0
   */
  @Getter
  enum NullPolicy {
    /**
     * A constant representing a null value transformation function.
     *
     * <p>This function takes any value and returns null, effectively ignoring the input and always
     * producing a null result. It can be used as a default or placeholder transformation in
     * functional pipelines where a null-producing operation is required.
     *
     * <p>The function is implemented as a lambda: {@code v -> null}.
     */
    NULL(v -> v),

    /**
     * Converts any value to an empty string representation.
     *
     * <p>If the value is {@code null}, returns an empty string; otherwise, converts the value to
     * its string representation.
     */
    EMPTY(v -> Objects.toString(v, ""));

    /**
     * A function that transforms a string input into a string output.
     *
     * <p>This field holds a reference to a specific transformation logic.
     *
     * @see Function
     */
    private final Function<String, String> handler;

    /**
     * Creates a NullPolicy instance with the specified function.
     *
     * <p>The provided function will be applied to handle null values.
     *
     * @param handler the function to be used for null value handling
     */
    NullPolicy(Function<String, String> handler) {
      this.handler = handler;
    }
  }
}
