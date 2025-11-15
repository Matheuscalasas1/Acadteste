package com.uniruy.appuser;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Camada de Serviço responsável pela lógica de negócios
 * de Coordenador (validação, criptografia, e persistência).
 */
@Service
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;
    private final PasswordEncoder passwordEncoder;

    // Construtor com injeção automática do Repository e do PasswordEncoder
    public CoordenadorService(CoordenadorRepository coordenadorRepository, PasswordEncoder passwordEncoder) {
        this.coordenadorRepository = coordenadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um novo coordenador no banco de dados.
     * Realiza a validação de email e criptografa a senha.
     *
     * @param coordenador O objeto Coordenador a ser salvo.
     * @return O objeto Coordenador salvo.
     * @throws IllegalStateException se o email já estiver cadastrado.
     */
    public Coordenador salvarCoordenador(Coordenador coordenador) {
        // 1. VERIFICAÇÃO DE EMAIL DUPLICADO
        Optional<Object> coordenadorExistente = coordenadorRepository.findByEmail(coordenador.getEmail());
if (coordenadorExistente.isPresent()) {
    throw new IllegalStateException("O e-mail '" + coordenador.getEmail() + "' já está cadastrado.");
}

        // 2. CRIPTOGRAFIA DA SENHA
        String senhaCriptografada = passwordEncoder.encode(coordenador.getSenha());
        coordenador.setSenha(senhaCriptografada);

        // 3. DEFINIÇÃO DE DATA DE CRIAÇÃO
        coordenador.setDataCriacao(LocalDateTime.now());

        // 4. SALVAMENTO NO BANCO DE DADOS
        return coordenadorRepository.save(coordenador);
    }
}
