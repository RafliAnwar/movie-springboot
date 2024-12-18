package dev.dzul.movie.transaction;

import dev.dzul.movie.utils.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseFormatter<ResponseDTO>> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            ResponseDTO responseDTO = transactionService.createTransaction(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseFormatter<>(HttpStatus.CREATED.value(), "Transaction created successfully", responseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseFormatter<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while creating the transaction", null));
        }
    }
}
