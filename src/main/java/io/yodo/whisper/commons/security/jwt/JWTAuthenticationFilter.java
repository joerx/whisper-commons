package io.yodo.whisper.commons.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.commons.web.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public JWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        log.debug("Attempting authentication");

        String tokenHeader = httpServletRequest.getHeader("Authorization");

        if (tokenHeader == null) {
            throw new InvalidAuthenticationException("No token found in request");
        }
        if (!tokenHeader.matches("^Bearer\\s[.\\-\\w]+")) {
            throw new InvalidAuthenticationException("Invalid format for auth header");
        }

        String token = tokenHeader.replaceFirst("Bearer\\s+", "").trim();
        log.debug("Found a token, will try to authenticate");

        JWTTokenAuthentication auth = new JWTTokenAuthentication(token);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("Authentication was successful, setting security context auth to " + authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.debug("Authentication failed: " + failed.getMessage());
        SecurityContextHolder.clearContext();

        ErrorResponse res = new ErrorResponse(failed);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(res);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }
}
