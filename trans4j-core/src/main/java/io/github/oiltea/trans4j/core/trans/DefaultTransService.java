package io.github.oiltea.trans4j.core.trans;

import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.exception.TransException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 基于 {@link DictProvider} 的默认翻译实现。
 * <p>
 * - 支持按类型懒加载字典；<br>
 * - 若 {@link DictProvider#getAllDicts()} 返回非空，则优先使用全量字典以减少外部访问。
 */
public class DefaultTransService implements TransService {

	private final DictProvider dictProvider;

	public DefaultTransService(DictProvider dictProvider) {
		this.dictProvider = Objects.requireNonNull(dictProvider, "DictProvider must not be null");
	}

	@Override
	public void trans(Object vo, List<Field> transFieldList) throws Exception {
		if (vo == null || transFieldList == null || transFieldList.isEmpty()) {
			return;
		}

		// 1. 收集本次翻译涉及到的字典类型
		Set<String> types = new HashSet<>();
		for (Field field : transFieldList) {
			Trans trans = field.getAnnotation(Trans.class);
			if (trans != null && !trans.key().isEmpty()) {
				types.add(trans.key());
			}
		}
		if (types.isEmpty()) {
			return;
		}

		// 2. 优先尝试全量加载；若未实现或返回为空，则回退到按类型加载
		Map<String, Map<String, String>> allDicts = Optional.ofNullable(this.dictProvider.getAllDicts())
			.orElse(Collections.emptyMap());

		Map<String, Map<String, String>> dictCache = new HashMap<>();
		if (!allDicts.isEmpty()) {
			// 只保留当前需要用到的类型，避免无意义的内存占用
			for (String type : types) {
				Map<String, String> dict = allDicts.get(type);
				if (dict != null) {
					dictCache.put(type, dict);
				}
			}
		}

		// 3. 遍历字段执行翻译
		for (Field field : transFieldList) {
			Trans trans = field.getAnnotation(Trans.class);
			if (trans == null) {
				continue;
			}

			String type = trans.key();
			if (type.isEmpty()) {
				continue;
			}

			// 实际取值字段
			field.setAccessible(true);
			Object rawValue = field.get(vo);
			if (rawValue == null) {
				continue;
			}
			String code = String.valueOf(rawValue);

			// 获取对应字典（按需从 provider 获取）
			Map<String, String> dict = dictCache.computeIfAbsent(type, t -> {
				Map<String, String> fromProvider = dictProvider.getDict(t);
				return fromProvider != null ? fromProvider : Collections.emptyMap();
			});

			String name = dict.get(code);
			if (name == null) {
				continue;
			}

			// 4. 映射到目标字段
			String[] refs = trans.refs();
			if (refs != null && refs.length > 0) {
				for (String ref : refs) {
					setFieldValue(vo, ref, name);
				}
			}
			else {
				// 未指定 ref 时，使用原字段名 + suffix 的代理模式
				String targetName = field.getName() + trans.suffix();
				setFieldValue(vo, targetName, name);
			}
		}
	}

	/**
	 * 向实体对象的指定字段写入值。
	 * <p>
	 * 仅当字段存在且可写时才会赋值，否则抛出 {@link TransException}。
	 */
	protected void setFieldValue(Object vo, String fieldName, Object value) {
		Class<?> clazz = vo.getClass();
		Field target = findField(clazz, fieldName);
		if (target == null) {
			throw new TransException("Can not find target field '" + fieldName + "' in class " + clazz.getName());
		}
		try {
			target.setAccessible(true);
			target.set(vo, value);
		}
		catch (IllegalAccessException e) {
			throw new TransException("Failed to set value for field '" + fieldName + "' in class " + clazz.getName(),
					e);
		}
	}

	/**
	 * 在类层级结构中查找字段（包含父类）。
	 */
	protected Field findField(Class<?> clazz, String fieldName) {
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			try {
				return current.getDeclaredField(fieldName);
			}
			catch (NoSuchFieldException ignored) {
				current = current.getSuperclass();
			}
		}
		return null;
	}

}


