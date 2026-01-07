package io.github.oiltea.trans4j.core.trans;

import io.github.oiltea.trans4j.core.annotation.Trans;

import java.util.Collections;
import java.util.Map;

/**
 * 字典数据提供者 SPI。
 * <p>
 * 使用方只需实现该接口，即可将自身的字典数据源（数据库、缓存、配置等）接入到 trans4j。
 */
public interface DictProvider {

	/**
	 * 根据字典类型获取该类型下的编码 - 名称映射。
	 * @param type 字典类型（通常与 {@link Trans#key()} 对应）
	 * @return code -> name 映射，若不存在该类型可返回空 Map 或 {@code null}
	 */
	Map<String, String> getDict(String type);

	/**
	 * 获取全量字典。
	 * <p>
	 * 主要用于“全量预加载”场景，可通过一次查询将所有字典加载到内存，避免频繁访问外部存储。
	 * <p>
	 * 默认返回空 Map，表示不支持/不使用全量加载模式，此时框架将回退到按类型查询
	 * {@link #getDict(String)} 的方式。
	 * @return type -> (code -> name) 的嵌套映射
	 */
	default Map<String, Map<String, String>> getAllDicts() {
		return Collections.emptyMap();
	}

}