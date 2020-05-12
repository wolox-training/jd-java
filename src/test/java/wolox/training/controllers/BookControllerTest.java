package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import wolox.training.config.SecurityConfigTest;
import wolox.training.factories.BookFactory;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {SecurityConfigTest.class, BookController.class})
public class BookControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OpenLibraryService openLibraryService;

    @MockBean
    private BookRepository bookRepository;

    private Book book;

    private ArrayList<Book> bookList;

    private HashMap<String, Object> bookMap;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        BookFactory bookFactory = new BookFactory();
        this.bookMap = bookFactory.book();
        this.book = bookFactory.bookModel(this.bookMap);
        this.bookList = new ArrayList<Book>(Arrays.asList(
            this.book
        ));
    }


    @Test
    public void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        given(this.bookRepository.findAll()).willReturn(this.bookList);

        this.mockMvc.perform(get("/api/books").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(this.objectMapper.writeValueAsString(this.bookList)));
    }

    @Test
    public void whenGetOneBookAndFound_thenReturnJsonObject() throws Exception {
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));

        this.mockMvc
            .perform(get("/api/books/" + this.book.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(this.objectMapper.writeValueAsString(this.book)));
    }

    @Test
    public void whenGetOneBookAndNotFound_thenReturnBookNotFoundException() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(get("/api/books/" + this.book.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenSavesABook_thenReturnBookJsonObject() throws Exception {
        given(this.bookRepository.save(this.book)).willReturn(this.book);

        this.mockMvc
            .perform(post("/api/books")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test(expected = NestedServletException.class)
    public void whenSavesABookWithoutRequiredParams_thenReturnNullPointerException()
        throws Exception {
        when(this.bookRepository.save(this.book)).thenThrow(NullPointerException.class);

        this.mockMvc
            .perform(post("/api/books")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenDeletesABook_thenReturnNoContentHttpStatus() throws Exception {
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));

        this.mockMvc
            .perform(delete("/api/books/" + this.book.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletesABookAndNotFound_thenReturnBookNotFoundException() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(delete("/api/books/" + this.book.getId()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdatesABook_thenReturnBookJsonObject() throws Exception {
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));
        given(this.bookRepository.save(this.book)).willReturn(this.book);

        this.mockMvc
            .perform(put("/api/books/" + this.book.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdatesABookButBookIdMismatch_thenReturnBookIdMismatchException()
        throws Exception {
        this.mockMvc
            .perform(put("/api/books/9999")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdateABookAndNotFound_thenReturnBookNotFoundException() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(put("/api/books/" + this.book.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenSearchABookByISBNAndFoundAtDataBase_thenReturnJSONObject()
        throws Exception {
        this.book.setIsbn("test");

        given(this.bookRepository.findFirstByIsbn(this.book.getIsbn()))
            .willReturn(Optional.of(this.book));

        this.mockMvc
            .perform(get("/api/books/search?isbn=" + this.book.getIsbn()))
            .andDo(print())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenSearchABookByISBNAndFoundAtExternalService_thenReturnJSONObject()
        throws Exception {
        given(this.bookRepository.findFirstByIsbn("0451526538")).willReturn(Optional.empty());

        this.mockMvc
            .perform(get("/api/books/search?isbn=0451526538"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenSearchABookByISBNAndNotFound_thenReturnBookNotFoundException()
        throws Exception {
        given(this.bookRepository.findFirstByIsbn("0000000009")).willReturn(Optional.empty());

        this.mockMvc
            .perform(get("/api/books/search?isbn=0000000009"))
            .andExpect(status().is4xxClientError());
    }
}
