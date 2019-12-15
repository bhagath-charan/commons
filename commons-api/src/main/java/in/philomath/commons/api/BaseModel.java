package in.philomath.commons.api;

import in.philomath.commons.api.data.annotations.Searchable;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;

import static in.philomath.commons.api.Constants.*;

/**
 * BaseModel
 *
 * @author Bhagath Charan 15-12-2019
 */
@Data
public class BaseModel implements Serializable {

	protected String id;

	@Searchable(exactMatch = true)
	protected String uid;

	protected Status status = Status.ACTIVE;

	@CreatedDate
	protected DateTime createdAt;

	@LastModifiedDate
	protected DateTime lastModifiedAt;

	protected String createdBy;

	protected String lastUpdatedBy;

	protected String organizationUid;
}
