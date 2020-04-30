package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.factories.BookFactory;
import wolox.training.models.Book;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    private Faker faker = new Faker();

    private Book book;

    @Before
    public void setup() {
        Map<String, Object> bookMap = new BookFactory().book();

        this.book = new Book(
            bookMap.get("author").toString(),
            bookMap.get("image").toString(),
            bookMap.get("title").toString(),
            bookMap.get("subtitle").toString(),
            bookMap.get("publisher").toString(),
            bookMap.get("year").toString(),
            Integer.parseInt(bookMap.get("pages").toString()),
            bookMap.get("isbn").toString()
        );
        this.testEntityManager.persist(this.book);
        this.testEntityManager.flush();
    }

    @Test
    public void whenSearchABookByAuthor_thenBookShouldFound() {
        Optional<Book> bookFound = this.bookRepository.findFirstByAuthor(this.book.getAuthor());

        assertThat(bookFound.get().getId()).isNotNull();
        assertThat(bookFound.get().getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(bookFound.get().getImage()).isEqualTo(this.book.getImage());
        assertThat(bookFound.get().getTitle()).isEqualTo(this.book.getTitle());
        assertThat(bookFound.get().getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(bookFound.get().getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(bookFound.get().getYear()).isEqualTo(this.book.getYear());
        assertThat(bookFound.get().getPages()).isEqualTo(this.book.getPages());
        assertThat(bookFound.get().getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByAuthor_thenBookNotFound() {
        Optional<Book> bookFound = this.bookRepository.findFirstByAuthor(faker.book().author());

        assertThat(bookFound.orElse(null)).isNull();
    }

}
