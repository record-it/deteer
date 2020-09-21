package pl.recordit.deteer.facade;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    default boolean isAuthenticated(){
        return !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }
    default boolean isAnonymous(){
        return getAuthentication() instanceof AnonymousAuthenticationToken;
    }
}
