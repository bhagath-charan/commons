package in.philomath.commons.core.service;

import in.philomath.commons.api.*;
import in.philomath.commons.api.data.annotations.Searchable;
import in.philomath.commons.api.lifecycle.ModelLifecycleListener;
import in.philomath.commons.api.search.SearchDto;
import in.philomath.commons.core.repository.BaseRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static in.philomath.commons.api.Constants.ErrorCode.INVALID_REQUEST;
import static in.philomath.commons.api.Constants.ErrorCode.NOT_FOUND;
import static in.philomath.commons.api.Constants.Status;
import static in.philomath.commons.api.Constants.Status.*;

/**
 * created by Bhagath Charan on 15-12-2019
 */
@Data
@Slf4j
public abstract class AbstractService<T extends BaseDto, E extends BaseModel> implements BaseService<T> {

	@Autowired
	protected MongoOperations mongoOperations;

	protected Class<T> dtoClazz;

	protected Class<E> modelClazz;

	protected Map<String, Searchable> searchProperties = new HashMap<>();

	public abstract BaseRepository<E> getRepository();

	public abstract BaseMapper<T, E> getMapper();

	public ModelLifecycleListener<T, E> getLifecycleListener() {
		return null;
	}

	public T create(T dto) throws PlatformException {

		if (dto == null) {
			log.error("Cannot create a record with null values");
			throw PlatformException.of(INVALID_REQUEST);
		}

		if (getRepository().existsByUid(dto.getUid())) {
			log.error("Cannot create record with an already existing UID [{}]", dto.getUid());
			throw PlatformException.of(INVALID_REQUEST);
		}

		if (getLifecycleListener() != null) {
			getLifecycleListener().beforeCreate(dto);
		}

		E model = getMapper().asModel(dto);
		getRepository().insert(model);

		if (log.isDebugEnabled()) {
			log.debug("Successfully created entity with id {}", model.getId());
		}

		if (getLifecycleListener() != null) {
			getLifecycleListener().afterCreate(dto, model);
		}
		return getMapper().asDto(model);
	}

	public T update(T dto) throws PlatformException {
		if (dto == null || dto.getId() == null) {
			log.error("Cannot update an entity with null identifier");
			throw PlatformException.of(INVALID_REQUEST);
		}

		Optional<E> entityContainer = getRepository().findById(dto.getId());

		if (!entityContainer.isPresent()) {
			log.error("Cannot update a non existing entity");
			throw PlatformException.of(NOT_FOUND);
		}

		E entity = entityContainer.get();

		if (!entity.getUid().equals(dto.getUid()) && getRepository().existsByUid(dto.getUid())) {
			log.error("Cannot update uid to an existing uid {}", dto.getUid());
			throw PlatformException.of(INVALID_REQUEST);
		}

		if (getLifecycleListener() != null) {
			getLifecycleListener().beforeUpdate(dto);
		}

		E modelIn = getMapper().asModel(dto);

		getRepository().save(modelIn);

		if (log.isDebugEnabled()) {
			log.debug("Successfully updated entity with id {}", modelIn.getId());
		}

		if (getLifecycleListener() != null) {
			getLifecycleListener().afterUpdate(dto, modelIn);
		}

		return getMapper().asDto(modelIn);
	}

	public T findById(String id) throws PlatformException {
		if (id == null) {
			if (log.isDebugEnabled()) {
				log.debug("Id cannot be null while querying for an entity by id");
			}
			return null;
		}

		Optional<E> entityContainer = getRepository().findById(id);
		return entityContainer.map(e -> getMapper().asDto(e)).orElse(null);
	}

	public T findByUid(String uid) throws PlatformException {
		if (uid == null) {
			if (log.isDebugEnabled()) {
				log.debug("UID cannot be null while querying for an entity by uid");
			}
			return null;
		}

		Optional<E> entityContainer = getRepository().findOneByUid(uid);
		return entityContainer.map(e -> getMapper().asDto(e)).orElse(null);
	}

	public Page<T> findAll(Pageable pageable) {
		return getRepository().findAll(pageable).map(entity -> getMapper().asDto(entity));
	}

	public List<T> findAll() {
		List<E> modelList;
		if (getRepository().count() > 1000) {
			modelList = getRepository().findFirst1000ByStatus(ACTIVE);
		} else {
			modelList = getRepository().findAll();
		}
		return getMapper().asDtoList(modelList);
	}

