package in.philomath.commons.api;

import in.philomath.commons.api.search.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static in.philomath.commons.api.Constants.Status;

/**
 * BaseService
 *
 * @author Bhagath Charan 15-12-2019
 */
public interface BaseService<T extends BaseDto> {
	T create(T entity) throws PlatformException;

	T update(T entity) throws PlatformException;

	T findById(String id) throws PlatformException;

	T findByUid(String id) throws PlatformException;

	List<T> findAll();

	Page<T> findAll(Pageable pageable);

	List<T> findAll(String query);

	Page<T> findAll(Pageable pageable, String query);

	List<T> findAllByStatus(Status status);

	void changeStatus(String id, Status status);

	void deleteById(String id);

	Page<T> advancedSearch(Pageable pageable, ArrayList<SearchDto> filterCriteria);
}
