package dev.dzul.movie.controller;
import dev.dzul.movie.user.*;
import dev.dzul.movie.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;
    private ResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setUsername("testuser");
        userDTO.setPhone("1234567890");

        responseDTO = new ResponseDTO();
        responseDTO.setEmail("test@example.com");
        responseDTO.setUsername("testuser");
        responseDTO.setPhone("1234567890");
        responseDTO.setBalance(1000);
    }

    // Test: Register user - 201 Created
    @Test
    void testRegisterUser_201Created() {
        when(userService.registerUser(any(UserDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.registerUser(userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals("test@example.com", response.getBody().getData().getEmail());

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    // Test: Register user - 400 Bad Request
    @Test
    void testRegisterUser_400BadRequest() {
        when(userService.registerUser(any(UserDTO.class))).thenThrow(new IllegalArgumentException("Email already exists"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.registerUser(userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    // Test: Register user - 500 Internal Server Error
    @Test
    void testRegisterUser_500InternalServerError() {
        when(userService.registerUser(any(UserDTO.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.registerUser(userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while registering the user", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    // Test: Add balance - 200 OK
    @Test
    void testAddBalance_200Ok() {
        BalanceRequestDTO balanceRequest = new BalanceRequestDTO();
        balanceRequest.setAmount(500);

        when(userService.addBalance(1L, 500)).thenReturn(responseDTO);

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.addBalance(1L, balanceRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Balance added successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(1000, response.getBody().getData().getBalance());

        verify(userService, times(1)).addBalance(1L, 500);
    }

    // Test: Add balance - 500 Internal Server Error
    @Test
    void testAddBalance_500InternalServerError() {
        BalanceRequestDTO balanceRequest = new BalanceRequestDTO();
        balanceRequest.setAmount(500);

        when(userService.addBalance(1L, 500)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.addBalance(1L, balanceRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while adding balance: Database error", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService, times(1)).addBalance(1L, 500);
    }

    // Test: Add balance - 404 Not Found
    @Test
    void testAddBalance_404NotFound() {
        BalanceRequestDTO balanceRequest = new BalanceRequestDTO();
        balanceRequest.setAmount(500);

        when(userService.addBalance(1L, 500)).thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<ResponseFormatter<ResponseDTO>> response = userController.addBalance(1L, balanceRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while adding balance: User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(userService, times(1)).addBalance(1L, 500);
    }
}
