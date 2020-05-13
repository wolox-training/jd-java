package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
    @Query(value =
               "SELECT u FROM User u WHERE (:name IS NULL OR LOWER(u.name) LIKE CONCAT('%',LOWER(cast(:name as string)),'%')) AND "
                   + "(CAST(:start_date AS date) IS NULL OR CAST(:end_date AS date) IS NULL OR "
                   + "u.birthDate BETWEEN :start_date AND :end_date)")
    Optional<List<User>> findByBirthDateBetweenAndNameContainingIgnoreCase(
        @Param("start_date") LocalDate startDate,
        @Param("end_date") LocalDate endDate,
        @Param("name") String name);

    /**
     * Show all users with pagination
     *
     * @param pageable
     * @return Page<User>
     */
    Page<User> findAll(Pageable pageable);
}
