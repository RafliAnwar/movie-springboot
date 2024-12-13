package dev.dzul.movie.movie;

import dev.dzul.movie.genre.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "movies")
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_genre")
    private Genre genre;

    @Column(nullable = false)
    private String title;

    @Column
    private String studio;

    @Column(nullable = false)
    private String description;

    @Column
    private String review;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private Date releaseDate;

}
