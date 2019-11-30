package io.yodo.whisper.commons.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@SuppressWarnings("WeakerAccess")
public class TokenAuthentication implements Authentication {

    private String token;

    private UserDetails userDetails;

    private TokenDetails tokenDetails;

    private boolean authenticated;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    public void setPrincipal(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated = b;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    public TokenDetails getTokenDetails() {
        return tokenDetails;
    }

    public String getToken() {
        return token;
    }

    public void setTokenDetails(TokenDetails tokenDetails) {
        this.tokenDetails = tokenDetails;
    }
}
