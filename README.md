# Sistema de Gestao de Casa de Repouso

Aplicacao desktop em Java para apoiar a gestao de uma casa de repouso. O sistema organiza cadastros, prontuarios, consultas, prescricoes, vacinas, eventos sentinela e relatorios operacionais em uma interface Swing com navegacao clara e blocos de informacao.

## Arquitetura

- `Idosoapp/src/main/java/projeto/edu/unichristus/java/model`: entidades do dominio.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/dao`: persistencia JDBC/MySQL.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/controller`: servicos de aplicacao usados pela interface.
- `Idosoapp/src/main/java/projeto/edu/unichristus/java/view`: interface desktop Swing.
- `Idosoapp/src/main/resources/application.properties`: configuracao local de banco, sem credenciais pessoais.

O projeto permanece em Java 8, Maven, JDBC e MVC. A modernizacao visual nao muda o dominio nem transforma a aplicacao em web.

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

- dashboard com resumo vindo das listagens reais;
- navegacao lateral com secao ativa;
- cabecalhos de modulo com contexto, proposito e acao principal;
- area principal para tabela e area secundaria para detalhes/formulario;
- estados vazios, mensagens de sucesso, erro e confirmacao de remocao;
- formularios com validacao basica antes de acionar controllers/DAOs;
- criacao, edicao e remocao integradas aos metodos reais de persistencia.

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

As conexoes JDBC foram centralizadas em `DatabaseConnection`, reduzindo repeticao nos DAOs.

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
- Consultas, prescricoes, vacinas, eventos e relatorios dependem de IDs existentes de profissional, prontuario ou idosa conforme as chaves estrangeiras do banco.
- Em registros vinculados a prontuario, a interface usa o ID do prontuario na criacao. Na edicao, atualiza os campos proprios do registro, porque os models atuais nao carregam `prontuario_id` na listagem.
- Sem MySQL configurado, a interface abre, mas as listagens ficam vazias ou exibem feedback de conexao.

## Testes

Validado com:

```bash
..\maven\mvn\bin\mvn.cmd -q test
..\maven\mvn\bin\mvn.cmd -q package
```
