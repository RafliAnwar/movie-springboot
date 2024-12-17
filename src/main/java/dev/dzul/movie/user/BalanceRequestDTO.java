package dev.dzul.movie.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class BalanceRequestDTO implements Serializable {
    private static final Long serialVersionUID = 214782385698596L;

    private Integer amount;
}
