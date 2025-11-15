package com.uniruy.appuser;

import java.time.LocalDateTime;
import java.time.Year;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @Column(nullable = false)
    private String curso;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String turno;

    @Column(nullable = false)
    private String status;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Construtores
    public Aluno() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Aluno(String nomeAluno, String curso, String matricula, String turno, String status) {
        this();
        this.nomeAluno = nomeAluno;
        this.curso = curso;
        this.matricula = matricula;
        this.turno = turno;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeAluno() { return nomeAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public static String gerarMatriculaAutomatica(AlunoRepository alunoRepository) {
    try {
        String anoAtual = String.valueOf(Year.now().getValue());
        
        System.out.println("Gerando matrícula para ano: " + anoAtual);
        
        // Tentar primeiro com a query JPQL
        Long ultimaSequencia = null;
        try {
            ultimaSequencia = alunoRepository.findMaxSequenciaByAno(anoAtual);
            System.out.println("Última sequência (JPQL): " + ultimaSequencia);
        } catch (Exception e) {
            System.out.println("Erro na query JPQL: " + e.getMessage());
            // Tentar com a query nativa
            try {
                ultimaSequencia = alunoRepository.findMaxSequenciaByAnoNative(anoAtual);
                System.out.println("Última sequência (Native): " + ultimaSequencia);
            } catch (Exception e2) {
                System.out.println("Erro na query nativa: " + e2.getMessage());
            }
        }
        
        long novaSequencia = (ultimaSequencia == null) ? 1 : ultimaSequencia + 1;
        String matricula = anoAtual + String.format("%04d", novaSequencia);
        
        System.out.println("Nova matrícula gerada: " + matricula);
        return matricula;
        
    } catch (Exception e) {
        System.out.println("ERRO CRÍTICO ao gerar matrícula: " + e.getMessage());
        // Fallback seguro
        return String.valueOf(Year.now().getValue()) + "0001";
    }
}
        
    
    

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}