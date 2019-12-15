package in.philomath.commons.api.lifecycle;

import in.philomath.commons.api.BaseDto;
import in.philomath.commons.api.BaseModel;

/**
 * Created by BhagathCharan on 15/12/19.
 */
public interface ModelLifecycleListener<T extends BaseDto, E extends BaseModel> {

	default void beforeCreate(T dto) {

	}

	default void afterCreate(T dto, E model) {

	}

	default void beforeUpdate(T dto) {

	}

	default void afterUpdate(T dto, E model) {

	}
}
