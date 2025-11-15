package com.uniruy.appuser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // MÉTODOS EXISTENTES - MANTENHA
    @Query("SELECT MAX(CAST(SUBSTRING(a.matricula, 5) AS long)) FROM Aluno a WHERE SUBSTRING(a.matricula, 1, 4) = :ano")
    Long findMaxSequenciaByAno(@Param("ano") String ano);
    
    @Query(value = "SELECT MAX(CAST(SUBSTRING(matricula, 5) AS UNSIGNED)) FROM alunos WHERE SUBSTRING(matricula, 1, 4) = :ano", nativeQuery = true)
    Long findMaxSequenciaByAnoNative(@Param("ano") String ano);
    
    // REMOVA O MÉTODO PROBLEMÁTICO:
    // Long countByMesMatricula(int mes, int ano); // ← COMENTE OU DELETE ESTA LINHA
    
    // MÉTODOS ÚTEIS PARA RELATÓRIOS (OPCIONAIS):
    List<Aluno> findByCurso(String curso);
    List<Aluno> findByTurno(String turno);
    List<Aluno> findByStatus(String status);
    
    // Query personalizada para contar por mês/ano (SE PRECISAR):
    @Query("SELECT COUNT(a) FROM Aluno a WHERE FUNCTION('MONTH', a.dataCriacao) = :mes AND FUNCTION('YEAR', a.dataCriacao) = :ano")
    Long countByMesEAno(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT MONTH(a.dataCriacao), COUNT(a) FROM Aluno a WHERE YEAR(a.dataCriacao) = :ano GROUP BY MONTH(a.dataCriacao)")
List<Object[]> countAlunosPorMes(@Param("ano") Integer ano);

    
}

