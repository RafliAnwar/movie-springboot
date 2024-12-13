package dev.dzul.movie.movie;

import dev.dzul.movie.utils.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("")
    public ResponseEntity<ResponseFormatter<List<ResponseDTO>>> getAllMovies() {
        try {
            List<ResponseDTO> movies = movieService.getAllMovies();
            return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Movies fetched successfully", movies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while fetching movies", null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseFormatter<List<ResponseDTO>>> getMoviesByTitle(@RequestParam String query) {
        try {
            List<ResponseDTO> movies = movieService.findAllMoviesByTitle(query, query);
            return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Movies fetched successfully", movies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while searching for movies", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseFormatter<Movie>> getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieById(id);
            if (movie != null) {
                return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Movie fetched successfully", movie));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseFormatter<>(HttpStatus.NOT_FOUND.value(), "Movie not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while fetching the movie", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseFormatter<ResponseDTO>> createMovie(@RequestBody MovieDTO movieDTO) {
        try {
            ResponseDTO response = movieService.createMovie(movieDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseFormatter<>(HttpStatus.CREATED.value(), "Movie created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while creating the movie", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseFormatter<ResponseDTO>> updateMovie(
            @PathVariable Long id, @RequestBody MovieDTO movieDTO) {
        try {
            ResponseDTO response = movieService.updateMovie(id, movieDTO);
            if (response != null) {
                return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Movie updated successfully", response));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseFormatter<>(HttpStatus.NOT_FOUND.value(), "Movie not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while updating the movie", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseFormatter<Void>> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Movie deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Movie with id: "+id+ " is not found", null));
        }
    }
}
