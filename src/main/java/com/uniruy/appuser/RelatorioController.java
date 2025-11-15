package com.uniruy.appuser;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RelatorioController {

    private final AlunoRepository alunoRepository;
    
    public RelatorioController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping("/relatorios")
    public String relatoriosPage(
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            Model model) {
        
        // Valores padrão
        if (mes == null) mes = LocalDate.now().getMonthValue();
        if (ano == null) ano = LocalDate.now().getYear();
        
        System.out.println("=== CARREGANDO RELATÓRIOS ===");
        System.out.println("Filtros - Mês: " + mes + ", Ano: " + ano);
        
        // Buscar todos os alunos
        List<Aluno> alunos = alunoRepository.findAll();
        System.out.println("Total de alunos encontrados: " + alunos.size());
        
        // Processar dados para relatórios
        Map<String, Long> alunosPorCurso = getAlunosPorCurso(alunos);
        Map<String, Long> alunosPorTurno = getAlunosPorTurno(alunos);
        Map<String, Long> alunosPorStatus = getAlunosPorStatus(alunos);
        
        // Debug dos dados
        System.out.println("Alunos por Curso: " + alunosPorCurso);
        System.out.println("Alunos por Turno: " + alunosPorTurno);
        System.out.println("Alunos por Status: " + alunosPorStatus);
        
        // Adicionar dados ao modelo
        model.addAttribute("alunosPorCurso", alunosPorCurso != null ? alunosPorCurso : new HashMap<>());
        model.addAttribute("alunosPorTurno", alunosPorTurno != null ? alunosPorTurno : new HashMap<>());
        model.addAttribute("alunosPorStatus", alunosPorStatus != null ? alunosPorStatus : new HashMap<>());
        model.addAttribute("totalAlunos", alunos.size());
        model.addAttribute("alunosAtivos", contarAlunosAtivos(alunos));
        model.addAttribute("cursosCount", (long) (alunosPorCurso != null ? alunosPorCurso.size() : 0));
        model.addAttribute("mes", mes);
        model.addAttribute("ano", ano);
        
        return "relatorios";
    }

    private Map<String, Long> getAlunosPorCurso(List<Aluno> alunos) {
        if (alunos == null || alunos.isEmpty()) {
            return new HashMap<>();
        }
        
        return alunos.stream()
            .filter(aluno -> aluno.getCurso() != null && !aluno.getCurso().trim().isEmpty())
            .collect(Collectors.groupingBy(
                Aluno::getCurso, 
                Collectors.counting()
            ));
    }

    private Map<String, Long> getAlunosPorTurno(List<Aluno> alunos) {
        if (alunos == null || alunos.isEmpty()) {
            return new HashMap<>();
        }
        
        return alunos.stream()
            .filter(aluno -> aluno.getTurno() != null && !aluno.getTurno().trim().isEmpty())
            .collect(Collectors.groupingBy(
                Aluno::getTurno, 
                Collectors.counting()
            ));
    }

    private Map<String, Long> getAlunosPorStatus(List<Aluno> alunos) {
        if (alunos == null || alunos.isEmpty()) {
            return new HashMap<>();
        }
        
        return alunos.stream()
            .filter(aluno -> aluno.getStatus() != null && !aluno.getStatus().trim().isEmpty())
            .collect(Collectors.groupingBy(
                Aluno::getStatus, 
                Collectors.counting()
            ));
    }
    
    private Long contarAlunosAtivos(List<Aluno> alunos) {
        if (alunos == null || alunos.isEmpty()) {
            return 0L;
        }
        
        return alunos.stream()
            .filter(aluno -> aluno.getStatus() != null && "Ativo".equalsIgnoreCase(aluno.getStatus()))
            .count();
    }
}