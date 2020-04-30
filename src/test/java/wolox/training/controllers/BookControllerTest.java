package wolox.training.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import wolox.training.factories.BookFactory;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    private Book book;

    private ArrayList<Book> bookList;

    private HashMap<String, Object> bookMap;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

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
    public void whenGetOneBookAndFound_thenReturnJson() throws Exception {
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));

        this.mockMvc
            .perform(get("/api/books/" + this.book.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(this.objectMapper.writeValueAsString(this.book)));
    }

    @Test
    public void whenGetOneBookAndNotFound_thenReturnJsonError() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(get("/api/books/" + this.book.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenSavesABook_thenReturnBookJson() throws Exception {
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
    public void whenSavesABookWithoutRequiredParams_thenReturnBookJson() throws Exception {
        when(this.bookRepository.save(this.book)).thenThrow(NullPointerException.class);

        this.mockMvc
            .perform(post("/api/books")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenDeletesABook_thenReturnNoContent() throws Exception {
        given(this.bookRepository.findById(this.book.getId())).willReturn(Optional.of(this.book));

        this.mockMvc
            .perform(delete("/api/books/" + this.book.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeletesABookAndNotFound_thenReturnJsonError() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(delete("/api/books/" + this.book.getId()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdatesABook_thenReturnBookJson() throws Exception {
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
    public void whenUpdatesABookButBookIdMismatch_thenReturnJsonError() throws Exception {
        this.mockMvc
            .perform(put("/api/books/9999")
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenUpdateABookAndNotFound_thenReturnJsonError() throws Exception {
        given(this.bookRepository.findById(this.book.getId()))
            .willReturn(Optional.empty());

        this.mockMvc
            .perform(put("/api/books/" + this.book.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .characterEncoding("utf-8")
                         .content(this.objectMapper.writeValueAsString(this.bookMap)))
            .andExpect(status().is4xxClientError());
    }
}
