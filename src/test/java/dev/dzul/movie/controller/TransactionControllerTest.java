package dev.dzul.movie.controller;
import dev.dzul.movie.transaction.ResponseDTO;
import dev.dzul.movie.transaction.TransactionController;
import dev.dzul.movie.transaction.TransactionDTO;
import dev.dzul.movie.transaction.TransactionService;
import dev.dzul.movie.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private TransactionDTO transactionDTO;
    private ResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocked TransactionDTO
        transactionDTO = new TransactionDTO(1L, 1L, null, null, null, null);

        // Mocked ResponseDTO
        responseDTO = new ResponseDTO(1L, "john_doe", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), "TXN123", "PAID");
    }

    @Test
    void testCreateTransaction_201Created() {
        when(transactionService.createTransaction(transactionDTO)).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = transactionController.createTransaction(transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Transaction created successfully", response.getBody().getMessage());
        assertEquals(201, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }

    @Test
    void testCreateTransaction_400BadRequest() {
        when(transactionService.createTransaction(transactionDTO))
                .thenThrow(new IllegalArgumentException("Invalid user ID"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = transactionController.createTransaction(transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid user ID", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }


    @Test
    void testCreateTransaction_404NotFound() {
        when(transactionService.createTransaction(transactionDTO))
                .thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = transactionController.createTransaction(transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Adjust to 404 if your implementation supports it.
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }

    @Test
    void testCreateTransaction_500InternalServerError() {
        when(transactionService.createTransaction(transactionDTO))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = transactionController.createTransaction(transactionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while creating the transaction", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(transactionService, times(1)).createTransaction(transactionDTO);
    }
}

