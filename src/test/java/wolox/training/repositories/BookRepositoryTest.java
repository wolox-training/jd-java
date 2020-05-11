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

    @Test
    public void whenSearchABookByISBN_thenBookShouldFound() {
        Optional<Book> bookFound = this.bookRepository.findFirstByIsbn(this.book.getIsbn());

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
    public void whenSearchABookByISBN_thenBookNotFound() {
        Optional<Book> bookFound = this.bookRepository.findFirstByIsbn(faker.book().author());

        assertThat(bookFound.orElse(null)).isNull();
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenre_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(this.book.getPublisher(),
                                    this.book.getYear(), this.book.getGenre());

        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenreAndPublisherIsNull_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(null,
                                    this.book.getYear(), this.book.getGenre());

        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenreAndYearIsNull_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(this.book.getPublisher(),
                                    null, this.book.getGenre());

        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenreAndGenreIsNull_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(this.book.getPublisher(),
                                    this.book.getYear(), null);

        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenreAndAllIsNull_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenSearchABookByPublisherAndYearAndGenre_thenBookNotFound() {
        Optional<Book> bookFound = this.bookRepository.findFirstByIsbn(faker.book().author());
        Book[] booksFound = this.bookRepository
                                .findByPublisherAndYearAndGenre(faker.book().author(),
                                    "1234", faker.book().author());

        assertThat(booksFound.length).isEqualTo(0);
    }

    @Test
    public void whenFindByAllParameters_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, null, null, null, null,
                                    null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterById_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(this.book.getId(), null, null, null, null,
                                    null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByGenre_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, this.book.getGenre(), null, null, null,
                                    null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByAuthor_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, this.book.getAuthor(), null, null,
                                    null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByTitle_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, this.book.getTitle(), null,
                                    null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterBySubtitle_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, null,
                                    this.book.getSubtitle(), null, null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByPublisher_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, null, null,
                                    this.book.getPublisher(), null, null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByYear_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, null, null, null,
                                    this.book.getYear(), null);

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

    @Test
    public void whenFindByAllParametersFilterByIsbn_thenBookShouldFound() {
        Book[] booksFound = this.bookRepository
                                .findByAllParameters(null, null, null, null, null, null, null,
                                    this.book.getIsbn());

        assertThat(booksFound.length).isGreaterThan(0);
        assertThat(booksFound[0].getId()).isNotNull();
        assertThat(booksFound[0].getAuthor()).isEqualTo(this.book.getAuthor());
        assertThat(booksFound[0].getImage()).isEqualTo(this.book.getImage());
        assertThat(booksFound[0].getTitle()).isEqualTo(this.book.getTitle());
        assertThat(booksFound[0].getSubtitle()).isEqualTo(this.book.getSubtitle());
        assertThat(booksFound[0].getPublisher()).isEqualTo(this.book.getPublisher());
        assertThat(booksFound[0].getYear()).isEqualTo(this.book.getYear());
        assertThat(booksFound[0].getPages()).isEqualTo(this.book.getPages());
        assertThat(booksFound[0].getIsbn()).isEqualTo(this.book.getIsbn());
    }

}
