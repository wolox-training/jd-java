package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api
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
    @ApiOperation(value = "Return all users with books collection", response = Iterable.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return all users")})
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
    @ApiOperation(value = "Return user", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Return all users"),
        @ApiResponse(code = 404, message = "User Not Found")
    })
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
    @ApiOperation(value = "Return user created", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Return user created")
    })
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
    @ApiOperation(value = "Deletes correctly")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Deletes correctly"),
        @ApiResponse(code = 404, message = "User Not Found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
     * @throws UserIdMismatchException
     * @throws UserNotFoundException
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Returns user updated")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updates user correctly"),
        @ApiResponse(code = 400, message = "User Id mismatch"),
        @ApiResponse(code = 404, message = "User Not Found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @PathVariable long id)
        throws UserIdMismatchException, UserNotFoundException {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.save(user);
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
    @ApiOperation(value = "Returns user with added book", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updates user correctly"),
        @ApiResponse(code = 404, message = "User Not Found"),
        @ApiResponse(code = 404, message = "Book Not Found"),
    })
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
    @ApiOperation(value = "Returns user with removed book", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updates user correctly"),
        @ApiResponse(code = 404, message = "User Not Found"),
        @ApiResponse(code = 404, message = "Book Not Found"),
    })
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
