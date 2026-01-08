package io.github.oiltea.trans4j.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * trans4j 配置属性。
 * <p>
 * 支持通过 {@code application.yml} 或 {@code application.properties} 配置：
 * <pre>{@code
 * trans4j:
 *   mybatis:
 *     enabled: true  # 是否启用 MyBatis 拦截器（默认：true）
 * }</pre>
 *
 * @author oiltea
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "trans4j")
public class TransProperties {

	/**
	 * MyBatis 相关配置。
	 */
	private Mybatis mybatis = new Mybatis();

	public Mybatis getMybatis() {
		return mybatis;
	}

	public void setMybatis(Mybatis mybatis) {
		this.mybatis = mybatis;
	}

	/**
	 * MyBatis 配置。
	 */
	public static class Mybatis {

		/**
		 * 是否启用 MyBatis 拦截器。
		 */
		private boolean enabled = true;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

}

