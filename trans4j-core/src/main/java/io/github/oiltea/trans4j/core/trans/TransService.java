package io.github.oiltea.trans4j.core.trans;

import io.github.oiltea.trans4j.core.annotation.Trans;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 字典翻译服务接口。
 * <p>
 * 负责对对象中带 {@link Trans} 注解的字段进行字典翻译，将 code 值转换为对应的 name 值。
 * <p>
 * <b>实现类：</b>
 * <ul>
 *     <li>{@link DefaultTransService}：基于 {@link DictProvider} 的默认实现</li>
 * </ul>
 * <p>
 * <b>使用场景：</b>
 * <ul>
 *     <li>JPA 实体加载后自动翻译（通过 {@code @PostLoad} 回调）</li>
 *     <li>MyBatis 查询结果自动翻译（通过拦截器）</li>
 *     <li>手动调用翻译（适用于非持久化对象）</li>
 * </ul>
 *
 * @author oiltea
 * @since 1.0.0
 */
public interface TransService {

	/**
	 * 对对象中指定的字段进行字典翻译。
	 * <p>
	 * 该方法会：
	 * <ol>
	 *     <li>读取字段值（code）</li>
	 *     <li>根据字段上的 {@link Trans#key()} 获取对应的字典</li>
	 *     <li>将 code 转换为 name</li>
	 *     <li>将 name 写入目标字段（根据 {@link Trans#refs()} 或默认规则）</li>
	 * </ol>
	 *
	 * @param vo             要翻译的对象（VO、DO、DTO 等）
	 * @param transFieldList 需要翻译的字段列表（通常通过 {@link io.github.oiltea.trans4j.core.util.FieldResolver#resolveTransFields(Class)}
	 *                       获取）
	 * @throws Exception 翻译过程中的异常（如字段不存在、字典数据缺失等）
	 */
	void trans(Object vo, List<Field> transFieldList) throws Exception;

}