	public List<T> findAllByStatus(Status status) {
		if (status == null) {
			if (log.isDebugEnabled()) {
				log.debug("Invalid Status while querying for an entity");
			}
			return null;
		}
		List<E> modelList;
		modelList = getRepository().findAllByStatus(status);
		return getMapper().asDtoList(modelList);
	}

	public void changeStatus(String id, Status newStatus) {
		Optional<E> optionalEntity = getRepository().findById(id);
		if (optionalEntity.isPresent()) {
			E entity = optionalEntity.get();
			entity.setStatus(newStatus);
			getRepository().save(entity);

			if (log.isDebugEnabled()) {
				log.debug("Successfully updated status to {}", newStatus);
			}
		}
	}

	public void deleteById(String id) {
		getRepository().deleteById(id);
		if (log.isDebugEnabled()) {
			log.debug("Successfully deleted entity with id {}", id);
		}
	}

	public List<T> findAll(String query) {
		List<E> modelList;
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage();
		textCriteria.matchingAny(query);
		modelList = getRepository().findAllBy(textCriteria);
		return getMapper().asDtoList(modelList);
	}

	public Page<T> findAll(Pageable pageable, String query) {
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage();
		textCriteria.matchingAny(query);
		List<Criteria> criterias = new ArrayList<Criteria>();

		for (String key : searchProperties.keySet()) {
			criterias.add(Criteria.where(key).regex(".*" + query + ".*", "i"));
		}

		Query q =
				new Query().addCriteria(new Criteria().orOperator(criterias.toArray(new Criteria[criterias.size()]))).with(pageable);

		List<E> models = mongoOperations.find(q, this.getModelClazz());
		Page<E> modeslPaginated = PageableExecutionUtils.getPage(models,
				pageable,
				() -> mongoOperations.count(q, this.getModelClazz()));

		return modeslPaginated.map(entity -> getMapper().asDto(entity));
	}

	public Page<T> advancedSearch(Pageable pageable, ArrayList<SearchDto> filterCriteria) {
		boolean emptyCriteria = true;
		TextCriteria textCriteria = TextCriteria.forDefaultLanguage();
		List<Criteria> criterias = new ArrayList<Criteria>();

		for (SearchDto searchDto : filterCriteria) {
			if (searchDto.getType().equals("text") && searchDto.getValue() != null && searchDto.getValue().trim().length() > 0) {
				switch (searchDto.getSearchType()) {
					case "Starts with":
						criterias.add(Criteria.where(searchDto.getKey()).regex("^" + searchDto.getValue(), "i"));
						break;
					case "Ends with":
						criterias.add(Criteria.where(searchDto.getKey()).regex(searchDto.getValue() + "$", "i"));
						break;
					case "Anywhere":
						criterias.add(Criteria.where(searchDto.getKey()).regex(".*" + searchDto.getValue() + ".*", "i"));
						break;
				}
				emptyCriteria = false;
			}
			if (searchDto.getType().equals("entity") && searchDto.getValues() != null && searchDto.getValues().size() > 0) {
				ArrayList<ObjectId> objectIds = new ArrayList<ObjectId>();
				for (String value : searchDto.getValues()) {
					objectIds.add(new ObjectId(value));
				}
				criterias.add(Criteria.where(searchDto.getKey() + ".$id").in(objectIds));
				emptyCriteria = false;
			}
		}
		if (emptyCriteria) {
			return getRepository().findAll(pageable).map(entity -> getMapper().asDto(entity));
		} else {
			Query q =
					new Query().addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

			List<E> models = mongoOperations.find(q, this.getModelClazz());
			Page<E> modelsPaginated = PageableExecutionUtils.getPage(models,
					pageable,
					() -> mongoOperations.count(q, this.getModelClazz()));

			return modelsPaginated.map(entity -> getMapper().asDto(entity));
		}
	}

	@PostConstruct
	public void initialize() {
		Type[] types =
				((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();

		this.dtoClazz = (Class<T>) types[0];
		this.modelClazz = (Class<E>) types[1];
		for (Field field : BaseModel.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(Searchable.class)) {
				this.searchProperties.put(field.getName(), field.getAnnotation(Searchable.class));
			}
		}

		Field[] fields = this.modelClazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Searchable.class)) {
				this.searchProperties.put(field.getName(), field.getAnnotation(Searchable.class));
			}
		}
	}
}
