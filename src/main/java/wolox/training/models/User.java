package wolox.training.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(name = "users_books",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> books;

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) throws BookAlreadyOwnedException {
        if (haveDuplicateBooks(books)) {
            throw new BookAlreadyOwnedException();
        }

        this.books = books;
    }

    private boolean haveDuplicateBooks(List<Book> books) {
        return countDuplicates(this.books, books) != 0 || countDuplicates(books, books) != 0;
    }

    private long countDuplicates(List<Book> searchIn, List<Book> search) {
        return searchIn.stream().filter(book -> Collections.frequency(search, book) > 1).count();
    }
}
