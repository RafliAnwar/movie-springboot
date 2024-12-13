package dev.dzul.movie.genre;

import dev.dzul.movie.movie.Movie;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "genres")
@Data
public class Genre {

    @Id
    private Long id;

    @Column(nullable = false)
    private String genreName;

//    @OneToMany(mappedBy = "genre", cascade = CascadeType.DETACH, orphanRemoval = true)
//    List <Movie> movies;

}
