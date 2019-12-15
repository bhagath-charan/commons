package in.philomath.commons.api;

import in.philomath.commons.api.search.SearchDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * created by Bhagath Charan on 15-12-2019
 */
@Data
@Slf4j
public abstract class AbstractController<T extends BaseDto> {

	public abstract BaseService<T> getService();

	/**
	 * POST to create i.e., to save an entity.
	 *
	 * @param dto entity to be saved
	 * @return created dto
	 */
	@PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<T> create(@RequestBody T dto) {
		try {
			dto = getService().create(dto);
		} catch (Exception e) {
			log.error("There is an error while saving entity [message: {}]", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(dto);
	}

	/**
	 * POST /{id} To update an entity with id
	 *
	 * @param dto entity to be updated
	 * @param id  id of the entity
	 * @return updated entity
	 */
	@PostMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE, produces =
			APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<T> update(@RequestBody T dto, @PathVariable("id") String id) {
		if (dto == null || !id.equalsIgnoreCase(dto.getId())) {
			return ResponseEntity.badRequest().build();
		}
		try {
			dto = getService().update(dto);
		} catch (Exception e) {
			log.error("There is an error while updating entity [message: {}]", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(dto);
	}

	/**
	 * GET /{id} To find an entity with id
	 *
	 * @param id id of entity to be fetched
	 * @return entity if found or httpStatus 404 if not found.
	 * @throws PlatformException exception
	 */
	@GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<T> findOne(@PathVariable("id") String id) throws PlatformException {
		T dto = getService().findById(id);
		if (dto != null)
			return ResponseEntity.ok(dto);
		return ResponseEntity.notFound().build();
	}

	/**
	 * GET /uid/{uid} To find an entity with uid
	 *
	 * @param uid uid of entity to be fetched
	 * @return entity if found or httpStatus 404 if not found.
	 * @throws PlatformException exception
	 */
	@GetMapping(path = "/uid/{uid}",
			produces = APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<T> findByUid(@PathVariable("uid") String uid) throws PlatformException {
		T model = getService().findByUid(uid);
		if (model != null)
			return ResponseEntity.ok(model);
		return ResponseEntity.notFound().build();

	}

	/**
	 * To get records with size,page and query
	 *
	 * @param size  page size,default is 25
	 * @param page  pages to be delivered,default is 0
	 * @param query optional, search query
	 * @return paginated records based on params provided
	 */
	@GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
	public Page<T> findAllPaginated(@RequestParam(value = "size", defaultValue = "25") int size,
	                                @RequestParam(value = "page", defaultValue = "0") int page,
	                                @RequestParam(value = "query", required = false) String query) {
		if (null != query && !("".equalsIgnoreCase(query.trim()))) {
			return getService().findAll(PageRequest.of(page, size), query);
		} else {
			return getService().findAll(PageRequest.of(page, size));
		}
	}

	/**
	 * To get all record from the related repository
	 *
	 * @return list of all entities
	 */
	@GetMapping(path = "/all",
			produces = APPLICATION_JSON_UTF8_VALUE)
	public List<T> findAll() {
		return getService().findAll();
	}

	/**
	 * To delete an entity by id
	 *
	 * @param id id of entity that needs to be deleted
	 * @return 204 noContent
	 */
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		getService().deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * To find all entities with status.
	 *
	 * @param status status with which the entities needs to be fetched.
	 * @return list of matched entities.
	 */
	@GetMapping(path = "/all/{status}",
			produces = APPLICATION_JSON_UTF8_VALUE)
	public List<T> findAllByStatus(@PathVariable("status") String status) {
		return getService().findAllByStatus(Constants.Status.valueOf(status.toUpperCase()));
	}

	/**
	 * To change the status of the entity
	 *
	 * @param id        id of entity whose status needs to be changed.
	 * @param newStatus new status to be updated.
	 * @return if change is success returns 204 else 400
	 */
	@PutMapping(path = "/{id}/status/{newStatus}")
	public ResponseEntity<Void> changeStatus(@PathVariable("id") String id,
	                                         @PathVariable("newStatus") Constants.Status newStatus) {
		if (newStatus != null) {
			getService().changeStatus(id, newStatus);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * To find all entities with particular query
	 *
	 * @param query with which the entities are fetched
	 * @return all records that matched the query.
	 */
	@GetMapping(path = "/search",
			produces = APPLICATION_JSON_UTF8_VALUE)
	public List<T> findAll(@RequestParam(value = "query") String query) {
		return getService().findAll(query);
	}

	/**
	 * To search for the records with multiple search criteria
	 *
	 * @param size           no.of records
	 * @param page           no.of pages
	 * @param filterCriteria filter criteria
	 * @return list of entities that matched the filter criteria
	 */
	@PostMapping(path = "/advanced-search",
			consumes = APPLICATION_JSON_UTF8_VALUE,
			produces = APPLICATION_JSON_UTF8_VALUE)
	public Page<T> advancedSearch(@RequestParam(value = "size", defaultValue = "25") int size,
	                              @RequestParam(value = "page", defaultValue = "0") int page,
	                              @RequestBody ArrayList<SearchDto> filterCriteria) {
		if (null != filterCriteria) {
			return getService().advancedSearch(PageRequest.of(page, size), filterCriteria);
		}
		return null;
	}
}
