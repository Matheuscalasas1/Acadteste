package com.uniruy.appuser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    private final CoordenadorService coordenadorService;
    private final AlunoRepository alunoRepository;

    public PageController(CoordenadorService coordenadorService, AlunoRepository alunoRepository) {
        this.coordenadorService = coordenadorService;
        this.alunoRepository = alunoRepository;
    }

    // MÉTODO LOGIN - CORRIGIDO
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           HttpSession session,
                           Model model) {
        
        // Limpa mensagens anteriores da sessão
        if (error == null) {
            session.removeAttribute("errorLogin");
        }
        
        // Adiciona mensagem de sucesso do registro se existir
        if (session.getAttribute("sucessoRegistro") != null) {
            model.addAttribute("sucessoRegistro", session.getAttribute("sucessoRegistro"));
            session.removeAttribute("sucessoRegistro");
        }
        
        // Adiciona mensagem de erro do login apenas se error=true
        if ("true".equals(error) && session.getAttribute("errorLogin") != null) {
            model.addAttribute("erroLogin", session.getAttribute("errorLogin"));
            session.removeAttribute("errorLogin"); // Limpa após usar
        }
        
        // Mensagem de logout
        if ("true".equals(logout)) {
            model.addAttribute("mensagemLogout", "Logout realizado com sucesso!");
        }
        
        return "login"; 
    }

    // MÉTODO REGISTRO - DEVE FUNCIONAR AGORA
    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("coordenador", new Coordenador());
        return "registro";
    }

   @GetMapping("/home")
    @SuppressWarnings("CallToPrintStackTrace")
public String homePage(@RequestParam(value = "sucesso", required = false) String sucessoParam,
                      @RequestParam(value = "erro", required = false) String erroParam,
                      HttpSession session,
                      Model model) {
    try {
        System.out.println("=== CARREGANDO PÁGINA HOME ===");
        
        // Buscar todos os alunos do banco de dados
        List<Aluno> alunos = alunoRepository.findAll();
        System.out.println("Total de alunos no banco: " + alunos.size());
        
        // Debug detalhado
        for (Aluno aluno : alunos) {
            System.out.println("Aluno encontrado: " + aluno.getNomeAluno() + " - " + aluno.getMatricula());
        }
        
        // Adicionar a lista de alunos ao modelo
        model.addAttribute("alunos", alunos);
        
        // Adicionar objeto aluno vazio para o formulário
        Aluno aluno = new Aluno();
        String matriculaGerada = Aluno.gerarMatriculaAutomatica(alunoRepository);
        aluno.setMatricula(matriculaGerada);
        model.addAttribute("aluno", aluno);
        System.out.println("Matrícula gerada para formulário: " + matriculaGerada);
        
        // Verificar mensagens da sessão
        if (session.getAttribute("sucesso") != null) {
            model.addAttribute("sucesso", session.getAttribute("sucesso"));
            session.removeAttribute("sucesso");
            System.out.println("Mensagem de sucesso adicionada: " + model.getAttribute("sucesso"));
        }
        
        if (session.getAttribute("erro") != null) {
            model.addAttribute("erro", session.getAttribute("erro"));
            session.removeAttribute("erro");
            System.out.println("Mensagem de erro adicionada: " + model.getAttribute("erro"));
        }
        
    } catch (Exception e) {
        System.out.println("ERRO CRÍTICO ao carregar home: " + e.getMessage());
        e.printStackTrace();
        
        // Em caso de erro, criar lista vazia
        model.addAttribute("alunos", new ArrayList<Aluno>());
        
        // Criar aluno com matrícula padrão
        Aluno aluno = new Aluno();
        aluno.setMatricula("20250001");
        model.addAttribute("aluno", aluno);
    }
    
    return "home";

}
    // MÉTODO POST PARA SALVAR ALUNO - NOVO MÉTODO ADICIONADO
    // MÉTODO POST PARA SALVAR ALUNO - CORRIGIDO
@PostMapping("/alunos/salvar")
    @SuppressWarnings("CallToPrintStackTrace")
public String salvarAluno(@ModelAttribute("aluno") Aluno aluno, 
                         HttpSession session) {
    try {
        System.out.println("=== TENTANDO SALVAR ALUNO (SIMPLIFICADO) ===");
        
        // Validações básicas
        if (aluno.getNomeAluno() == null || aluno.getNomeAluno().trim().isEmpty()) {
            session.setAttribute("erro", "Nome do aluno é obrigatório");
            return "redirect:/home";
        }
        
        // Gerar matrícula SIMPLES temporariamente
        if (aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            // Método simples temporário
            String anoAtual = String.valueOf(java.time.Year.now().getValue());
            List<Aluno> todosAlunos = alunoRepository.findAll();
            long sequencia = todosAlunos.size() + 1;
            aluno.setMatricula(anoAtual + String.format("%04d", sequencia));
            System.out.println("Matrícula simples gerada: " + aluno.getMatricula());
        }
        
        // Salvar o aluno
        Aluno alunoSalvo = alunoRepository.save(aluno);
        System.out.println("ALUNO SALVO COM SUCESSO! ID: " + alunoSalvo.getId());
        
        session.setAttribute("sucesso", "Aluno cadastrado com sucesso!");
        return "redirect:/home";
        
    } catch (Exception e) {
        System.out.println("ERRO GRAVE ao salvar aluno: " + e.getMessage());
        e.printStackTrace();
        session.setAttribute("erro", "Erro: " + e.getMessage());
        return "redirect:/home";
    }
}
@GetMapping("/alunos/delete/{id}")
public String deletarAluno(@PathVariable Long id, HttpSession session) {
    try {
        System.out.println("Tentando deletar aluno com ID: " + id);
        
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            session.setAttribute("sucesso", "Aluno deletado com sucesso!");
            System.out.println("Aluno deletado com ID: " + id);
        } else {
            session.setAttribute("erro", "Aluno não encontrado!");
        }
        
    } catch (Exception e) {
        System.out.println("Erro ao deletar aluno: " + e.getMessage());
        session.setAttribute("erro", "Erro ao deletar aluno: " + e.getMessage());
    }
    
    return "redirect:/home";
}

