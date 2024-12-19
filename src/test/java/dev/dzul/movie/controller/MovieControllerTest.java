package dev.dzul.movie.controller;

import dev.dzul.movie.movie.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    private Movie movie;
    private MovieDTO movieDTO;
    private ResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller");
        movie.setRating(9);
        movie.setPhoto("inception.jpg");
        movie.setVideoUrl("inception-trailer.mp4");
        movie.setReleaseDate(new Date());
        movie.setPrice(100);

        movieDTO = new MovieDTO();
        movieDTO.setTitle("Inception");
        movieDTO.setDescription("A mind-bending thriller");
        movieDTO.setRating(9);
        movieDTO.setPhoto("inception.jpg");
        movieDTO.setVideoUrl("inception-trailer.mp4");
        movieDTO.setReleaseDate(new Date());
        movieDTO.setPrice(100);

        responseDTO = new ResponseDTO(1L, "Thriller", "Inception", "Warner Bros",
                "A mind-bending thriller", "Excellent movie", 9, "inception.jpg",
                "inception-trailer.mp4", new Date(), 100);
    }

    // Test: Get all movies - 200 OK
    @Test
    void testGetAllMovies_200Ok() throws Exception {
        List<ResponseDTO> movies = new ArrayList<>();
        movies.add(responseDTO);

        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].title").value("Inception"));

        verify(movieService, times(1)).getAllMovies();
    }

    // Test: Get movie by ID - 200 OK
    @Test
    void testGetMovieById_200Ok() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(movie);
        when(movieService.mapEntityToDto(movie)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/movie/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.title").value("Inception"));

        verify(movieService, times(1)).getMovieById(1L);
    }

    // Test: Get movie by ID - 404 Not Found
    @Test
    void testGetMovieById_404NotFound() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/movie/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Movie not found"));

        verify(movieService, times(1)).getMovieById(1L);
    }

    // Test: Create movie - 201 Created
    @Test
    void testCreateMovie_201Created() throws Exception {
        when(movieService.createMovie(any(MovieDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Inception",
                                    "description": "A mind-bending thriller",
                                    "rating": 9,
                                    "photo": "inception.jpg",
                                    "videoUrl": "inception-trailer.mp4",
                                    "releaseDate": "2024-12-18",
                                    "price": 100
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.title").value("Inception"));

        verify(movieService, times(1)).createMovie(any(MovieDTO.class));
    }

    // Test: Create movie - 500 Internal Server Error
    @Test
    void testCreateMovie_500InternalServerError() throws Exception {
        when(movieService.createMovie(any(MovieDTO.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Inception",
                                    "description": "A mind-bending thriller",
                                    "rating": 9,
                                    "photo": "inception.jpg",
                                    "videoUrl": "inception-trailer.mp4",
                                    "releaseDate": "2024-12-18",
                                    "price": 100
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An error occurred while creating the movie"));

        verify(movieService, times(1)).createMovie(any(MovieDTO.class));
    }

    // Test: Delete movie - 200 OK
    @Test
    void testDeleteMovie_200Ok() throws Exception {
        doNothing().when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/api/movie/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Movie deleted successfully"));

        verify(movieService, times(1)).deleteMovie(1L);
    }

    // Test: Delete movie - 500 Internal Server Error
    @Test
    void testDeleteMovie_500InternalServerError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/api/movie/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Movie with id: 1 is not found"));

        verify(movieService, times(1)).deleteMovie(1L);
    }
}

