package io.github.oiltea.trans4j.core.util;

import io.github.oiltea.trans4j.core.annotation.Trans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段解析器。
 * <p>
 * 用于解析类中带 {@link Trans} 注解的字段。
 *
 * @author oiltea
 * @since 1.0.0
 */
public final class FieldResolver {

	private FieldResolver() {
		// 工具类，禁止实例化
	}

	/**
	 * 解析类及其父类中所有带 {@link Trans} 注解的字段。
	 *
	 * @param clazz 目标类
	 * @return 带 {@link Trans} 注解的字段列表
	 */
	public static List<Field> resolveTransFields(Class<?> clazz) {
		List<Field> result = new ArrayList<>();
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			for (Field field : current.getDeclaredFields()) {
				if (field.isAnnotationPresent(Trans.class)) {
					result.add(field);
				}
			}
			current = current.getSuperclass();
		}
		return result;
	}

	/**
	 * 获取字段上的 {@link Trans} 注解。
	 *
	 * @param field 字段对象
	 * @return {@link Trans} 注解，若不存在则返回 {@code null}
	 */
	public static Trans getTransAnnotation(Field field) {
		if (field == null) {
			return null;
		}
		return field.getAnnotation(Trans.class);
	}

}

