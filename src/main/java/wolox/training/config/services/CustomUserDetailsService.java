package wolox.training.config.services;

import java.util.ArrayList;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Service("UserDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService() {
        super();
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findFirstByUsername(username)
                        .orElseThrow(UserNotFoundException::new);
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            true, true, true, true,
            new ArrayList<GrantedAuthority>()
        );
    }
}
