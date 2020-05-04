package wolox.training.config.providers;

import java.util.ArrayList;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (this.userValid(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Boolean userValid(String username, String password) {
        UserDetails userDetails = this.getUserDetailsService().loadUserByUsername(username);
        return this.getPasswordEncoder().matches(password, userDetails.getPassword());
    }
}
