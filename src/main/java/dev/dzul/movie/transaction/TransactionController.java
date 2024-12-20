package dev.dzul.movie.transaction;

import dev.dzul.movie.utils.ResponseFormatter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseFormatter<ResponseDTO>> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        ResponseDTO responseDTO = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseFormatter<>(HttpStatus.CREATED.value(), "Transaction created successfully", responseDTO));
    }
}
