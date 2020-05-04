package wolox.training.models;

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
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.annotations.NotNullConstraint;

/**
 * Book model is using to save books data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
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
    @NotNullConstraint
    private String author;

    /**
     * URL's image
     */
    @Column(nullable = false)
    @NotNullConstraint
    private String image;

    /**
     * Book's title
     */
    @Column(nullable = false)
    @NotNullConstraint
    private String title;

    /**
     * Book's subtitle
     */
    @Column(nullable = false)
    @NotNullConstraint
    private String subtitle;

    /**
     * Book's publisher
     */
    @Column(nullable = false)
    @NotNullConstraint
    private String publisher;

    /**
     * Book's publishing year
     */
    @Column(nullable = false)
    @NotNullConstraint
    private String year;

    /**
     * Book's quantity pages
     */
    @Column(nullable = false)
    @NotNullConstraint
    private int pages;

    /**
     * Book's ISBN
     */
    @Column(nullable = false, unique = true)
    @NotNullConstraint
    private String isbn;

    /**
     * Users that have the book
     */
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private List<User> users;

    public Book(String author, String image, String title, String subtitle, String publisher,
        String year, int pages, String isbn) {
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;
    }
}
