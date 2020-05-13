package wolox.training.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

/**
 * Entry point to books routes
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    /**
     * Entry point to IO database operations
     */
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Get all books
     *
     * @return Book[]
     */
    @GetMapping
    @ApiOperation(value = "Return all books", response = Book[].class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return all books")})
    public Page<Book> findAll(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String genre,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String subtitle,
        @RequestParam(required = false) String publisher,
        @RequestParam(required = false) String year,
        @RequestParam(required = false) String isbn,
        Pageable pageable
    ) {
        return bookRepository
                   .findByAllParameters(id, genre, author, title, subtitle, publisher, year, isbn,
                       pageable);
    }

    /**
     * Shows a book by id
     *
     * @param id identification's book
     * @return Book's model
     * @throws BookNotFoundException
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Return book searched", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Return book"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    public Book findOne(@PathVariable long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    /**
     * Creates a book
     *
     * @param book body params request
     * @return Book's model
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Return book created", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Return book created")
    })
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * Deletes a book
     *
     * @param id identification's book
     * @throws BookNotFoundException
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes correctly")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Deletes correctly"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws BookNotFoundException {
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * Updates a book
     *
     * @param book body params request
     * @param id   identifications's book
     * @throws BookIdMismatchException
     * @throws BookNotFoundException
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Returns book updated")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Updates user correctly"),
        @ApiResponse(code = 400, message = "Book Id mismatch"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Book book, @PathVariable long id)
        throws BookIdMismatchException,

                   BookNotFoundException {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.save(book);
    }

    /**
     * Shows a greeting message
     *
     * @param name  request param to show on greeting message
     * @param model model object to add response data for view
     * @return show greeting view html
     */
    public String greeting(@RequestParam(name = "name", defaultValue = "world") String name,
        Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * Search a book by isbn
     *
     * @return Book
     */
    @GetMapping("/search")
    @ApiOperation(value = "Return Book", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Book Found"),
        @ApiResponse(code = 201, message = "Book Found in external service and created"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    public ResponseEntity<Book> search(@RequestParam(name = "isbn") String isbn)
        throws InterruptedException, IOException, URISyntaxException, BookNotFoundException, ParseException {
        Optional<Book> book = bookRepository.findFirstByIsbn(isbn);

        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                bookRepository
                    .save((this.openLibraryService.bookInformation(isbn))
                              .convertToEntity()),
                HttpStatus.CREATED);
        }
    }

}
