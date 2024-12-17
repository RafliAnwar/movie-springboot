package dev.dzul.movie.user;


import dev.dzul.movie.utils.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseFormatter<ResponseDTO>> registerUser(@RequestBody UserDTO userDTO) {
        try {
            ResponseDTO createdUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseFormatter<>(HttpStatus.CREATED.value(), "User registered successfully", createdUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseFormatter<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while registering the user", null));
        }
    }


    @PutMapping("/{id}/balance")
    public ResponseEntity<ResponseFormatter<ResponseDTO>> addBalance(
            @PathVariable Long id, @RequestBody BalanceRequestDTO balanceRequest) {
        try {
            ResponseDTO responseDTO = userService.addBalance(id, balanceRequest.getAmount());
            return ResponseEntity.ok(new ResponseFormatter<>(HttpStatus.OK.value(), "Balance added successfully", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseFormatter<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while adding balance: " + e.getMessage(), null));
        }
    }

}
