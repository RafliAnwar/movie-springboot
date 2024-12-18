package dev.dzul.movie.service;

import dev.dzul.movie.subscription.Subscription;
import dev.dzul.movie.subscription.SubscriptionRepository;
import dev.dzul.movie.transaction.*;
import dev.dzul.movie.user.User;
import dev.dzul.movie.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private User user;
    private Subscription subscription;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setBalance(1000);

        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setName("Premium");
        subscription.setPrice(500);
        subscription.setDuration(30);

        transactionDTO = new TransactionDTO(
                1L,
                1L,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void testCreateTransaction_201Created() {
        // Mock data
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute service
        ResponseDTO responseDTO = transactionService.createTransaction(transactionDTO);

        // Assertions
        assertNotNull(responseDTO, "ResponseDTO should not be null");
        assertEquals("john_doe", responseDTO.getUsername());
        assertNotNull(responseDTO.getSubscription(), "Subscription in ResponseDTO should not be null");
        assertEquals("Premium", responseDTO.getSubscription().getName());

        // Verify interactions
        verify(userRepository, times(1)).save(user);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    void testCreateTransaction_404UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_404SubscriptionNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        assertEquals("Subscription not found with ID: 1", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_500InsufficientBalance() {
        user.setBalance(100); // Insufficient balance
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        assertEquals("Insufficient balance for the transaction.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
