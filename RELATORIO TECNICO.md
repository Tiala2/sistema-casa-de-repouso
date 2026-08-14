# Relatorio Tecnico - Sistema de Gestao de Casa de Repouso

## 1. Introducao

O sistema e uma aplicacao desktop Java para apoiar a gestao de uma casa de repouso. Ele centraliza cadastros de idosas, prontuarios medicos, consultas, prescricoes, vacinas, eventos sentinela, profissionais de saude e relatorios operacionais.

A modernizacao manteve a proposta original do projeto: Java 8, Maven, MVC, JDBC e MySQL. A interface foi implementada em Swing, sem migracao para web, Spring Boot, JPA ou JavaFX.

## 2. Objetivos

- Organizar informacoes assistenciais e administrativas em uma interface unica.
- Persistir dados em MySQL usando JDBC.
- Exibir listagens, detalhes, formularios e feedback de operacoes.
- Manter o sistema responsivo durante consultas e gravacoes no banco.
- Preservar uma estrutura simples, compativel com o escopo academico e facil de evoluir.

## 3. Arquitetura

O projeto segue uma separacao em camadas:

- `model`: entidades do dominio, como `Idosa`, `ProntuarioMedico`, `Consulta`, `Prescricao`, `Vacina`, `EventoSentinela`, `ProfissionalSaude` e `Relatorio`.
- `dao`: acesso a dados com JDBC/MySQL e DAOs em memoria para testes.
- `controller`: servicos de aplicacao chamados pela interface.
- `view`: interface Swing, tema visual, dashboard e paineis dos modulos.
- `resources`: configuracao de banco em `application.properties`.

## 4. Banco de Dados

O schema esta em `criar_banco_idosoapp.sql` e usa:

- MySQL com InnoDB.
- `utf8mb4` para melhor compatibilidade de caracteres.
- Chaves estrangeiras nomeadas.
- Indices em campos de vinculo e busca.
- Regras explicitas de `ON UPDATE` e `ON DELETE`.

A conexao JDBC e centralizada em `DatabaseConnection`, que le `application.properties` ou variaveis de ambiente:

```properties
db.url=jdbc:mysql://localhost:3306/idosoapp
db.user=root
db.password=
```

Variaveis aceitas:

```text
DB_URL
DB_USER
DB_PASSWORD
```

## 5. Interface

A interface Swing foi organizada como um produto desktop tecnico:

- Dashboard com contagens reais do banco.
- Navegacao lateral por modulo.
- Tabelas com busca local e feedback de quantidade visivel.
- Area de detalhes do registro selecionado.
- Formularios para criar e editar registros.
- Mensagens claras para sucesso, erro, vazio e falha de conexao.
- Carregamento, salvamento e remocao em background com `SwingWorker`.

## 6. Regras e Validacoes

- Campos obrigatorios sao validados antes de gravar.
- IDs relacionados sao verificados antes da persistencia.
- Insercoes MySQL capturam o ID gerado e atualizam o objeto salvo.
- Remocoes informam quando podem ter falhado por vinculos protegidos.
- Listagens MySQL retornam erro para a interface quando a consulta falha, evitando confundir falha de conexao com lista vazia.

## 7. Testes

O projeto usa JUnit Jupiter com Maven Surefire. A suite cobre:

- Comportamento dos modelos.
- DAOs em memoria.
- Geracao de IDs.
- Busca, atualizacao e remocao em memoria.

Comandos de validacao:

```bash
cd Idosoapp
..\maven\mvn\bin\mvn.cmd -q test
..\maven\mvn\bin\mvn.cmd -q package
```

## 8. Limitacoes

- Relatorios ainda sao registros operacionais simples.
- Alguns models vinculados a prontuario nao carregam `prontuario_id` na listagem; por isso, na edicao sao atualizados apenas os campos proprios.
- Nao ha autenticacao de usuarios.
- Nao ha migracoes versionadas de banco.

## 9. Melhorias Futuras

- Autenticacao e perfis de acesso.
- Exportacao de relatorios.
- Testes de integracao com MySQL.
- Versionamento de migrations SQL.
- Relatorios analiticos mais ricos.

## 10. Conclusao

O sistema evoluiu para uma aplicacao desktop funcional, com persistencia real, interface organizada, feedback claro e base tecnica consistente. A modernizacao respeita o escopo Java desktop e melhora a apresentacao sem copiar visual externo nem transformar o projeto em web.
