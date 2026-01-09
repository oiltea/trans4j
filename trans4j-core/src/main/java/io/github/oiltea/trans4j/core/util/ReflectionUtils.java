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

package io.github.oiltea.trans4j.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类。
 * <p>
 * 提供字段查找、值设置等常用反射操作。
 *
 * @author oiltea
 * @since 1.0.0
 */
public final class ReflectionUtils {

  private ReflectionUtils() {
    // 工具类，禁止实例化
  }

  /**
   * 在类层级结构中查找字段（包含父类）。
   *
   * @param clazz     目标类
   * @param fieldName 字段名
   * @return 字段对象，若不存在则返回 {@code null}
   */
  public static Field findField(Class<?> clazz, String fieldName) {
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException ignored) {
        current = current.getSuperclass();
      }
    }
    return null;
  }

  /**
   * 获取类及其父类中所有字段。
   *
   * @param clazz 目标类
   * @return 字段列表
   */
  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      for (Field field : current.getDeclaredFields()) {
        fields.add(field);
      }
      current = current.getSuperclass();
    }
    return fields;
  }

  /**
   * 安全地获取字段值。
   *
   * @param field  字段对象
   * @param target 目标对象
   * @return 字段值，若获取失败则返回 {@code null}
   */
  public static Object getFieldValue(Field field, Object target) {
    if (field == null || target == null) {
      return null;
    }
    try {
      boolean accessible = field.isAccessible();
      if (!accessible) {
        field.setAccessible(true);
      }
      Object value = field.get(target);
      if (!accessible) {
        field.setAccessible(false);
      }
      return value;
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  /**
   * 安全地设置字段值。
   *
   * @param field  字段对象
   * @param target 目标对象
   * @param value  要设置的值
   * @throws IllegalAccessException 如果设置失败
   */
  public static void setFieldValue(Field field, Object target, Object value)
      throws IllegalAccessException {
    if (field == null || target == null) {
      return;
    }
    boolean accessible = field.isAccessible();
    if (!accessible) {
      field.setAccessible(true);
    }
    try {
      field.set(target, value);
    } finally {
      if (!accessible) {
        field.setAccessible(false);
      }
    }
  }

  /**
   * 判断字段是否为集合类型。
   *
   * @param field 字段对象
   * @return 是否为集合类型
   */
  public static boolean isCollectionType(Field field) {
    if (field == null) {
      return false;
    }
    Class<?> type = field.getType();
    return java.util.Collection.class.isAssignableFrom(type);
  }

  /**
   * 判断字段是否为数组类型。
   *
   * @param field 字段对象
   * @return 是否为数组类型
   */
  public static boolean isArrayType(Field field) {
    if (field == null) {
      return false;
    }
    return field.getType().isArray();
  }

}

