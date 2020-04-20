package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

/**
 * Book repository
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Find first record by author
     *
     * @param author author's name
     * @return optional book
     */
    Optional<Book> findFirstByAuthor(String author);

}
