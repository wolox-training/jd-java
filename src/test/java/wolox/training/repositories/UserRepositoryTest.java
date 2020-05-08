package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import java.time.LocalDate;
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
import wolox.training.factories.UserFactory;
import wolox.training.models.User;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Faker faker = new Faker();

    @Before
    public void setup() {
        Map<String, Object> userMap = new UserFactory().user();

        this.user = new User(
            userMap.get("username").toString(),
            userMap.get("name").toString(),
            LocalDate.parse(userMap.get("birth_date").toString())
        );
        this.testEntityManager.persist(this.user);
        this.testEntityManager.flush();
    }

    @Test
    public void whenSearchABookByAuthor_thenBookShouldFound() {
        Optional<User> userFound = this.userRepository.findFirstByName(this.user.getName());

        assertThat(userFound.get().getId()).isNotNull();
        assertThat(userFound.get().getUsername()).isEqualTo(this.user.getUsername().toString());
        assertThat(userFound.get().getName()).isEqualTo(this.user.getName().toString());
        assertThat(userFound.get().getBirthDate())
            .isEqualTo(LocalDate.parse(this.user.getBirthDate().toString()));
    }

    @Test
    public void whenSearchABookByAuthor_thenBookNotFound() {
        Optional<User> userFound = this.userRepository
                                       .findFirstByName(this.faker.name().fullName());

        assertThat(userFound.orElse(null)).isNull();
    }

}
