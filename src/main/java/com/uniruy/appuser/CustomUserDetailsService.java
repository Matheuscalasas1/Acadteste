package com.uniruy.appuser;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CoordenadorRepository coordenadorRepository;

    public CustomUserDetailsService(CoordenadorRepository coordenadorRepository) {
        this.coordenadorRepository = coordenadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Object coordenador = coordenadorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        return User.builder()
                .username(((Coordenador) coordenador).getEmail())
                .password(((Coordenador) coordenador).getSenha()) // Senha já criptografada do banco
                .roles("COORDENADOR")
                .build();
    }
}