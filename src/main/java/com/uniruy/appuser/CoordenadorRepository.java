package com.uniruy.appuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Coordenador.
 * Fornece métodos CRUD e de busca personalizados.
 */
@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {

    /**
     * Busca um Coordenador pelo seu endereço de e-mail.
     * É usado para verificar se o e-mail já existe no cadastro.
     * * @param email o e-mail a ser pesquisado.
     * @return Um Optional contendo o Coordenador, se encontrado.
     */
    Optional<Coordenador> findByEmail(String email);
}
