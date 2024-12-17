package dev.dzul.movie.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        user.setBalance(0);

        User savedUser = userRepository.save(user);

        ResponseDTO responseDTO = new ResponseDTO();
        BeanUtils.copyProperties(savedUser, responseDTO);
        return responseDTO;
    }
}
