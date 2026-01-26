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

import io.github.oiltea.trans4j.core.Translate;
import io.github.oiltea.trans4j.core.handler.*;
import lombok.Data;

@Data
public class UserDto {

  private Long id;

  private String gender;

  @Translate(key = "gender", from = "gender")
  private String genderText;

  private String status;

  @Translate(key = "status", from = "status", failureHandler = EmptyStringFailureHandler.class)
  private String statusText;

  private String role;

  @Translate(key = "role", from = "role", failureHandler = OriginalValueFailureHandler.class)
  private String roleText;
}
