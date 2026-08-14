# Roteiro de Apresentacao - Sistema RSC

## 1. Contextualizacao

O Recanto do Sagrado Coracao precisa de uma aplicacao desktop para organizar cadastros, prontuarios medicos, consultas, prescricoes, vacinas, eventos sentinela e relatorios operacionais de uma casa de repouso.

## 2. Problema

- Dados clinicos e administrativos ficavam espalhados em registros manuais ou fluxos pouco integrados.
- Prontuarios, eventos e historicos precisam estar relacionados a uma idosa.
- A equipe precisa consultar, cadastrar, editar e remover registros com feedback claro.
- O sistema precisa persistir informacoes em banco relacional, sem depender de dados ficticios na interface.

## 3. Solucao Implementada

- Aplicacao desktop Java 8 com interface Swing.
- Arquitetura em camadas: Model, DAO, Controller e View.
- Persistencia JDBC/MySQL com configuracao centralizada.
- Interface com dashboard, navegacao lateral, tabelas, formularios, detalhes e busca local.
- Operacoes de listagem, salvamento e remocao executadas em background para manter a janela responsiva.
- Validacao de campos obrigatorios e IDs relacionados antes da gravacao.

## 4. Tecnologias

- Java 8
- Swing
- Maven
- JDBC
- MySQL
- JUnit Jupiter
- Git/GitHub

## 5. Estrutura

- `model`: entidades do dominio.
- `dao`: persistencia em MySQL e DAOs em memoria usados nos testes.
- `controller`: servicos de aplicacao usados pela interface.
- `view`: telas Swing, painel base reutilizavel e tema visual.
- `criar_banco_idosoapp.sql`: schema MySQL com InnoDB, `utf8mb4`, indices e chaves estrangeiras.

## 6. Destaques da Modernizacao

- A aplicacao continua desktop Java; nao foi transformada em web.
- O visual nao copia um portfolio: traduz organizacao editorial para apresentar um produto tecnico.
- O dashboard mostra contagens reais vindas do banco.
- As listagens possuem busca local, estados vazios e mensagens de erro/sucesso.
- Salvar, remover e atualizar listagens nao travam a interface.
- O feedback diferencia lista vazia de falha de conexao.

## 7. Limitacoes Atuais

- Relatorios ainda sao registros operacionais simples, nao dashboards analiticos avancados.
- Registros vinculados a prontuario usam o ID do prontuario na criacao; na edicao, os models atuais atualizam apenas os campos proprios.
- Sem MySQL configurado, a interface abre, mas as consultas exibem erro ou listas vazias.

## 8. Melhorias Futuras

- Autenticacao e niveis de permissao.
- Relatorios exportaveis.
- Migracoes versionadas de banco.
- Testes de integracao com MySQL em ambiente controlado.

## 9. Conclusao

O sistema entrega uma base desktop organizada, com persistencia real, testes, interface navegavel e estrutura preparada para evolucoes incrementais sem abandonar Java, Swing, Maven, JDBC e MVC.
