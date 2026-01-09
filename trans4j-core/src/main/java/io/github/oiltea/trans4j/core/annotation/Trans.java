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
package io.github.oiltea.trans4j.core.annotation;

import io.github.oiltea.trans4j.core.trans.TransDictProvider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典翻译注解。
 * <p>
 * 用于标记需要进行字典翻译的字段，框架会自动将 code 值转换为对应的 name 值。
 * <p>
 * <b>使用示例：</b>
 * <pre>{@code
 * public class UserVO {
 *     // 方式1：使用默认后缀（genderName）
 *     @Trans(key = "GENDER")
 *     private String gender;
 *     private String genderName;  // 自动填充
 *
 *     // 方式2：指定目标字段
 *     @Trans(key = "STATUS", refs = {"statusName", "statusText"})
 *     private String status;
 *     private String statusName;
 *     private String statusText;
 *
 *     // 方式3：自定义后缀
 *     @Trans(key = "TYPE", suffix = "Label")
 *     private String type;
 *     private String typeLabel;  // 自动填充
 * }
 * }</pre>
 *
 * @author oiltea
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Trans {

  /**
   * 字典类型（必填）。
   * <p>
   * 对应 {@link TransDictProvider#getDict(String)} 的参数。
   *
   * @return 字典类型标识
   */
  String key();

  /**
   * 翻译结果的目标字段名（可选）。
   * <p>
   * 若指定，翻译结果会写入这些字段；若未指定，则使用 {@link #suffix()} 规则自动生成字段名。
   * <p>
   * <b>注意：</b>目标字段必须存在于当前类或其父类中，否则会抛出异常。
   *
   * @return 目标字段名数组
   */
  String[] refs() default {};

  /**
   * 字段名后缀（默认："Text"）。
   * <p>
   * 当未指定 {@link #refs()} 时，框架会自动将翻译结果写入 {@code 原字段名 + suffix} 字段。
   * <p>
   * 例如：字段名为 {@code status}，suffix 为 {@code "Text"}，则目标字段为 {@code statusText}。
   *
   * @return 字段名后缀
   */
  String suffix() default "Text";

  /**
   * 是否忽略空值（默认：true）。
   * <p>
   * 当字段值为 {@code null} 或空字符串时，是否跳过翻译。
   *
   * @return 是否忽略空值
   */
  boolean ignoreNull() default true;

}