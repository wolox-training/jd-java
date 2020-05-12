package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
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

    /**
     * Find first record by isbn
     *
     * @param isbn isbn's name
     * @return optional book
     */
    Optional<Book> findFirstByIsbn(String isbn);

    /**
     * Find a book by publisher, year and genre
     *
     * @param publisher book's publisher
     * @param year      book's year
     * @param genre     book's genre
     * @return Optional<List < book>>
     */
    Optional<List<Book>> findByPublisherAndYearAndGenre(String publisher, String year,
        String genre);

}
