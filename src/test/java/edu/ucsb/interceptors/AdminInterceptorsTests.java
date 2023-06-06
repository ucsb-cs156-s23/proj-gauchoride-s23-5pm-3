package edu.ucsb.interceptors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import edu.ucsb.cs156.gauchoride.entities.User;
import edu.ucsb.cs156.gauchoride.interceptors.AdminInterceptors;
import edu.ucsb.cs156.gauchoride.repositories.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminInterceptorsTest {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    @Test
    public void adminInterceptorShouldBeApplied() throws Exception {
        // Create a mock user repository and a user object
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User();
        user.setAdmin(true);
        user.setDriver(false);

        // Create a mock OAuth2 authentication token with the user details
        Authentication authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getAuthorities()).Return(getAuthorities());
        when(authentication.getPrincipal()).thenReturn(getPrincipal());

        // Set the authentication object in the SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Create an instance of the interceptor with the mock user repository
        AdminInterceptors interceptor = new AdminInterceptors(userRepository);

        // Create a mock request
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/example");

        // Get the handler execution chain for the request
        HandlerExecutionChain chain = mapping.getHandler(request);

        assert chain != null;
        // Apply the interceptor on the handler execution chain
        boolean preHandleResult = interceptor.preHandle(request, null, chain.getHandler());

        // Assert that the interceptor is applied
        assertTrue(preHandleResult);
    }

    @Test
    public void adminInterceptorShouldNotBeAppliedToHealthURL() throws Exception {
        // Create a mock user repository and a user object
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User();
        user.setAdmin(true);
        user.setDriver(false);

        // Create a mock OAuth2 authentication token with the user details
        Authentication authentication = mock(OAuth2AuthenticationToken.class);
        when(authentication.getAuthorities()).thenReturn(getAuthorities());
        when(authentication.getPrincipal()).thenReturn(getPrincipal());

        // Set the authentication object in the SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Create an instance of the interceptor with the mock user repository
        AdminInterceptors interceptor = new AdminInterceptors(userRepository);

        // Create a mock request for the health URL
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/health");

        // Get the handler execution chain for the request
        HandlerExecutionChain chain = mapping.getHandler(request);

        assert chain != null;
        // Apply the interceptor on the handler execution chain
        boolean preHandleResult = interceptor.preHandle(request, null, chain.getHandler());

        // Assert that the interceptor is not applied
        assertFalse(preHandleResult);
    }

    private Set<String> getAuthorities() {
        return new HashSet<>(Arrays.asList("ROLE_USER"));
    }

    private OAuth2AuthenticationToken getPrincipal() {
        return mock(OAuth2AuthenticationToken.class);
    }
}