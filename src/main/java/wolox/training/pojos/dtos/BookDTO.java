package wolox.training.pojos.dtos;

import java.text.ParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import wolox.training.models.Book;

@Setter
@Getter
@AllArgsConstructor
public class BookDTO {

    private long id;

    private String genre;

    private String author;

    private String image;

    private String title;

    private String subtitle;

    private String publisher;

    private String year;

    private int pages;

    private String isbn;

    public Book convertToEntity() throws ParseException {
        ModelMapper modelMapper = new ModelMapper();
        Book book = modelMapper.map(this, Book.class);
        return book;
    }

}
