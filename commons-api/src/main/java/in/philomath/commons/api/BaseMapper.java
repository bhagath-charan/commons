package in.philomath.commons.api;

import in.philomath.commons.api.BaseDto;
import in.philomath.commons.api.BaseModel;

import java.util.List;

/**
 * BaseMapper maps from DTO to MODEL and vice versa
 *
 * @author Bhagath Charan 15-12-2019
 */
public interface BaseMapper<T extends BaseDto, E extends BaseModel> {

	T asDto(E model);

	E asModel(T dto);

	List<T> asDtoList(List<E> modelList);

	List<E> asModelList(List<T> dtoList);
}
