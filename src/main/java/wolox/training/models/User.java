package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wolox.training.exceptions.BookAlreadyOwnedException;

/**
 * User model is using to save users data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class User {

    /**
     * User's auto generated identification
     */
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Identification's name
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * User's name
     */
    @Column(nullable = false)
    private String name;

    /**
     * User's birth date
     */
    @Column(nullable = false)
    private LocalDate birthDate;

    /**
     * Books that belongs to the user
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_books",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> books;

    /**
     * Get user's books
     *
     * @return List of books that belongs to the user
     */
    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    /**
     * Set user's book
     *
     * @param books books list to set
     * @throws BookAlreadyOwnedException
     */
    public void setBooks(List<Book> books) throws BookAlreadyOwnedException {
        if (haveDuplicateBooks(books)) {
            throw new BookAlreadyOwnedException();
        }

        this.books = books;
    }

    /**
     * Verify if books list have duplicates
     *
     * @param books books list
     * @return boolean that indicates if the list have duplicates
     */
    private boolean haveDuplicateBooks(List<Book> books) {
        return countDuplicates(this.books, books) != 0 || countDuplicates(books, books) != 0;
    }

    /**
     * Count duplicates in list from another list
     *
     * @param searchIn list to search in
     * @param search   list to search
     * @return integer duplicated count
     */
    private long countDuplicates(List<Book> searchIn, List<Book> search) {
        return searchIn.stream().filter(book -> Collections.frequency(search, book) > 1).count();
    }
}
