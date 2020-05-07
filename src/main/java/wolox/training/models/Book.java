package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Book model is using to save books data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Book {

    /**
     * Book's auto generated identification
     */
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Book's genre; can be horror, drama, etc.
     */
    @Column
    @ApiModelProperty(notes = "The book genre: could be horror, comedy, drama, etc.")
    private String genre;

    /**
     * Author's name of the book
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String author;

    /**
     * URL's image
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String image;

    /**
     * Book's title
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String title;

    /**
     * Book's subtitle
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String subtitle;

    /**
     * Book's publisher
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String publisher;

    /**
     * Book's publishing year
     */
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String year;

    /**
     * Book's quantity pages
     */
    @Column(nullable = false)
    @NotNull
    @Positive
    private int pages;

    /**
     * Book's ISBN
     */
    @Column(nullable = false, unique = true)
    @NotNull
    @NotEmpty
    private String isbn;

    /**
     * Users that have the book
     */
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private List<User> users;

    /**
     * Set genre of a book
     *
     * @param genre
     */
    public void setGenre(String genre) {
        checkNotNull(genre);
        this.genre = genre;
    }

    /**
     * Set author of a book
     *
     * @param author
     */
    public void setAuthor(String author) {
        checkNotNull(author);
        this.author = author;
    }

    /**
     * Set cover photo of the book
     *
     * @param image
     */
    public void setImage(String image) {
        checkNotNull(image);
        this.image = image;
    }

    /**
     * Set title of the book
     *
     * @param title
     */
    public void setTitle(String title) {
        checkNotNull(title);
        this.title = title;
    }

    /**
     * Set subtitle of the book
     *
     * @param subtitle
     */
    public void setSubtitle(String subtitle) {
        checkNotNull(subtitle);
        this.subtitle = subtitle;
    }

    /**
     * Set publisher of the book
     *
     * @param publisher
     */
    public void setPublisher(String publisher) {
        checkNotNull(publisher);
        this.publisher = publisher;
    }

    /**
     * Set year of the book
     *
     * @param year
     */
    public void setYear(String year) {
        checkNotNull(year);
        this.year = year;
    }

    /**
     * Set pages of the book
     *
     * @param pages
     */
    public void setPages(int pages) {
        checkNotNull(pages);
        checkArgument(pages > 0);
        this.pages = pages;
    }

    /**
     * Set ISBN of the book
     *
     * @param isbn
     */
    public void setIsbn(String isbn) {
        checkNotNull(isbn);
        this.isbn = isbn;
    }
}
