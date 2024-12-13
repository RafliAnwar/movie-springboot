package dev.dzul.movie.genre;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

}
