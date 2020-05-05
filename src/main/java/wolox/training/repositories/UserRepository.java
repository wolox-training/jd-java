package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

/**
 * User repository
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find first record by user's name
     *
     * @param name author's name
     * @return optional user
     */
    Optional<User> findFirstByName(String name);

}
