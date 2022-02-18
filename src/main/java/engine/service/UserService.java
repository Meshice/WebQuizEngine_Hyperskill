package engine.service;

import engine.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(UserSecurity userSecurity) {
        if (checkUserEmailExist(userSecurity.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        userSecurity.setPassword(passwordEncoder.encode(userSecurity.getPassword()));
        userRepository.save(userSecurity);
    }

    private boolean checkUserEmailExist(String email) {
        return userRepository.findByUsername(email).isPresent();
    }
}
