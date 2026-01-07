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
import org.springframework.context.annotation.Bean;

/**
 * trans4j Spring Boot 自动配置。
 * <p>
 * - 提供 {@link TransService} 的默认实现 {@link DefaultTransService}；<br>
 * - 检测到 MyBatis 时自动注册 {@link TransMybatisInterceptor}。
 */
@AutoConfiguration
public class TransAutoConfiguration {

	@Bean
	@ConditionalOnBean(DictProvider.class)
	@ConditionalOnMissingBean(TransService.class)
	public TransService transService(DictProvider dictProvider) {
		return new DefaultTransService(dictProvider);
	}

	/**
	 * MyBatis 集成：自动注册拦截器。
	 */
	@Bean
	@ConditionalOnBean(TransService.class)
	@ConditionalOnClass({ TransMybatisInterceptor.class, Configuration.class })
	public TransMybatisInterceptor transMybatisInterceptor(TransService transService) {
		return new TransMybatisInterceptor(transService);
	}

	@Bean
	@ConditionalOnBean(TransMybatisInterceptor.class)
	@ConditionalOnClass({ TransMybatisInterceptor.class, Configuration.class })
	public ConfigurationCustomizer transConfigurationCustomizer(TransMybatisInterceptor interceptor) {
		return configuration -> configuration.addInterceptor(interceptor);
	}

}