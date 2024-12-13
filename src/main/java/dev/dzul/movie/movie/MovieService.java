package dev.dzul.movie.movie;

import dev.dzul.movie.genre.Genre;
import dev.dzul.movie.genre.GenreRepository;
import dev.dzul.movie.genre.GenreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByIdAsc();
    }

    public List<Movie > findAllMoviesByTitle(String genre, String title) { return movieRepository.findAllByGenre_GenreNameContainingIgnoreCaseOrTitleContainingIgnoreCase(genre, title); } // <Movie>

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        mapDtoToEntity(movieDTO, movie);
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = getMovieById(id);
        if (existingMovie != null) {
            var updatedMovie = mapDtoToEntity(movieDTO, existingMovie);
            return movieRepository.save(updatedMovie);
        }
        return null;
    }



    public void deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Movie with id: "+id+ "is not found");
        }
    }

    private Movie mapDtoToEntity(MovieDTO movieDTO, Movie movie) {
        BeanUtils.copyProperties(movieDTO, movie);
        // Fetch Genre by id_genre and set it to Movie
        Genre genre = genreRepository.findById(movieDTO.getId_genre())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        movie.setGenre(genre);
        return movie;
    }

    private ResponseDTO mapEntityToDto(Movie movie) {
        ResponseDTO dto = new ResponseDTO();
        BeanUtils.copyProperties(movie, dto);
        dto.setGenre_name(movie.getGenre() != null ? movie.getGenre().getGenreName() : null);
        return dto;
    }

}
