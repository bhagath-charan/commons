package in.philomath.commons.launcher.web;

import in.philomath.commons.api.PlatformException;
import lombok.Data;

/**
 * created by Bhagath Charan on 21-12-2019
 */
@Data
public class PlatformExceptionWrapper {

	private String status = "ERROR";

	private String code;

	private String description;

	private Object data;

	private Object stackTrace;

	public PlatformExceptionWrapper(PlatformException pe) {
		this.code = pe.getErrorCode().getCode();
		this.description = pe.getErrorCode().getDescription();
		this.data = pe.toString();
		this.stackTrace = pe.getStackTrace()[0];
	}
}
