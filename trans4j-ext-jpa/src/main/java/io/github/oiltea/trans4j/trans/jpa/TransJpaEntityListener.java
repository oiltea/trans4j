package io.github.oiltea.trans4j.trans.jpa;

import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.trans.TransService;
import jakarta.persistence.PostLoad;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA 实体监听器，在 {@code @PostLoad} 阶段对带有 {@link Trans} 注解的字段进行字典翻译。
 * <p>
 * 使用方式：在实体类上添加 {@code @EntityListeners(TransJpaEntityListener.class)}。
 */
public class TransJpaEntityListener {

	@PostLoad
	public void postLoad(Object entity) throws Exception {
		TransService transService = SpringTransSupport.getTransService();
		if (transService == null || entity == null) {
			return;
		}
		List<Field> transFields = resolveTransFields(entity.getClass());
		if (transFields.isEmpty()) {
			return;
		}
		transService.trans(entity, transFields);
	}

	/**
	 * 解析类及父类中带 {@link Trans} 注解的字段。
	 */
	protected List<Field> resolveTransFields(Class<?> clazz) {
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
	 * 通过 Spring 容器获取 {@link TransService}。
	 */
	@Component
	static class SpringTransSupport implements ApplicationContextAware {

		private static ApplicationContext CONTEXT;

		private static TransService TRANS_SERVICE;

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			CONTEXT = applicationContext;
			TRANS_SERVICE = applicationContext.getBean(TransService.class);
		}

		static TransService getTransService() {
			return TRANS_SERVICE != null && CONTEXT != null ? TRANS_SERVICE : null;
		}

	}

}