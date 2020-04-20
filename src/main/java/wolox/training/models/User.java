package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
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
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    /**
     * Books that belongs to the user
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "users_books",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false))
    @JsonManagedReference
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
     * Add book to user's collection
     *
     * @param book book's model
     * @throws BookAlreadyOwnedException
     */
    public void addBook(Book book) throws BookAlreadyOwnedException {
        if (this.books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }

        this.books.add(book);
    }

    /**
     * Remove book from user's collection
     *
     * @param book book's model
     */
    public void removeBook(Book book) {
        this.books.remove(book);
    }
}
