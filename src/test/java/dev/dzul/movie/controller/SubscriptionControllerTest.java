package dev.dzul.movie.controller;

import dev.dzul.movie.subscription.*;
import dev.dzul.movie.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionControllerTest {

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Mock
    private SubscriptionService subscriptionService;

    private Subscription subscription;
    private ResponseDTO responseDTO;
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

        responseDTO = new ResponseDTO(1L, "Premium", 100, 30, true);

        subscriptionDTO = new SubscriptionDTO(1L, "Standard", 50, 15, false);
    }

    // Test: Get all subscriptions - 200 OK
    @Test
    void testGetAllSubscriptions_200Ok() {
        List<ResponseDTO> subs = new ArrayList<>();
        subs.add(responseDTO);

        when(subscriptionService.getAllSubscriptions()).thenReturn(subs);

        ResponseEntity<ResponseFormatter<List<ResponseDTO>>> response = subscriptionController.getAllSubscriptions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subscriptions fetched successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        verify(subscriptionService, times(1)).getAllSubscriptions();
    }

    // Test: Get subscription by ID - 200 OK
    @Test
    void testGetSubscriptionById_200Ok() {
        when(subscriptionService.getSubscriptionById(1L)).thenReturn(subscription);
        when(subscriptionService.mapEntityToDto(subscription)).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = subscriptionController.getSubscriptionById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subscription fetched successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        verify(subscriptionService, times(1)).getSubscriptionById(1L);
    }

    // Test: Get subscription by ID - 404 Not Found
    @Test
    void testGetSubscriptionById_404NotFound() {
        when(subscriptionService.getSubscriptionById(1L)).thenReturn(null);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = subscriptionController.getSubscriptionById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Subscription not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(subscriptionService, times(1)).getSubscriptionById(1L);
    }

    // Test: Create subscription - 201 Created
    @Test
    void testCreateSubscription_201Created() {
        when(subscriptionService.createSubscription(subscriptionDTO)).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = subscriptionController.createSubscription(subscriptionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Subscription created successfully", response.getBody().getMessage());
        assertEquals(201, response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        verify(subscriptionService, times(1)).createSubscription(subscriptionDTO);
    }

    // Test: Create subscription - 500 Internal Server Error
    @Test
    void testCreateSubscription_500InternalServerError() {
        when(subscriptionService.createSubscription(subscriptionDTO))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = subscriptionController.createSubscription(subscriptionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while creating the subscription Unexpected error", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        assertNull(response.getBody().getData());
        verify(subscriptionService, times(1)).createSubscription(subscriptionDTO);
    }

    // Test: Delete subscription - 200 OK
    @Test
    void testDeleteSubscription_200Ok() {
        doNothing().when(subscriptionService).deleteSubscription(1L);

        ResponseEntity<ResponseFormatter<Void>> response = subscriptionController.deleteSubscription(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subscription deleted successfully", response.getBody().getMessage());
        assertEquals(200, response.getBody().getStatus());
        verify(subscriptionService, times(1)).deleteSubscription(1L);
    }

    // Test: Delete subscription - 500 Internal Server Error
    @Test
    void testDeleteSubscription_500InternalServerError() {
        doThrow(new RuntimeException("Unexpected error")).when(subscriptionService).deleteSubscription(1L);

        ResponseEntity<ResponseFormatter<Void>> response = subscriptionController.deleteSubscription(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Subscription with id: 1 is not found", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        verify(subscriptionService, times(1)).deleteSubscription(1L);
    }
}
