package dev.dzul.movie.transaction;

import dev.dzul.movie.subscription.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private Long id_user;
    private Long id_subscription;
    private LocalDateTime transaction_date;
    private LocalDateTime end_of_subscription;
    private String transaction_code;
    private String status;
}
