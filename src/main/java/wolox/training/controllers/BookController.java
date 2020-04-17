package wolox.training.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    @ApiOperation(value = "Return all books", response = Iterable.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return all books")})
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Return all books"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    public Book findOne(@PathVariable long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Return book created", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Return book created")
    })
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes correctly")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Deletes correctly"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    public void delete(@PathVariable Long id) throws BookNotFoundException {
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Returns book updated", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updates user correctly"),
        @ApiResponse(code = 400, message = "Book Id mismatch"),
        @ApiResponse(code = 404, message = "Book Not Found")
    })
    public Book update(@RequestBody Book book, @PathVariable long id)
        throws BookIdMismatchException, BookNotFoundException {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", defaultValue = "world") String name,
        Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