// MÉTODO PARA EDITAR ALUNO - FORMULÁRIO
@GetMapping("/alunos/editar/{id}")
public String editarAlunoForm(@PathVariable Long id, Model model, HttpSession session) {
    try {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        
        if (alunoOptional.isPresent()) {
            model.addAttribute("aluno", alunoOptional.get());
            System.out.println("Carregando aluno para edição: " + alunoOptional.get().getNomeAluno());
            return "editar-aluno"; // Página específica para edição
        } else {
            session.setAttribute("erro", "Aluno não encontrado!");
            return "redirect:/home";
        }
        
    } catch (Exception e) {
        System.out.println("Erro ao carregar aluno para edição: " + e.getMessage());
        session.setAttribute("erro", "Erro ao carregar aluno para edição");
        return "redirect:/home";
    }
}

// MÉTODO PARA ATUALIZAR ALUNO
@PostMapping("/alunos/editar/{id}")
public String atualizarAluno(@PathVariable Long id, 
                            @ModelAttribute("aluno") Aluno alunoAtualizado,
                            HttpSession session) {
    try {
        System.out.println("Atualizando aluno ID: " + id);
        
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);
        
        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();
            
            // Atualizar apenas os campos permitidos
            aluno.setNomeAluno(alunoAtualizado.getNomeAluno());
            aluno.setCurso(alunoAtualizado.getCurso());
            aluno.setTurno(alunoAtualizado.getTurno());
            aluno.setStatus(alunoAtualizado.getStatus());
            // Não atualizamos a matrícula (ela é fixa)
            
            alunoRepository.save(aluno);
            session.setAttribute("sucesso", "Aluno atualizado com sucesso!");
            System.out.println("Aluno atualizado: " + aluno.getNomeAluno());
        } else {
            session.setAttribute("erro", "Aluno não encontrado!");
        }
        
        return "redirect:/home";
        
    } catch (Exception e) {
        System.out.println("Erro ao atualizar aluno: " + e.getMessage());
        session.setAttribute("erro", "Erro ao atualizar aluno: " + e.getMessage());
        return "redirect:/home";
    }
}

// MÉTODO PARA DETALHES DO ALUNO
@GetMapping("/alunos/detalhes/{id}")
public String detalhesAluno(@PathVariable Long id, Model model, HttpSession session) {
    try {
        Optional<Aluno> alunoOptional = alunoRepository.findById(id);
        
        if (alunoOptional.isPresent()) {
            model.addAttribute("aluno", alunoOptional.get());
            System.out.println("Carregando detalhes do aluno: " + alunoOptional.get().getNomeAluno());
            return "detalhes-aluno"; // Página específica para detalhes
        } else {
            session.setAttribute("erro", "Aluno não encontrado!");
            return "redirect:/home";
        }
        
    } catch (Exception e) {
        System.out.println("Erro ao carregar detalhes do aluno: " + e.getMessage());
        session.setAttribute("erro", "Erro ao carregar detalhes do aluno");
        return "redirect:/home";
    }
}

    // MÉTODO POST REGISTRO - CORRIGIDO
    @PostMapping("/registro")
    public String registrarCoordenador(
        @ModelAttribute("coordenador") Coordenador coordenador,
        @RequestParam("confirmacaoSenha") String confirmacaoSenha,
        HttpSession session,
        Model model) {

        if (!coordenador.getSenha().equals(confirmacaoSenha)) {
            model.addAttribute("erroRegistro", "As senhas digitadas não conferem.");
            return "registro";
        }

        try {
            coordenadorService.salvarCoordenador(coordenador);
            
            // Armazena a mensagem de sucesso na sessão
            session.setAttribute("sucessoRegistro", "Cadastro realizado com sucesso! Faça login para continuar.");
            
            return "redirect:/login"; 

        } catch (IllegalStateException e) {
            model.addAttribute("erroRegistro", e.getMessage());
            return "registro";
        } catch (Exception e) {
            model.addAttribute("erroRegistro", "Erro inesperado ao tentar registrar. Tente novamente.");
            return "registro";
        }

        
    }

    @GetMapping("/logout")
public String logout(HttpSession session) {
    try {
        // Remove atributos específicos da sessão se necessário
        session.removeAttribute("usuarioLogado");
        session.removeAttribute("coordenadorId");
        
        // Invalida completamente a sessão
        session.invalidate();
        
        System.out.println("Usuário deslogado com sucesso - sessão invalidada");
    } catch (Exception e) {
        System.out.println("Erro ao fazer logout: " + e.getMessage());
    }
    
    // Redireciona para a página de login com mensagem de logout
    return "redirect:/login?logout=true";
}

    @GetMapping("/teste-alunos")
@ResponseBody
public String testeAlunos() {
    try {
        List<Aluno> alunos = alunoRepository.findAll();
        StringBuilder result = new StringBuilder();
        result.append("Total de alunos: ").append(alunos.size()).append("<br>");
        
        for (Aluno aluno : alunos) {
            result.append("Aluno: ID=").append(aluno.getId())
                  .append(", Nome=").append(aluno.getNomeAluno())
                  .append(", Matrícula=").append(aluno.getMatricula())
                  .append(", Curso=").append(aluno.getCurso())
                  .append("<br>");
        }
        
        return result.toString();
    } catch (Exception e) {
        return "Erro: " + e.getMessage();
    }
}
}