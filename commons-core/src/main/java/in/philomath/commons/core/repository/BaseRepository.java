package in.philomath.commons.core.repository;

import in.philomath.commons.api.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

import static in.philomath.commons.api.Constants.Status;

/**
 * created by Bhagath Charan on 15-12-2019
 */
public interface BaseRepository<E extends BaseModel> extends MongoRepository<E, String> {

	boolean existsByUid(String uid);

	Optional<E> findOneByUid(String uid);

	List<E> findAllByStatus(Status status);

	List<E> findFirst1000ByStatus(Status status);

	List<E> findAllBy(TextCriteria textCriteria);

	Page<E> findAllBy(TextCriteria textCriteria, Pageable pageable);
}
