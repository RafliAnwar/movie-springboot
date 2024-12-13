package dev.dzul.movie.movie;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseDTO {
    private Long id;
    private String genre_name;
    private String title;
    private String studio;
    private String description;
    private String review;
    private Integer rating;
    private String photo;
    private String videoUrl;
    private Date releaseDate;
}
