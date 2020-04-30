package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;
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

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;

    private Map<String, Object> userMap;

    @Before
    public void setUp() {
        this.userMap = new UserFactory().user();

        this.user = new User(
            this.userMap.get("username").toString(),
            this.userMap.get("name").toString(),
            LocalDate.parse(this.userMap.get("birth_date").toString())
        );
    }

    @Test
    public void whenSaveUser_thenUserShouldFound() {
        User savedUser = this.testEntityManager.persistFlushFind(this.user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(this.userMap.get("username").toString());
        assertThat(savedUser.getName()).isEqualTo(this.userMap.get("name").toString());
        assertThat(savedUser.getBirthDate())
            .isEqualTo(LocalDate.parse(this.userMap.get("birth_date").toString()));
    }

}
