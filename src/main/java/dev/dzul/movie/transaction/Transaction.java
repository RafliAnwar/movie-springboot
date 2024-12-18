package dev.dzul.movie.transaction;


import dev.dzul.movie.movie.Movie;
import dev.dzul.movie.subscription.Subscription;
import dev.dzul.movie.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    //id, id_user, id_ subscription, status default paid, tgl_transaksi, end of subscription, id_movie
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_subscription")
    private Subscription subscription;

    private LocalDateTime transaction_date;

    private LocalDateTime end_of_subscription;

    private String transaction_code;

    private String status = "PAID";
}
