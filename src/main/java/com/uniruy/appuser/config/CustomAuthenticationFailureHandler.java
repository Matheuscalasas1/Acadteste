package com.uniruy.appuser.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      AuthenticationException exception) throws IOException {
        
        HttpSession session = request.getSession();
        String errorMessage = "E-mail ou senha incorretos. Verifique suas credenciais.";
        session.setAttribute("errorLogin", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}