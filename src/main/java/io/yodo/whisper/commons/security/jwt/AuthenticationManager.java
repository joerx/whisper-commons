package io.yodo.whisper.commons.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final TokenDecoder tokenDecoder;

    public AuthenticationManager(TokenDecoder tokenDecoder) {
        this.tokenDecoder = tokenDecoder;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof TokenAuthentication)) {
            throw new IllegalArgumentException("Unsupported authentication class " + authentication.getClass());
        }

        TokenAuthentication jwtAuth = (TokenAuthentication) authentication;
        String jwtToken = (String) authentication.getCredentials();

        TokenDetails td = tokenDecoder.decode(jwtToken);
        User user = mapToUser(td);

        jwtAuth.setAuthenticated(true);
        jwtAuth.setPrincipal(user);
        jwtAuth.setTokenDetails(td);

        log.debug("Authentication successful, returning " + jwtAuth);

        return jwtAuth;
    }

    private User mapToUser(TokenDetails td) {
        Collection<GrantedAuthority> scopes = Arrays.stream(td.getScopes())
                .map(s -> new SimpleGrantedAuthority("SCOPE_"+s))
                .collect(Collectors.toUnmodifiableSet());
        return new User(td.getSubject(), "", scopes);
    }
}
