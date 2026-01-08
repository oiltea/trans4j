package io.github.oiltea.trans4j.core.exception;

/**
 * 字典翻译异常。
 * <p>
 * 在字典翻译过程中发生的异常，如字段不存在、字段访问失败等。
 *
 * @author oiltea
 * @since 1.0.0
 */
public class TransException extends RuntimeException {

	/**
	 * 构造异常。
	 *
	 * @param message 异常消息
	 */
	public TransException(String message) {
		super(message);
	}

	/**
	 * 构造异常。
	 *
	 * @param message 异常消息
	 * @param cause   原因异常
	 */
	public TransException(String message, Throwable cause) {
		super(message, cause);
	}

}