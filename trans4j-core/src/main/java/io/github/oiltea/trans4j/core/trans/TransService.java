package io.github.oiltea.trans4j.core.trans;

import java.lang.reflect.Field;
import java.util.List;

public interface TransService {

	/**
	 * 翻译
	 * @param vo 实现vo的实体
	 * @param transFieldList 要翻译的字段
	 * @throws Exception 抛出的异常
	 */
	void trans(Object vo, List<Field> transFieldList) throws Exception;

}