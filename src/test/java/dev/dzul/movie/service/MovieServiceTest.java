package dev.dzul.movie.service;

import dev.dzul.movie.genre.Genre;
import dev.dzul.movie.genre.GenreRepository;
import dev.dzul.movie.movie.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    private Movie movie;
    private MovieDTO movieDTO;
    private Genre genre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        genre = new Genre();
        genre.setId(1L);
        genre.setGenreName("Thriller");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller");
        movie.setRating(9);
        movie.setPhoto("inception.jpg");
        movie.setVideoUrl("inception-trailer.mp4");
        movie.setReleaseDate(new Date());
        movie.setPrice(100);
        movie.setGenre(genre);

        movieDTO = new MovieDTO();
        movieDTO.setId_genre(1L);
        movieDTO.setTitle("Inception");
        movieDTO.setDescription("A mind-bending thriller");
        movieDTO.setRating(9);
        movieDTO.setPhoto("inception.jpg");
        movieDTO.setVideoUrl("inception-trailer.mp4");
        movieDTO.setReleaseDate(new Date());
        movieDTO.setPrice(100);
    }

    // Test: Get all movies - 200 OK
    @Test
    void testGetAllMovies_200Ok() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        when(movieRepository.findAllByOrderByIdAsc()).thenReturn(movies);

        List<ResponseDTO> result = movieService.getAllMovies();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());

        verify(movieRepository, times(1)).findAllByOrderByIdAsc();
    }

    // Test: Get movie by ID - 200 OK
    @Test
    void testGetMovieById_200Ok() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(1L);
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());

        verify(movieRepository, times(1)).findById(1L);
    }

    // Test: Get movie by ID - 404 Not Found
    @Test
    void testGetMovieById_404NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        Movie result = movieService.getMovieById(1L);
        assertNull(result);

        verify(movieRepository, times(1)).findById(1L);
    }

    // Test: Create movie - 201 Created
    @Test
    void testCreateMovie_201Created() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        ResponseDTO result = movieService.createMovie(movieDTO);
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals("Thriller", result.getGenre_name());

        verify(genreRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    // Test: Create movie - 500 Internal Server Error
    @Test
    void testCreateMovie_500InternalServerError() {
        when(genreRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.createMovie(movieDTO);
        });

        assertEquals("Database error", exception.getMessage());

        verify(genreRepository, times(1)).findById(1L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    // Test: Update movie - 200 OK
    @Test
    void testUpdateMovie_200Ok() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        ResponseDTO result = movieService.updateMovie(1L, movieDTO);
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());

        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    // Test: Update movie - 404 Not Found
    @Test
    void testUpdateMovie_404NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.updateMovie(1L, movieDTO);
        });

        assertEquals("Movie with id 1 not found.", exception.getMessage());

        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    // Test: Delete movie - 200 OK
    @Test
    void testDeleteMovie_200Ok() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    // Test: Delete movie - 500 Internal Server Error
    @Test
    void testDeleteMovie_500InternalServerError() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.deleteMovie(1L);
        });

        assertEquals("Movie with id: 1is not found", exception.getMessage());

        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, never()).deleteById(1L);
    }
}
