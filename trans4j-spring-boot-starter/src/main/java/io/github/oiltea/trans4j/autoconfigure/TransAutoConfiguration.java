package io.github.oiltea.trans4j.autoconfigure;

import io.github.oiltea.trans4j.core.trans.DefaultTransService;
import io.github.oiltea.trans4j.core.trans.DictProvider;
import io.github.oiltea.trans4j.core.trans.TransService;
import io.github.oiltea.trans4j.trans.mybatis.TransMybatisInterceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * trans4j Spring Boot 自动配置类。
 * <p>
 * <b>自动配置内容：</b>
 * <ul>
 *     <li>{@link TransService}：当存在 {@link DictProvider} Bean 时自动创建</li>
 *     <li>{@link TransMybatisInterceptor}：当存在 MyBatis 和 {@link TransService} 时自动注册</li>
 * </ul>
 * <p>
 * <b>使用方式：</b>
 * <ol>
 *     <li>引入 {@code trans4j-spring-boot-starter} 依赖</li>
 *     <li>实现 {@link DictProvider} 接口并注册为 Spring Bean</li>
 *     <li>在实体类字段上使用 {@code @Trans} 注解</li>
 * </ol>
 * <p>
 * <b>JPA 使用：</b>
 * <pre>{@code
 * @Entity
 * @EntityListeners(TransJpaEntityListener.class)
 * public class User {
 *     @Trans(key = "GENDER")
 *     private String gender;
 * }
 * }</pre>
 * <p>
 * <b>MyBatis 使用：</b>
 * <pre>{@code
 * public class UserVO {
 *     @Trans(key = "GENDER")
 *     private String gender;
 *     private String genderName;  // 自动填充
 * }
 * }</pre>
 *
 * @author oiltea
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnBean(DictProvider.class)
@EnableConfigurationProperties(TransProperties.class)
public class TransAutoConfiguration {

	/**
	 * 创建 {@link TransService} 默认实现。
	 * <p>
	 * 当用户未自定义 {@link TransService} 时，使用 {@link DefaultTransService}。
	 *
	 * @param dictProvider 字典提供者
	 * @return TransService 实例
	 */
	@Bean
	@ConditionalOnMissingBean(TransService.class)
	public TransService transService(DictProvider dictProvider) {
		return new DefaultTransService(dictProvider);
	}

	/**
	 * 创建 MyBatis 拦截器。
	 * <p>
	 * 当检测到 MyBatis 和 {@link TransService} 存在时，自动创建拦截器。
	 *
	 * @param transService 翻译服务
	 * @return MyBatis 拦截器
	 */
	@Bean
	@ConditionalOnBean(TransService.class)
	@ConditionalOnClass({ TransMybatisInterceptor.class, Configuration.class })
	@ConditionalOnProperty(name = "trans4j.mybatis.enabled", havingValue = "true", matchIfMissing = true)
	public TransMybatisInterceptor transMybatisInterceptor(TransService transService) {
		return new TransMybatisInterceptor(transService);
	}

	/**
	 * 注册 MyBatis 拦截器到配置中。
	 *
	 * @param interceptor MyBatis 拦截器
	 * @return 配置定制器
	 */
	@Bean
	@ConditionalOnBean(TransMybatisInterceptor.class)
	@ConditionalOnClass({ TransMybatisInterceptor.class, Configuration.class })
	public ConfigurationCustomizer transConfigurationCustomizer(TransMybatisInterceptor interceptor) {
		return configuration -> configuration.addInterceptor(interceptor);
	}

}