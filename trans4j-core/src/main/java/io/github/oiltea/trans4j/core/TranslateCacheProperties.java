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

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("trans4j.cache")
public class TranslateCacheProperties {
  private CacheType type;
  private final Caffeine caffeine = new Caffeine();
  private final Redis redis = new Redis();

  public static class Caffeine {
    private @Nullable String spec;

    public @Nullable String getSpec() {
      return this.spec;
    }

    public void setSpec(@Nullable String spec) {
      this.spec = spec;
    }
  }

  @Getter
  @Setter
  public static class Redis {
    private @Nullable Duration timeToLive;
    private @Nullable String keyPrefix;
  }
}
