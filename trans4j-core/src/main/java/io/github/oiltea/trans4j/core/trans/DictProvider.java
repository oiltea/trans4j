package io.github.oiltea.trans4j.core.trans;

import io.github.oiltea.trans4j.core.annotation.Trans;

import java.util.Collections;
import java.util.Map;

/**
 * 字典数据提供者 SPI（Service Provider Interface）。
 * <p>
 * 使用方只需实现该接口，即可将自身的字典数据源（数据库、缓存、配置文件、远程服务等）接入到 trans4j。
 * <p>
 * <b>实现示例：</b>
 * <pre>{@code
 * @Component
 * public class DbDictProvider implements DictProvider {
 *
 *     @Autowired
 *     private DictMapper dictMapper;
 *
 *     @Override
 *     public Map<String, String> getDict(String type) {
 *         // 从数据库查询字典项
 *         List<DictItem> items = dictMapper.selectByType(type);
 *         return items.stream()
 *             .collect(Collectors.toMap(DictItem::getCode, DictItem::getName));
 *     }
 *
 *     @Override
 *     public Map<String, Map<String, String>> getAllDicts() {
 *         // 全量加载（可选，用于性能优化）
 *         List<DictItem> allItems = dictMapper.selectAll();
 *         return allItems.stream()
 *             .collect(Collectors.groupingBy(
 *                 DictItem::getType,
 *                 Collectors.toMap(DictItem::getCode, DictItem::getName)
 *             ));
 *     }
 * }
 * }</pre>
 * <p>
 * <b>性能建议：</b>
 * <ul>
 *     <li>对于频繁访问的字典，建议实现 {@link #getAllDicts()} 进行全量预加载</li>
 *     <li>对于不常用的字典，仅实现 {@link #getDict(String)} 按需加载即可</li>
 *     <li>可在实现类中添加缓存机制，减少对底层数据源的访问</li>
 * </ul>
 *
 * @author oiltea
 * @since 1.0.0
 */
public interface DictProvider {

	/**
	 * 根据字典类型获取该类型下的编码 - 名称映射。
	 * <p>
	 * 框架会在需要翻译时调用此方法，按需加载字典数据。
	 *
	 * @param type 字典类型（通常与 {@link Trans#key()} 对应）
	 * @return code -> name 映射，若不存在该类型可返回空 Map 或 {@code null}
	 */
	Map<String, String> getDict(String type);

	/**
	 * 获取全量字典（可选实现）。
	 * <p>
	 * 主要用于"全量预加载"场景，可通过一次查询将所有字典加载到内存，避免频繁访问外部存储。
	 * <p>
	 * <b>使用场景：</b>
	 * <ul>
	 *     <li>字典数据量不大，可以全部加载到内存</li>
	 *     <li>字典访问频繁，需要减少数据库/缓存查询次数</li>
	 *     <li>字典数据相对稳定，不需要实时更新</li>
	 * </ul>
	 * <p>
	 * <b>默认行为：</b>
	 * <p>
	 * 默认返回空 Map，表示不支持/不使用全量加载模式，此时框架将回退到按类型查询
	 * {@link #getDict(String)} 的方式。
	 *
	 * @return type -> (code -> name) 的嵌套映射，格式为：
	 *         <pre>{@code
	 *         {
	 *             "GENDER": {"M": "男", "F": "女"},
	 *             "STATUS": {"1": "启用", "0": "禁用"}
	 *         }
	 *         }</pre>
	 */
	default Map<String, Map<String, String>> getAllDicts() {
		return Collections.emptyMap();
	}

}