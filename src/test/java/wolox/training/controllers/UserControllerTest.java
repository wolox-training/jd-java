package wolox.training.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import wolox.training.config.SecurityConfigTest;
import wolox.training.factories.BookFactory;
import wolox.training.factories.UserFactory;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {SecurityConfigTest.class, UserController.class})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    private User user;

    private ArrayList<User> userList;

    private HashMap<String, Object> userMap;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Book book;

    private HashMap<String, Object> bookMap;

    @Before
    public void setup() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        this.userMap = new UserFactory().user();
        this.user = new User(
            this.userMap.get("username").toString(),
            this.userMap.get("name").toString(),
            LocalDate.parse(this.userMap.get("birth_date").toString()),
            this.userMap.get("password").toString()
        );
        this.user.setBooks(new ArrayList<>());

        this.bookMap = new BookFactory().book();
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

        this.userList = new ArrayList<User>(Arrays.asList(
            this.user
        ));
    }

    @Test
    public void whenGetAllUsers_thenReturnJsonArray() throws Exception {
        given(this.userRepository.findAll()).willReturn(this.userList);

        this.mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name", is(this.user.getName())))
            .andExpect(jsonPath("$[0].username", is(this.user.getUsername())))
            .andExpect(jsonPath("$[0].birth_date", is(
                this.user.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            )));
    }

    @Test
    public void whenGetOneUserAndFound_thenReturnJson() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));

        this.mockMvc
            .perform(get("/api/users/" + this.user.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", is(this.user.getName())))
            .andExpect(jsonPath("$.username", is(this.user.getUsername())))
            .andExpect(jsonPath("$.birth_date", is(
                this.user.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            )));
    }

    @Test
    public void whenGetOneUserAndNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(get("/api/users/" + this.user.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenSavesAUser_thenReturnUserJson() throws Exception {
        given(this.userRepository.save(this.user)).willReturn(this.user);

        this.mockMvc
            .perform(post("/api/users")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.userMap)))
            .andExpect(status().isCreated());
    }

    @Test(expected = NestedServletException.class)
    public void whenSavesAUserWithoutRequiredParams_thenReturnUserJson() throws Exception {
        when(this.userRepository.save(Mockito.any())).thenThrow(NullPointerException.class);

        this.mockMvc
            .perform(post("/api/users")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(
                             this.objectMapper.writeValueAsString(this.userMap)))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenDeletesAUser_thenReturnNoContent() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));

        this.mockMvc
            .perform(delete("/api/users/" + this.user.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletesAUserAndNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(delete("/api/users/" + this.user.getId()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdatesAUser_thenReturnUserJson() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.userRepository.save(this.user)).willReturn(this.user);

        this.mockMvc
            .perform(put("/api/users/" + this.user.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.userMap)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdatesAUserButUserIdMismatch_thenReturnJsonError() throws Exception {
        this.mockMvc
            .perform(put("/api/users/9999")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.userMap)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdateAUserAndNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(put("/api/users/" + this.user.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.userMap)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenAddBook_thenReturnJson() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));
        given(this.userRepository.save(this.user)).willReturn(this.user);

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().isOk());
    }

    @Test
    public void whenAddBookButUserNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId()))
            .willReturn(Optional.empty());

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenAddBookButBookNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.empty());

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenAddBookButBookAlreadyOwned_thenReturnJsonError() throws Exception {
        this.user.addBook(this.book);
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenRemoveABook_thenReturnJson() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));
        given(this.userRepository.save(this.user)).willReturn(this.user);

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().isOk());
    }

    @Test
    public void whenRemoveABookButUserNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId()))
            .willReturn(Optional.empty());

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenRemoveABookButBookNotFound_thenReturnJsonError() throws Exception {
        given(this.userRepository.findById(this.user.getId())).willReturn(Optional.of(this.user));
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.empty());

        this.mockMvc.perform(
            post("/api/users/" + this.user.getId() + "/books/" + this.book.getId() + "/add")
        )
            .andExpect(status().is4xxClientError());
    }
}
