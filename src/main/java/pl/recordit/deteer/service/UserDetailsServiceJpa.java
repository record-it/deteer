package pl.recordit.deteer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceJpa implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceJpa(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Użytkownik %s nie został znaleziony ", username)));
    }
}
