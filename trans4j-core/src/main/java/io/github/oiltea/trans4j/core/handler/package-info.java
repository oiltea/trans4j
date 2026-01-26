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

/**
 * Translation failure handling strategies.
 *
 * <p>This package contains the {@link
 * io.github.oiltea.trans4j.core.handler.TranslationFailureHandler} interface and built-in
 * implementations for handling translation failures.
 *
 * <h2>Built-in Handlers</h2>
 *
 * <ul>
 *   <li>{@link io.github.oiltea.trans4j.core.handler.NullFailureHandler} - Returns null (default)
 *   <li>{@link io.github.oiltea.trans4j.core.handler.EmptyStringFailureHandler} - Returns empty
 *       string
 *   <li>{@link io.github.oiltea.trans4j.core.handler.OriginalValueFailureHandler} - Returns
 *       original value
 * </ul>
 *
 * <h2>Custom Handlers</h2>
 *
 * <p>Users can create custom handlers by implementing the {@link
 * io.github.oiltea.trans4j.core.handler.TranslationFailureHandler} interface.
 *
 * @see io.github.oiltea.trans4j.core.handler.TranslationFailureHandler
 * @see io.github.oiltea.trans4j.core.Translate
 */
package io.github.oiltea.trans4j.core.handler;
