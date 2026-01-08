package io.github.oiltea.trans4j.core.trans;

import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.exception.TransException;
import io.github.oiltea.trans4j.core.util.FieldResolver;
import io.github.oiltea.trans4j.core.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 基于 {@link DictProvider} 的默认翻译服务实现。
 * <p>
 * <b>特性：</b>
 * <ul>
 *     <li>支持按类型懒加载字典</li>
 *     <li>支持全量字典预加载（性能优化）</li>
 *     <li>支持嵌套对象翻译</li>
 *     <li>支持集合/数组翻译</li>
 *     <li>智能缓存字典数据，减少重复查询</li>
 * </ul>
 * <p>
 * <b>工作流程：</b>
 * <ol>
 *     <li>收集所有需要翻译的字段及其字典类型</li>
 *     <li>优先从全量字典中获取，若不存在则按类型查询</li>
 *     <li>执行翻译并将结果写入目标字段</li>
 *     <li>递归处理嵌套对象和集合</li>
 * </ol>
 *
 * @author oiltea
 * @since 1.0.0
 */
public class DefaultTransService implements TransService {

	private final DictProvider dictProvider;

	/**
	 * 全量字典缓存（仅在 {@link DictProvider#getAllDicts()} 返回非空时使用）。
	 * <p>
	 * 注意：此缓存是实例级别的，在服务生命周期内有效。
	 */
	private volatile Map<String, Map<String, String>> allDictsCache;

	/**
	 * 是否已加载全量字典。
	 */
	private volatile boolean allDictsLoaded = false;

	public DefaultTransService(DictProvider dictProvider) {
		this.dictProvider = Objects.requireNonNull(dictProvider, "DictProvider must not be null");
	}

	@Override
	public void trans(Object vo, List<Field> transFieldList) throws Exception {
		if (vo == null || transFieldList == null || transFieldList.isEmpty()) {
			return;
		}

		// 1. 收集本次翻译涉及到的字典类型
		Set<String> types = collectDictTypes(transFieldList);
		if (types.isEmpty()) {
			return;
		}

		// 2. 加载字典数据（优先使用全量字典）
		loadDicts(types);

		// 3. 执行翻译
		translateFields(vo, transFieldList);
	}

	/**
	 * 收集字典类型。
	 */
	private Set<String> collectDictTypes(List<Field> transFieldList) {
		Set<String> types = new HashSet<>();
		for (Field field : transFieldList) {
			Trans trans = FieldResolver.getTransAnnotation(field);
			if (trans != null && !trans.key().isEmpty()) {
				types.add(trans.key());
			}
		}
		return types;
	}

	/**
	 * 加载字典数据。
	 * <p>
	 * 优先使用全量字典，若不存在则按类型加载。
	 * <p>
	 * 注意：字典数据在本次翻译会话中缓存，避免重复查询。
	 *
	 * @param types 需要加载的字典类型集合
	 */
	private void loadDicts(Set<String> types) {
		// 尝试加载全量字典（仅加载一次，使用双重检查锁定）
		if (!allDictsLoaded) {
			synchronized (this) {
				if (!allDictsLoaded) {
					Map<String, Map<String, String>> allDicts = dictProvider.getAllDicts();
					if (allDicts != null && !allDicts.isEmpty()) {
						allDictsCache = allDicts;
					}
					allDictsLoaded = true;
				}
			}
		}
	}

	/**
	 * 获取指定类型的字典。
	 * <p>
	 * 优先从全量字典缓存中获取，若不存在则按类型查询。
	 *
	 * @param type 字典类型
	 * @return 字典映射（code -> name），若不存在则返回空 Map
	 */
	private Map<String, String> getDict(String type) {
		// 优先从全量字典缓存获取
		if (allDictsCache != null && allDictsCache.containsKey(type)) {
			return allDictsCache.get(type);
		}

		// 按类型查询
		Map<String, String> dict = dictProvider.getDict(type);
		return dict != null ? dict : Collections.emptyMap();
	}

