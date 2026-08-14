# Sistema de Gestao de Casa de Repouso

Aplicacao desktop em Java para apoiar a gestao de uma casa de repouso. O sistema organiza cadastros, prontuarios, consultas, prescricoes, vacinas, eventos sentinela e relatorios operacionais em uma interface Swing com navegacao clara e blocos de informacao.

## Arquitetura

- `Idosoapp/src/main/java/projeto/edu/unichristus/java/model`: entidades do dominio.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/dao`: persistencia JDBC/MySQL.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/controller`: servicos de aplicacao usados pela interface.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/view`: interface desktop Swing.
- `Idosoapp/src/main/resources/application.properties`: configuracao local de banco, sem credenciais pessoais.

O projeto permanece em Java 8, Maven, JDBC e MVC. A modernizacao visual nao muda o dominio nem transforma a aplicacao em web.
As dependencias foram mantidas enxutas: MySQL Connector/J para persistencia e JUnit Jupiter para testes.

## Modulos

- Cadastro de idosas
- Agenda de consultas
- Prontuarios medicos
- Prescricoes e medicacoes
- Profissionais de saude
- Vacinas
- Eventos sentinela
- Relatorios operacionais

## Modernizacao Visual

A interface foi criada em Swing porque nao havia camada visual, classes `main`, Swing, JavaFX ou FXML no branch analisado. A nova apresentacao traduz a referencia editorial para um produto desktop tecnico:

- dashboard com resumo vindo das listagens reais, atualizacao em background, botao manual e recarga automatica ao voltar para a tela inicial;
- navegacao lateral com secao ativa;
- cabecalhos de modulo com contexto, proposito e acao principal;
- area principal para tabela e area secundaria para detalhes/formulario;
- busca local nas listagens carregadas;
- listagens ordenadas de forma previsivel por nome, data ou identificador conforme o modulo;
- listagens carregadas em background para reduzir travamentos da interface quando o banco demora a responder;
- acoes de novo, salvar, remover, filtrar e selecionar ficam bloqueadas durante carregamentos para evitar estado visual inconsistente;
- estados vazios, mensagens de sucesso, erro e confirmacao de remocao;
- formularios com validacao basica antes de acionar controllers/DAOs;
- criacao, edicao e remocao integradas aos metodos reais de persistencia.
- feedback de erro quando a consulta ao banco falha, evitando confundir falha de conexao com lista vazia.

Nao foram adicionados graficos, metricas hardcoded ou dados ficticios.

## Banco de Dados

Crie o banco MySQL executando:

```bash
mysql -u root -p < criar_banco_idosoapp.sql
```

Configure `Idosoapp/src/main/resources/application.properties` ou use variaveis de ambiente:

```properties
db.url=jdbc:mysql://localhost:3306/idosoapp
db.user=root
db.password=
```

Variaveis aceitas:

```bash
DB_URL
DB_USER
DB_PASSWORD
```

As conexoes JDBC foram centralizadas em `DatabaseConnection`, reduzindo repeticao nos DAOs MySQL e permitindo configurar o banco por arquivo ou variaveis de ambiente.

## Como Executar

No Windows, usando o Maven incluido no repositorio:

```bash
cd Idosoapp
..\maven\mvn\bin\mvn.cmd test
..\maven\mvn\bin\mvn.cmd package
..\maven\mvn\bin\mvn.cmd exec:java
```

Se Maven estiver no PATH, tambem funciona:

```bash
cd Idosoapp
mvn test
mvn package
mvn exec:java
```

## Limitacoes Reais

- Os DAOs oferecem salvar, listar, buscar por ID, atualizar e remover para os fluxos principais.
- Operacoes de salvar, atualizar e remover retornam sucesso/falha ate a interface.
- Insercoes MySQL capturam o ID gerado pelo banco e atualizam o objeto salvo.
- DAOs em memoria atribuem ID incremental quando o objeto e salvo sem identificador.
- Listagens MySQL retornam erro para a interface quando a consulta falha, evitando mascarar falha de conexao como lista vazia.
- Erros SQL dos DAOs MySQL sao registrados por um utilitario comum, com SQLState e codigo do banco, sem espalhar `printStackTrace()` pela camada de persistencia.
- Controllers registram falhas por um utilitario comum, reduzindo repeticao e evitando mensagens com encoding inconsistente no console.
- Campos de texto sao normalizados antes da persistencia, reduzindo valores compostos apenas por espacos.
- O calculo percentual de eventos sentinela considera os eventos do prontuario no periodo informado antes de calcular o percentual por tipo.
- Consultas, prontuarios, prescricoes, vacinas, eventos e relatorios validam IDs relacionados antes da gravacao para exibir mensagens claras quando profissional, prontuario ou idosa nao existem.
- Em registros vinculados a prontuario, a interface usa o ID do prontuario na criacao. Na edicao, atualiza os campos proprios do registro, porque os models atuais nao carregam `prontuario_id` na listagem.
- Sem MySQL configurado, a interface abre, mas as listagens ficam vazias ou exibem feedback de conexao.

## Testes

Validado com:

```bash
..\maven\mvn\bin\mvn.cmd -q test
..\maven\mvn\bin\mvn.cmd -q package
```

A suite cobre os models principais e os DAOs em memoria para salvar com ID gerado, buscar, atualizar e remover.
O Maven usa `maven-surefire-plugin` explicitamente para executar os testes JUnit 5 de forma previsivel.
