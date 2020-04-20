package wolox.training.controllers;

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

    /**
     * Get all books
     *
     * @return Iterable with all books
     */
    @GetMapping
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    /**
     * Shows a book by id
     *
     * @param id identification's book
     * @return Book's model
     * @throws BookNotFoundException
     */
    @GetMapping("/{id}")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Book book, @PathVariable long id)
        throws BookIdMismatchException, BookNotFoundException {
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

}
