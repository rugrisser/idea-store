package edu.mshp.ideastore.respository;

import edu.mshp.ideastore.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCrudRepository extends CrudRepository<User, Long> {
    List<User> findAllByLoginOrEmail(String login, String email);
}
