package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Find first record by user's username
     *
     * @param username author's username
     * @return optional user
     */
    Optional<User> findFirstByUsername(String username);

    /**
     * Find users with birth date between two dates and name contains certain characteres
     *
     * @param startDate
     * @param endDate
     * @param name
     * @return Optional<List < User>>
     */
    Optional<List<User>> findByBirthDateBetweenAndNameContainingIgnoreCase(LocalDate startDate,
        LocalDate endDate,
        String name);
}
