package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class BookTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Book book;

    private Faker faker = new Faker();

    private Map<String, Object> bookMap;

    @Before

    public void setUp() {
        Date date = faker.date().past(this.faker.random().nextInt(0, 1000), TimeUnit.DAYS);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
        calendar.setTime(date);

        this.bookMap = new HashMap<String, Object>() {{
            put("author", faker.book().author());
            put("image", faker.internet().image());
            put("title", faker.book().title());
            put("subtitle", faker.book().title());
            put("publisher", faker.book().publisher());
            put("year", calendar.get(Calendar.YEAR));
            put("pages", faker.number().digits(3));
            put("isbn", faker.hashCode());
        }};

        this.book = new Book(
            this.bookMap.get("author").toString(),
            this.bookMap.get("image").toString(),
            this.bookMap.get("title").toString(),
            this.bookMap.get("subtitle").toString(),
            this.bookMap.get("publisher").toString(),
            this.bookMap.get("year").toString(),
            Integer.parseInt(this.bookMap.get("pages").toString()),
            this.bookMap.get("isbn").toString()
        );
    }

    @Test
    public void whenSaveBook_thenBookShouldFound() {
        Book savedBook = this.testEntityManager.persistFlushFind(this.book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getAuthor()).isEqualTo(this.bookMap.get("author").toString());
        assertThat(savedBook.getImage()).isEqualTo(this.bookMap.get("image").toString());
        assertThat(savedBook.getTitle()).isEqualTo(this.bookMap.get("title").toString());
        assertThat(savedBook.getSubtitle()).isEqualTo(this.bookMap.get("subtitle").toString());
        assertThat(savedBook.getPublisher()).isEqualTo(this.bookMap.get("publisher").toString());
        assertThat(savedBook.getYear()).isEqualTo(this.bookMap.get("year").toString());
        assertThat(savedBook.getPages())
            .isEqualTo(Integer.parseInt(this.bookMap.get("pages").toString()));
        assertThat(savedBook.getIsbn()).isEqualTo(this.bookMap.get("isbn").toString());
    }

}
