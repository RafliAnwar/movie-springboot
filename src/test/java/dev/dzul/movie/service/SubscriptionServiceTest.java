package dev.dzul.movie.service;

import dev.dzul.movie.subscription.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private Subscription subscription;
    private SubscriptionDTO subscriptionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setName("Premium");
        subscription.setPrice(100);
        subscription.setDuration(30);
        subscription.setIs_4k(true);

        subscriptionDTO = new SubscriptionDTO(1L, "Standard", 50, 15, false);
    }

    // Test: Get all subscriptions - 200 OK
    @Test
    void testGetAllSubscriptions_200Ok() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(subscription);

        when(subscriptionRepository.findAllByOrderByIdAsc()).thenReturn(subscriptions);

        List<ResponseDTO> response = subscriptionService.getAllSubscriptions();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Premium", response.get(0).getName());
        verify(subscriptionRepository, times(1)).findAllByOrderByIdAsc();
    }

    // Test: Get subscription by ID - 200 OK
    @Test
    void testGetSubscriptionById_200Ok() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription response = subscriptionService.getSubscriptionById(1L);

        assertNotNull(response);
        assertEquals("Premium", response.getName());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    // Test: Get subscription by ID - 404 Not Found
    @Test
    void testGetSubscriptionById_404NotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Subscription response = subscriptionService.getSubscriptionById(1L);

        assertNull(response);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    // Test: Create subscription - 201 Created
    @Test
    void testCreateSubscription_201Created() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        ResponseDTO response = subscriptionService.createSubscription(subscriptionDTO);

        assertNotNull(response);
        assertEquals("Premium", response.getName());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    // Test: Update subscription - 200 OK
    @Test
    void testUpdateSubscription_200Ok() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        ResponseDTO response = subscriptionService.updateSubscription(1L, subscriptionDTO);

        assertNotNull(response);
        assertEquals("Standard", response.getName());
        verify(subscriptionRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    // Test: Update subscription - 404 Not Found
    @Test
    void testUpdateSubscription_404NotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                subscriptionService.updateSubscription(1L, subscriptionDTO)
        );

        assertEquals("Subscription with id 1 not found.", exception.getMessage());
        verify(subscriptionRepository, times(1)).findById(1L);
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    // Test: Delete subscription - 200 OK
    @Test
    void testDeleteSubscription_200Ok() {
        when(subscriptionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(subscriptionRepository).deleteById(1L);

        assertDoesNotThrow(() -> subscriptionService.deleteSubscription(1L));

        verify(subscriptionRepository, times(1)).existsById(1L);
        verify(subscriptionRepository, times(1)).deleteById(1L);
    }

    // Test: Delete subscription - 500 Internal Server Error
    @Test
    void testDeleteSubscription_500InternalServerError() {
        when(subscriptionRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                subscriptionService.deleteSubscription(1L)
        );

        assertEquals("Subscription with id: 1is not found", exception.getMessage());
        verify(subscriptionRepository, times(1)).existsById(1L);
        verify(subscriptionRepository, never()).deleteById(1L);
    }
}

