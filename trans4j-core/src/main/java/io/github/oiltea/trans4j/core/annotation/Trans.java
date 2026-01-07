package io.github.oiltea.trans4j.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Trans {

	/**
	 * 字典类型
	 */
	String key() default "";

	/**
	 * 翻译的值最终会映射到该refs的字段，注意该字段必须存在于实体
	 */
	String[] refs() default {};

	/**
	 * 当不指定 ref 时，代理模式字段后缀
	 */
	String suffix() default "Name";

}