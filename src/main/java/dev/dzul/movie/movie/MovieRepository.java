package dev.dzul.movie.movie;

import dev.dzul.movie.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

//    @Query
//    (value = "SELECT Movies (m.id, g.genre_name, m.title, m.studio, m.description, " +
//            "m.review, m.rating, m.photo, m.video_url, m.release_date) " +
//            "FROM Movies m JOIN Genres g ON m.id_genre = g.id", nativeQuery = true)


    List<Movie> findAllByOrderByIdAsc();

    List<Movie> findAllByGenre_GenreNameContainingIgnoreCaseOrTitleContainingIgnoreCase(String genre, String title);


}
