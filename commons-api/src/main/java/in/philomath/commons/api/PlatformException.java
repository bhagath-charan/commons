package in.philomath.commons.api;

import lombok.Data;

import static in.philomath.commons.api.Constants.*;

/**
 * Base Platform Exception
 *
 * @author Bhagath Charan 15-12-2019
 */
@Data
public class PlatformException extends Exception {

	private final ErrorCode errorCode;

	private final String description;

	public PlatformException(ErrorCode errorCode) {
		super(errorCode.getDescription());
		this.description = errorCode.getDescription();
		this.errorCode = errorCode;
	}

	public PlatformException(ErrorCode errorCode, String message) {
		super(message);
		this.description = message;
		this.errorCode = errorCode;
	}

	public PlatformException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode.getDescription(), throwable);
		this.description = errorCode.getDescription();
		this.errorCode = errorCode;
	}

	public static PlatformException of(ErrorCode errorCode) {
		return new PlatformException(errorCode);
	}
}
