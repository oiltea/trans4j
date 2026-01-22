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

/**
 * Enumeration defining strategies for handling failures or missing values in various operations.
 *
 * <p>This enum provides different approaches to deal with situations where a required value is not
 * available. Each strategy represents a specific behavior to adopt when a failure occurs.
 *
 * <p>The available strategies are:
 *
 * <ul>
 *   <li>{@link #NULL} - Return {@code null} to indicate the absence of a value.
 *   <li>{@link #EMPTY_STRING} - Return an empty string ({@code ""}) as a placeholder.
 *   <li>{@link #DEFAULT_VALUE} - Return a predefined default value specific to the context.
 *   <li>{@link #EXCEPTION} - Throw an exception to signal the failure explicitly.
 * </ul>
 *
 * @author oiltea
 * @version 1.0.0
 */
public enum FailStrategy {
  /** Represents a null value or absence of a value. */
  NULL,

  /** An empty string constant. */
  EMPTY_STRING,

  /** The default value used when no other value is specified. */
  DEFAULT_VALUE,

  /**
   * Exception type constant for exception handling scenarios.
   *
   * <p>This constant is used to identify and categorize different types of exceptions within the
   * application's error handling mechanism.
   */
  EXCEPTION
}
