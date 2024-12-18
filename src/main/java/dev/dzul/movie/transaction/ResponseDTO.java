package dev.dzul.movie.transaction;

import dev.dzul.movie.subscription.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private Long id;
    private String username;
    private Subscription subscription;
    private LocalDateTime transaction_date;
    private LocalDateTime end_of_subscription;
    private String transaction_code;
    private String status;

    public ResponseDTO() {}
}
