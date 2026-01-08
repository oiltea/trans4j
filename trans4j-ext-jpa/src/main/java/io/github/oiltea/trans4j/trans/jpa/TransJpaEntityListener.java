package io.github.oiltea.trans4j.trans.jpa;

import io.github.oiltea.trans4j.core.trans.TransService;
import io.github.oiltea.trans4j.core.util.FieldResolver;
import jakarta.persistence.PostLoad;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * JPA 实体监听器，在 {@code @PostLoad} 阶段对带有 {@link io.github.oiltea.trans4j.core.annotation.Trans}
 * 注解的字段进行字典翻译。
 * <p>
 * <b>使用方式：</b>
 * <pre>{@code
 * @Entity
 * @EntityListeners(TransJpaEntityListener.class)
 * public class User {
 *     @Trans(key = "GENDER")
 *     private String gender;
 *     private String genderName;  // 自动填充
 * }
 * }</pre>
 * <p>
 * <b>工作原理：</b>
 * <ul>
 *     <li>JPA 在加载实体后触发 {@code @PostLoad} 回调</li>
 *     <li>监听器自动检测实体中带 {@code @Trans} 注解的字段</li>
 *     <li>调用 {@link TransService} 执行字典翻译</li>
 * </ul>
 *
 * @author oiltea
 * @since 1.0.0
 */
public class TransJpaEntityListener {

	/**
	 * JPA 实体加载后的回调方法。
	 * <p>
	 * 自动对实体中带 {@code @Trans} 注解的字段进行字典翻译。
	 *
	 * @param entity 加载的实体对象
	 * @throws Exception 翻译过程中的异常
	 */
	@PostLoad
	public void postLoad(Object entity) throws Exception {
		if (entity == null) {
			return;
		}

		TransService transService = SpringTransSupport.getTransService();
		if (transService == null) {
			// TransService 未注册，跳过翻译（可能是非 Spring 环境或未配置）
			return;
		}

		List<Field> transFields = FieldResolver.resolveTransFields(entity.getClass());
		if (transFields.isEmpty()) {
			return;
		}

		transService.trans(entity, transFields);
	}

	/**
	 * Spring 容器支持类。
	 * <p>
	 * 用于在 JPA 监听器中获取 Spring 容器中的 {@link TransService} Bean。
	 * <p>
	 * <b>注意：</b>由于 JPA 监听器通常由 JPA 实现（如 Hibernate）直接实例化，
	 * 无法直接注入 Spring Bean，因此需要通过 {@link ApplicationContextAware} 获取。
	 */
	@Component
	static class SpringTransSupport implements ApplicationContextAware {

		private static volatile ApplicationContext CONTEXT;

		private static volatile TransService TRANS_SERVICE;

		@Override
		public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
			CONTEXT = applicationContext;
			try {
				TRANS_SERVICE = applicationContext.getBean(TransService.class);
			}
			catch (Exception e) {
				// TransService 可能未配置，忽略
			}
		}

		/**
		 * 获取 {@link TransService} 实例。
		 *
		 * @return TransService 实例，若未配置则返回 {@code null}
		 */
		static TransService getTransService() {
			if (TRANS_SERVICE != null && CONTEXT != null) {
				return TRANS_SERVICE;
			}
			// 延迟获取（支持动态注册）
			if (CONTEXT != null) {
				try {
					TRANS_SERVICE = CONTEXT.getBean(TransService.class);
					return TRANS_SERVICE;
				}
				catch (Exception e) {
					return null;
				}
			}
			return null;
		}

	}

}