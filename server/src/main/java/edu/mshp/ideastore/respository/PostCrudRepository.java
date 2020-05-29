package edu.mshp.ideastore.respository;

import edu.mshp.ideastore.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCrudRepository extends CrudRepository<Post, Long> {
}
