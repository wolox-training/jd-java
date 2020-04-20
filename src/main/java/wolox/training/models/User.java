package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(name = "users_books",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false))
    private List<Book> books;

    public List<Book> getBooks() {
        if (books != null) {
            return (List<Book>) Collections.unmodifiableList(books);
        } else {
            return new ArrayList<Book>();
        }
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        if (this.books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }

        this.books.add(book);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
    }
}
