package engine.security;

import engine.persistance.UserRepository;
import engine.service.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameLogin) throws UsernameNotFoundException {
        Optional<UserSecurity> userOptional = userRepository.findByUsername(usernameLogin);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User doesn't exist!");
        }
        UserSecurity userSecurity = userOptional.get();
        String username = userSecurity.getUsername();
        String password = userSecurity.getPassword();
        return new UserDetailsImpl(username, password);
    }
}
