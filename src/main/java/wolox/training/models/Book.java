package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
    private String genre;

    /**
     * Author's name of the book
     */
    @Column(nullable = false)
    private String author;

    /**
     * URL's image
     */
    @Column(nullable = false)
    private String image;

    /**
     * Book's title
     */
    @Column(nullable = false)
    private String title;

    /**
     * Book's subtitle
     */
    @Column(nullable = false)
    private String subtitle;

    /**
     * Book's publisher
     */
    @Column(nullable = false)
    private String publisher;

    /**
     * Book's publishing year
     */
    @Column(nullable = false)
    private String year;

    /**
     * Book's quantity pages
     */
    @Column(nullable = false)
    private int pages;

    /**
     * Book's ISBN
     */
    @Column(nullable = false, unique = true)
    private String isbn;

    /**
     * Users that have the book
     */
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private List<User> users;

}
