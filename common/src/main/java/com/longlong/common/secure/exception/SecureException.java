
package com.longlong.common.secure.exception;

import lombok.Getter;
import com.longlong.common.api.IResultCode;
import com.longlong.common.api.ResultCode;

import java.io.Serial;

/**
 * Secure异常
 *
 * 
 */
public class SecureException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 2359767895161832954L;

	@Getter
	private final IResultCode resultCode;

	public SecureException(String message) {
		super(message);
		this.resultCode = ResultCode.UN_AUTHORIZED;
	}

	public SecureException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
	}

	public SecureException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
