package dev.dzul.movie.service;

import dev.dzul.movie.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User Entity
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setUsername("testuser");
        user.setPhone("1234567890");
        user.setBalance(1000);

        // Mock UserDTO
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setUsername("testuser");
        userDTO.setPhone("1234567890");
    }

    // Test: Register User - 201 Created
    @Test
    void testRegisterUser_201Created() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseDTO response = userService.registerUser(userDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
        assertEquals(1000, response.getBalance());

        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test: Register User - 400 Bad Request (Email already exists)
    @Test
    void testRegisterUser_400BadRequest() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    // Test: Add Balance - 200 OK
    @Test
    void testAddBalance_200Ok() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseDTO response = userService.addBalance(1L, 500);

        assertNotNull(response);
        assertEquals(1500, response.getBalance()); // Initial balance 1000 + 500
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test: Add Balance - 400 Bad Request (Invalid amount)
    @Test
    void testAddBalance_400BadRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.addBalance(1L, -500));

        assertEquals("Amount to add must be positive", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    // Test: Add Balance - 404 Not Found (User not found)
    @Test
    void testAddBalance_404NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.addBalance(1L, 500));

        assertEquals("User with ID 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    // Test: Register User - 500 Internal Server Error (Unexpected error)
    @Test
    void testRegisterUser_500InternalServerError() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));

        assertEquals("Database error", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
}
