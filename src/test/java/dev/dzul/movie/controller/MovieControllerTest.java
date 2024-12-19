package dev.dzul.movie.controller;

import dev.dzul.movie.movie.*;
import dev.dzul.movie.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    private MovieDTO movieDTO;
    private ResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock MovieDTO
        movieDTO = new MovieDTO();
        movieDTO.setId_genre(1L);
        movieDTO.setTitle("Inception");
        movieDTO.setStudio("Warner Bros");
        movieDTO.setDescription("A mind-bending thriller");
        movieDTO.setRating(9);
        movieDTO.setPhoto("inception.jpg");
        movieDTO.setVideoUrl("inception-trailer.mp4");
        movieDTO.setReleaseDate(new Date());
        movieDTO.setPrice(100);

        // Mock ResponseDTO
        responseDTO = new ResponseDTO(1L, "Thriller", "Inception", "Warner Bros",
                "A mind-bending thriller", "Excellent movie", 9, "inception.jpg",
                "inception-trailer.mp4", new Date(), 100);
    }

    @Test
    void testGetAllMovies_200Ok() {
        List<ResponseDTO> movies = new ArrayList<>();
        movies.add(responseDTO);

        when(movieService.getAllMovies()).thenReturn(movies);

        ResponseEntity<ResponseFormatter<List<ResponseDTO>>> response = movieController.getAllMovies();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movies fetched successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals("Inception", response.getBody().getData().get(0).getTitle());
        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void testGetMovieById_200Ok() {
        when(movieService.getMovieById(1L)).thenReturn(new Movie());
        when(movieService.mapEntityToDto(any(Movie.class))).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = movieController.getMovieById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movie fetched successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals("Inception", response.getBody().getData().getTitle());
        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void testGetMovieById_404NotFound() {
        when(movieService.getMovieById(1L)).thenReturn(null);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = movieController.getMovieById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Movie not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void testCreateMovie_201Created() {
        when(movieService.createMovie(any(MovieDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = movieController.createMovie(movieDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Movie created successfully", response.getBody().getMessage());
        assertEquals(201, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals("Inception", response.getBody().getData().getTitle());
        verify(movieService, times(1)).createMovie(any(MovieDTO.class));
    }

    @Test
    void testCreateMovie_500InternalServerError() {
        when(movieService.createMovie(any(MovieDTO.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = movieController.createMovie(movieDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while creating the movie", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(movieService, times(1)).createMovie(any(MovieDTO.class));
    }

    @Test
    void testDeleteMovie_200Ok() {
        doNothing().when(movieService).deleteMovie(1L);

        ResponseEntity<ResponseFormatter<Void>> response = movieController.deleteMovie(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movie deleted successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        verify(movieService, times(1)).deleteMovie(1L);
    }

    @Test
    void testDeleteMovie_500InternalServerError() {
        doThrow(new RuntimeException("Database error")).when(movieService).deleteMovie(1L);

        ResponseEntity<ResponseFormatter<Void>> response = movieController.deleteMovie(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Movie with id: "+1L+" is not found", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        verify(movieService, times(1)).deleteMovie(1L);
    }
}
