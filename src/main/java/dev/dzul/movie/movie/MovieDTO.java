package dev.dzul.movie.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class MovieDTO {
    private Long id_genre;
    private String title;
    private String studio;
    private String description;
    private String review;
    private Integer rating;
    private String photo;
    private String videoUrl;
    private Date releaseDate;
    private Integer price;
}
