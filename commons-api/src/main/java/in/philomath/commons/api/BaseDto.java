package in.philomath.commons.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * BaseDto
 *
 * @author Bhagath Charan 14-12-2019
 */

@Data
@NoArgsConstructor
public class BaseDto implements Serializable {

	protected String id;

	protected String uid;

	protected String status;
}
