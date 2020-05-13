package wolox.training.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import wolox.training.services.OpenLibraryService;

public class SecurityConfigTest {

    @Bean
    public UserDetailsService userDetailsService() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
        UserDetails userDetails = (UserDetails) new User("user", "password",
            Arrays.asList(authority));
        return new InMemoryUserDetailsManager(Arrays.asList(userDetails));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenLibraryService openLibraryService() {
        return new OpenLibraryService();
    }

}
