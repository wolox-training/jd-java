package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
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
@Data
@NoArgsConstructor
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
    @NotNull
    @NotEmpty
    private String username;

    /**
     * User's name
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String name;

    /**
     * User's birth date
     */
    @Column(nullable = false)
    @NotNull
    @Past
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    /**
     * Books that belongs to the user
     */
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Book> books;

    public User(String username, String name, LocalDate birthDate) {
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
    }

    /**
     * Set username of the user
     *
     * @param username
     */
    public void setUsername(String username) {
        checkNotNull(username);
        this.username = username;
    }

    /**
     * Set name of the user
     *
     * @param name
     */
    public void setName(String name) {
        checkNotNull(name);
        this.name = name;
    }

    /**
     * Set birth date of the user
     *
     * @param birthDate
     */
    public void setBirthDate(LocalDate birthDate) {
        checkNotNull(birthDate);
        checkArgument(birthDate.isBefore(ChronoLocalDate.from(LocalDate.now())));
        this.birthDate = birthDate;
    }

    /**
     * Get user's books
     *
     * @return List of books that belongs to the user
     */
    public List<Book> getBooks() {
        if (books != null) {
            return (List<Book>) Collections.unmodifiableList(books);
        } else {
            return new ArrayList<Book>();
        }
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
