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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

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
