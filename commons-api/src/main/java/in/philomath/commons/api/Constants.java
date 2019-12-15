package in.philomath.commons.api;

/**
 * Constants
 *
 * @author Bhagath Charan 15-12-2019
 */
public interface Constants {

	/**
	 * Status of entity
	 */
	enum Status {
		ACTIVE,
		INACTIVE,
		NEW,
		PENDING,
		INPROGRESS,
		COMPLETED,
		ERROR,
		ENABLED,
		DISABLED,
		OPEN,
		CLOSED,
		SUSPENDED
	}

	/**
	 * Error Codes with code,httpStatus and description.
	 */
	enum ErrorCode {

		SYSTEM_ERROR("0000", 500, "System Error"),
		INVALID_REQUEST("0001", 400, "Invalid Request"),
		NOT_FOUND("0002", 404, "Object not found");

		/**
		 * code -error code to be used
		 */
		private final String code;

		/**
		 * httpStatus - HTTP STATUS Code translation of the error code.
		 * For example if we get an error that corresponds to something that cannot be found,
		 * httpStatusCode for such an Error will be 404.
		 */
		private final int httpStatus;

		/**
		 * description - Description of the error. This string will be used to set the exception
		 * message.
		 */
		private final String description;

		ErrorCode(String code, int httpStatus, String description) {
			this.code = code;
			this.httpStatus = httpStatus;
			this.description = description;
		}

		public int getHttpStatus() {
			return this.httpStatus;
		}

		public String getCode() {
			return this.code;
		}

		public String getDescription() {
			return this.description;
		}
	}
}
