package io.github.oiltea.trans4j.trans.mybatis;

import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.trans.TransService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MyBatis 结果集拦截器，对查询结果中带 {@link Trans} 注解的字段进行字典翻译。
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
@RequiredArgsConstructor
public class TransMybatisInterceptor implements Interceptor {

	private final TransService transService;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = invocation.proceed();
		if (result == null) {
			return null;
		}

		if (result instanceof List<?> list) {
			for (Object element : list) {
				translateIfNecessary(element, transService);
			}
		}
		else {
			translateIfNecessary(result, transService);
		}
		return result;
	}

	protected void translateIfNecessary(Object target, TransService transService) throws Exception {
		if (Objects.nonNull(target)) {
			List<Field> transFields = resolveTransFields(target.getClass());
			if (!transFields.isEmpty()) {
				transService.trans(target, transFields);
			}
		}
	}

	protected List<Field> resolveTransFields(Class<?> clazz) {
		List<Field> result = new ArrayList<>();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(Trans.class)) {
					result.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return result;
	}

}