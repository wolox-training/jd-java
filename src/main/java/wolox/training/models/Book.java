package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    @Column
    @ApiModelProperty(notes = "The book genre: could be horror, comedy, drama, etc.")
    private String genre;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private int pages;

    @Column(nullable = false, unique = true)
    private String isbn;

    @ManyToMany(mappedBy = "books")
    @JsonBackReference
    private List<User> users;
}
