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

    public List<ResponseDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAllByOrderByIdAsc();
        return movies.stream().map(this::mapEntityToDto).toList(); // Convert Movie to ResponseDTO
    }

    public List<ResponseDTO> findAllMoviesByTitle(String genre, String title) {
        List<Movie> movies = movieRepository.findAllByGenre_GenreNameContainingIgnoreCaseOrTitleContainingIgnoreCase(genre, title);
        return movies.stream().map(this::mapEntityToDto).toList(); // Convert Movie to ResponseDTO
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public ResponseDTO createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        mapDtoToEntity(movieDTO, movie); // Map request body DTO to Entity
        Movie savedMovie = movieRepository.save(movie); // Save to the database
        return mapEntityToDto(savedMovie); // Map saved Entity to response DTO
    }

    public ResponseDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = getMovieById(id);
        if (existingMovie != null) {
            var updatedMovie = mapDtoToEntity(movieDTO, existingMovie); // Map request body DTO to Entity
            Movie savedMovie = movieRepository.save(updatedMovie); // Update in the database
            return mapEntityToDto(savedMovie); // Map updated Entity to response DTO
        } else {
            throw new RuntimeException("Movie with id " + id + " not found.");
        }
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

    public ResponseDTO mapEntityToDto(Movie movie) {
        ResponseDTO dto = new ResponseDTO();
        BeanUtils.copyProperties(movie, dto);
        dto.setGenre_name(movie.getGenre() != null ? movie.getGenre().getGenreName() : null);
        return dto;
    }

}
