package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
    @Query(
        "SELECT b FROM Book b WHERE (:publisher IS NULL OR publisher = :publisher) "
            + "AND (:year IS NULL OR year = :year) AND (:genre IS NULL OR genre = :genre)")
    Optional<List<Book>> findByPublisherAndYearAndGenre(@Param("publisher") String publisher,
        @Param("year") String year, @Param("genre") String genre);

    /**
     * Find by all parameters optional
     *
     * @param id
     * @param genre
     * @param author
     * @param title
     * @param subtitle
     * @param publisher
     * @param year
     * @param isbn
     * @return
     */
    @Query("SELECT b FROM Book b WHERE (:id IS NULL OR b.id = :id) AND "
               + "(:genre IS NULL OR b.genre = :genre) AND "
               + "(:author IS NULL OR b.author = :author) AND "
               + "(:title IS NULL OR b.title = :title) AND "
               + "(:subtitle IS NULL OR b.subtitle = :subtitle) AND "
               + "(:publisher IS NULL OR b.publisher = :publisher) AND "
               + "(:year IS NULL OR b.year = :year) AND "
               + "(:isbn IS NULL OR b.isbn = :isbn)")
    List<Book> findByAllParameters(
        @Param("id") Long id,
        @Param("genre") String genre,
        @Param("author") String author,
        @Param("title") String title,
        @Param("subtitle") String subtitle,
        @Param("publisher") String publisher,
        @Param("year") String year,
        @Param("isbn") String isbn);

}
