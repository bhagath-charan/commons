package in.philomath.commons.launcher.web;

import in.philomath.commons.api.PlatformException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * created by Bhagath Charan on 21-12-2019
 */
@RestControllerAdvice
public class PlatformRestResponseEntityException {

	@ExceptionHandler({PlatformException.class})
	protected ResponseEntity<Object> handleException(PlatformException e) {
		return ResponseEntity
				.status(e.getErrorCode().getHttpStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(new PlatformExceptionWrapper(e));
	}
}