	/**
	 * 翻译字段。
	 */
	private void translateFields(Object vo, List<Field> transFieldList) throws Exception {
		for (Field field : transFieldList) {
			Trans trans = FieldResolver.getTransAnnotation(field);
			if (trans == null || trans.key().isEmpty()) {
				continue;
			}

			// 获取字段值
			Object rawValue = ReflectionUtils.getFieldValue(field, vo);
			if (rawValue == null) {
				if (trans.ignoreNull()) {
					continue;
				}
				// 如果 ignoreNull 为 false，继续处理（可能会写入空值）
			}

			// 处理集合类型（集合中的元素可能是对象，需要递归翻译）
			if (ReflectionUtils.isCollectionType(field) && rawValue != null) {
				translateCollection(field, vo, rawValue, trans);
				// 集合本身也可能需要翻译（如果集合字段本身有 @Trans）
				if (rawValue instanceof java.util.Collection<?> collection) {
					for (Object element : collection) {
						if (element != null && !isPrimitiveOrString(element.getClass())) {
							List<Field> elementFields = FieldResolver.resolveTransFields(element.getClass());
							if (!elementFields.isEmpty()) {
								translateFields(element, elementFields);
							}
						}
					}
				}
				continue;
			}

			// 处理数组类型
			if (ReflectionUtils.isArrayType(field) && rawValue != null) {
				translateArray(field, vo, rawValue, trans);
				// 数组中的元素如果是对象，也需要递归翻译
				int length = java.lang.reflect.Array.getLength(rawValue);
				for (int i = 0; i < length; i++) {
					Object element = java.lang.reflect.Array.get(rawValue, i);
					if (element != null && !isPrimitiveOrString(element.getClass())) {
						List<Field> elementFields = FieldResolver.resolveTransFields(element.getClass());
						if (!elementFields.isEmpty()) {
							translateFields(element, elementFields);
						}
					}
				}
				continue;
			}

			// 处理单个值
			translateSingleValue(field, vo, rawValue, trans);

			// 如果值是对象，递归翻译其字段
			if (rawValue != null && !isPrimitiveOrString(rawValue.getClass())) {
				List<Field> nestedFields = FieldResolver.resolveTransFields(rawValue.getClass());
				if (!nestedFields.isEmpty()) {
					translateFields(rawValue, nestedFields);
				}
			}
		}
	}

	/**
	 * 判断是否为基本类型或 String。
	 */
	private boolean isPrimitiveOrString(Class<?> clazz) {
		return clazz.isPrimitive() || clazz == String.class || clazz == Integer.class || clazz == Long.class
				|| clazz == Double.class || clazz == Float.class || clazz == Boolean.class || clazz == Byte.class
				|| clazz == Short.class || clazz == Character.class;
	}

	/**
	 * 翻译单个值。
	 */
	private void translateSingleValue(Field field, Object vo, Object rawValue, Trans trans) throws Exception {
		if (rawValue == null) {
			return;
		}

		String code = String.valueOf(rawValue);
		String type = trans.key();
		Map<String, String> dict = getDict(type);
		if (dict.isEmpty()) {
			return;
		}

		String name = dict.get(code);
		if (name == null) {
			return;
		}

		// 写入目标字段
		writeTargetFields(vo, field, trans, name);
	}

	/**
	 * 翻译集合（集合字段本身的翻译，如 List<String> 类型的 code 字段）。
	 */
	private void translateCollection(Field field, Object vo, Object rawValue, Trans trans) throws Exception {
		// 如果集合元素是基本类型或 String，则尝试翻译集合中的每个值
		if (rawValue instanceof java.util.Collection<?> collection && !collection.isEmpty()) {
			Object firstElement = collection.iterator().next();
			if (firstElement instanceof String) {
				// 集合中的 String 值作为 code，需要翻译
				// 注意：这里我们只处理集合字段本身的翻译，集合元素的递归翻译在 translateFields 中处理
				// 对于集合类型的字段，通常需要用户自定义处理逻辑
			}
		}
	}

	/**
	 * 翻译数组（数组字段本身的翻译）。
	 */
	private void translateArray(Field field, Object vo, Object rawValue, Trans trans) throws Exception {
		// 如果数组元素是基本类型或 String，则尝试翻译数组中的每个值
		if (rawValue != null && rawValue.getClass().isArray()) {
			Class<?> componentType = rawValue.getClass().getComponentType();
			if (componentType == String.class) {
				// String 数组的翻译逻辑
				// 注意：这里我们只处理数组字段本身的翻译，数组元素的递归翻译在 translateFields 中处理
			}
		}
	}

	/**
	 * 写入目标字段。
	 */
	private void writeTargetFields(Object vo, Field sourceField, Trans trans, String name) throws Exception {
		String[] refs = trans.refs();
		if (refs != null && refs.length > 0) {
			// 写入指定的目标字段
			for (String ref : refs) {
				setFieldValue(vo, ref, name);
			}
		}
		else {
			// 使用默认规则：原字段名 + suffix
			String targetName = sourceField.getName() + trans.suffix();
			setFieldValue(vo, targetName, name);
		}
	}

	/**
	 * 设置字段值。
	 */
	private void setFieldValue(Object vo, String fieldName, Object value) {
		Class<?> clazz = vo.getClass();
		Field target = ReflectionUtils.findField(clazz, fieldName);
		if (target == null) {
			throw new TransException(
					String.format("Cannot find target field '%s' in class %s", fieldName, clazz.getName()));
		}
		try {
			ReflectionUtils.setFieldValue(target, vo, value);
		}
		catch (IllegalAccessException e) {
			throw new TransException(
					String.format("Failed to set value for field '%s' in class %s", fieldName, clazz.getName()), e);
		}
	}

}




