<h1> AcadSystem - Sistema de Gestão Acadêmica </h1>
📋 Sobre o Projeto
O AcadSystem é um sistema completo de gestão acadêmica desenvolvido em Spring Boot que permite o gerenciamento eficiente de alunos, coordenadores e relatórios institucionais. O sistema oferece uma interface moderna e intuitiva para cadastro, edição, visualização e geração de relatórios acadêmicos.

<h3>🚀 Tecnologias Utilizadas> </h3>
Backend
Java 17+

Spring Boot 3.x

Spring Security - Autenticação e autorização

Spring Data JPA - Persistência de dados

MySQL - Banco de dados relacional

Maven - Gerenciamento de dependências

Frontend
- HTML5 - Estrutura das páginas

- CSS3 - Estilização e design responsivo

- JavaScript - Interatividade e validações

- Thymeleaf - Template engine

- Chart.js - Gráficos e visualizações

- IonIcons - Biblioteca de ícones

- Font Awesome - Ícones adicionais

<h3>Funcionalidades Principais</h3>
👤 Autenticação e Segurança
- Login seguro com Spring Security

- Registro de coordenadores com validação de email

- Criptografia de senhas com BCrypt

- Controle de sessões e logout seguro

🎓 Gestão de Alunos
- Cadastro completo de alunos com dados pessoais e acadêmicos

- Matrícula automática com sistema sequencial por ano

- Edição e atualização de informações dos alunos

- Exclusão segura com confirmação

- Visualização detalhada de cada aluno

- Status acadêmico (Ativo, Inativo, Trancado, Formado)

📊 Relatórios e Analytics
- Dashboard interativo com gráficos

- Distribuição por curso (gráfico de pizza)

- Análise por turno (gráfico de barras)

- Status dos alunos (gráfico de doughnut)

- Evolução mensal de matrículas

- Filtros por mês e ano

- Estatísticas em tempo real

🎨 Interface do Usuário
- Design responsivo para todos os dispositivos

- Navegação intuitiva entre seções

- Validações em tempo real nos formulários

- Mensagens de feedback (sucesso/erro)

- Loading states durante operações


<h2>📖 Guia de Uso</h2>
- Primeiro Acesso
Registro de Coordenador:

- Acesse /registro

Preencha nome, email e senha

Confirme a senha

Sistema redireciona para login automaticamente

Login no Sistema:

- Acesse /login

Use email e senha cadastrados

Será redirecionado para a página inicial

- Gestão de Alunos
Cadastrar Novo Aluno
Na página inicial, clique em "Adicionar Aluno"

Preencha os campos obrigatórios:

Nome completo

Curso

Turno (Matutino/Vespertino/Noturno)

Status (Ativo/Inativo/Trancado/Formado)

A matrícula é gerada automaticamente

Clique em "Cadastrar Aluno"

- Editar Aluno
Na lista de alunos, clique em "Atualizar"

Modifique os campos necessários

A matrícula não pode ser alterada

Clique em "Atualizar Aluno"

- Visualizar Detalhes
Clique em "Detalhes" na lista de alunos

Veja informações completas incluindo:

Dados pessoais

Informações acadêmicas

Datas de criação e atualização

- Excluir Aluno
Clique em "Deletar" na lista

Confirme a exclusão no popup

O aluno será removido permanentemente

- Relatórios
Acesse a seção de Relatórios através do menu

Use os filtros para personalizar a visualização:

Selecione mês específico

Escolha o ano desejado

Visualize os gráficos:

Distribuição por curso

Alunos por turno

Status dos alunos

Evolução mensal

Analise a tabela resumo com porcentagens

<h2>🔧 Funcionalidades Técnicas</h2>
Sistema de Matrícula Automática
Geração sequencial por ano (ex: 20250001, 20250002)

Prevenção de duplicatas

Formato: ANO + 4 dígitos sequenciais

Validações de Segurança
Senhas criptografadas com BCrypt

Prevenção de SQL Injection

Validação de sessões

Proteção contra CSRF

Design Responsivo
Adaptação para mobile, tablet e desktop

Grid system flexível

Media queries otimizadas

Performance
Consultas otimizadas ao banco

Cache de gráficos

Carregamento assíncrono


<h1>Arquitetura e Entidades do AcadSystem</h1>
<h2>📊 Modelo de Dados</h2>
<h1>Diagrama de Entidades</h1>

<h3>Coordenador</h3>

- id (pk)
- email (uk)
- senha
- nome
- departamento
- data_criacao

<h3>Aluno</h3>

- id (pk)
- nome_aluno
- curso
- matricula (uk)
- turno
- status
- data_criacao
- data_atualizacao
