package edu.mshp.ideastore.respository;

import edu.mshp.ideastore.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCrudRepository extends CrudRepository<User, Long> {
}
