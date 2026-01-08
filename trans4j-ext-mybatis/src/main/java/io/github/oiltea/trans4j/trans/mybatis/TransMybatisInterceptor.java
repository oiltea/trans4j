package io.github.oiltea.trans4j.trans.mybatis;

import io.github.oiltea.trans4j.core.trans.TransService;
import io.github.oiltea.trans4j.core.util.FieldResolver;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * MyBatis 结果集拦截器，对查询结果中带 {@link io.github.oiltea.trans4j.core.annotation.Trans}
 * 注解的字段进行字典翻译。
 * <p>
 * <b>工作原理：</b>
 * <ul>
 *     <li>拦截 MyBatis 的 {@code ResultSetHandler#handleResultSets} 方法</li>
 *     <li>在查询结果返回前，自动检测结果对象中带 {@code @Trans} 注解的字段</li>
 *     <li>调用 {@link TransService} 执行字典翻译</li>
 * </ul>
 * <p>
 * <b>支持场景：</b>
 * <ul>
 *     <li>单个对象翻译</li>
 *     <li>List 集合翻译</li>
 *     <li>嵌套对象翻译</li>
 * </ul>
 * <p>
 * <b>性能优化：</b>
 * <ul>
 *     <li>仅对带 {@code @Trans} 注解的对象进行翻译，避免不必要的反射开销</li>
 *     <li>支持批量翻译，减少字典查询次数</li>
 * </ul>
 *
 * @author oiltea
 * @since 1.0.0
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
@RequiredArgsConstructor
public class TransMybatisInterceptor implements Interceptor {

	private final TransService transService;

	/**
	 * 拦截结果集处理，执行字典翻译。
	 *
	 * @param invocation 方法调用
	 * @return 处理后的结果
	 * @throws Throwable 处理过程中的异常
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 先执行原始方法，获取查询结果
		Object result = invocation.proceed();

		if (result == null) {
			return null;
		}

		// 处理结果
		if (result instanceof List<?> list) {
			// 批量处理列表
			for (Object element : list) {
				translateIfNecessary(element);
			}
		}
		else {
			// 处理单个对象
			translateIfNecessary(result);
		}

		return result;
	}

	/**
	 * 对对象进行翻译（如果需要）。
	 *
	 * @param target 目标对象
	 * @throws Exception 翻译过程中的异常
	 */
	protected void translateIfNecessary(Object target) throws Exception {
		if (target == null) {
			return;
		}

		// 快速检查：是否有需要翻译的字段
		List<Field> transFields = FieldResolver.resolveTransFields(target.getClass());
		if (transFields.isEmpty()) {
			return;
		}

		// 执行翻译
		transService.trans(target, transFields);
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// 支持通过 MyBatis 配置传递属性（如果需要）
	}

}