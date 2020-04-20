package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Entry point to user's IO database operations
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Entry point to book's IO database operations
     */
    @Autowired
    private BookRepository bookRepository;

    /**
     * Get all users
     *
     * @return Iterable with all users
     */
    @GetMapping
    public Iterable findAll() {
        return userRepository.findAll();
    }

    /**
     * Shows an user by id
     *
     * @param id identification's user
     * @return User's model
     * @throws UserNotFoundException
     */
    @GetMapping("/{id}")
    public User findOne(@PathVariable long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Creates an user
     *
     * @param user body params request
     * @return User's model
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes an user
     *
     * @param id identification's user
     * @throws UserNotFoundException
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * Updates a book
     *
     * @param user body params request
     * @param id   identifications's user
     * @return User's model
     * @throws UserIdMismatchException
     * @throws UserNotFoundException
     */
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable long id)
        throws UserIdMismatchException, UserNotFoundException {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * Add a book to user's collection
     *
     * @param id     user's identification
     * @param bookId book's identification
     * @return User's model
     * @throws UserNotFoundException
     * @throws BookNotFoundException
     * @throws BookAlreadyOwnedException
     */
    @PostMapping("/{id}/books/{bookId}/add")
    public User addBook(@PathVariable long id, @PathVariable long bookId)
        throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException {
        User user = userRepository.findById(id)
                        .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(bookId)
                        .orElseThrow(BookNotFoundException::new);

        user.addBook(book);
        return userRepository.save(user);
    }

    /**
     * Remove book from user's collection
     *
     * @param id     user's identification
     * @param bookId book's identification
     * @return User's model
     * @throws UserNotFoundException
     * @throws BookNotFoundException
     */
    @PostMapping("/{id}/books/{bookId}/remove")
    public User removeBook(@PathVariable long id, @PathVariable long bookId)
        throws UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(id)
                        .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(bookId)
                        .orElseThrow(BookNotFoundException::new);

        user.removeBook(book);
        return userRepository.save(user);
    }

}
