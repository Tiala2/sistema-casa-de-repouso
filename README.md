# Sistema Casa de Repouso - IdosoApp

Sistema de gerenciamento de prontuários médicos e informações clínicas para o Recanto do Sagrado Coração (RSC), uma casa de repouso para idosas.

## 📋 Sobre o Projeto

O IdosoApp é uma aplicação desenvolvida para facilitar o gerenciamento de informações clínicas e administrativas de idosas residentes em instituições de longa permanência. O sistema permite o cadastro completo de residentes, registro de consultas médicas, prescrições, vacinas, eventos sentinela e geração de relatórios para acompanhamento da saúde.

Este projeto foi desenvolvido como trabalho de extensão para informatizar o prontuário médico institucional, promovendo maior segurança, qualidade no cuidado e eficiência nos processos administrativos.

## 🎯 Objetivos

- Centralizar informações clínicas das idosas residentes
- Facilitar o acompanhamento do histórico médico individualizado
- Registrar consultas, prescrições, vacinas e eventos sentinela
- Gerar relatórios para análise e tomada de decisão
- Otimizar fluxos internos da instituição
- Garantir rastreabilidade de todas as ações de saúde

## 🏗️ Arquitetura do Sistema

O projeto segue a arquitetura MVC (Model-View-Controller) com separação clara de responsabilidades:

```
Idosoapp/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── projeto/edu/unichristus/java/
│   │   │       ├── controller/    # Controladores e lógica de interface
│   │   │       ├── model/         # Entidades do domínio
│   │   │       └── dao/           # Acesso a dados
│   │   └── resources/
│   └── test/
│       └── java/                  # Testes unitários
└── target/                        # Artefatos de build
```

### Componentes Principais

- **Model**: Entidades do domínio (Idosa, ProntuarioMedico, Consulta, Prescricao, Vacina, EventoSentinela, Relatorio, ProfissionalSaude)
- **DAO**: Camada de acesso a dados com implementações para MySQL
- **Controller**: Lógica de negócio e coordenação entre camadas
- **Tests**: Testes unitários com JUnit para garantir qualidade

## 🗄️ Modelo de Dados

O sistema utiliza um modelo relacional com as seguintes entidades principais:

- **idosa**: Dados pessoais das residentes (nome, CPF, data de nascimento, cartão SUS, etc)
- **profissional_saude**: Cadastro de profissionais (médicos, enfermeiros, etc)
- **prontuario_medico**: Prontuário vinculado a cada idosa
- **consulta**: Registro de consultas médicas
- **prescricao**: Prescrições médicas e medicamentos
- **vacina**: Registro de vacinação
- **evento_sentinela**: Eventos importantes de saúde
- **relatorio**: Relatórios e documentos

### Relacionamentos

```
idosa (1) ──── (N) prontuario_medico
prontuario_medico (1) ──── (N) consulta, prescricao, vacina, evento_sentinela, relatorio
profissional_saude (1) ──── (N) consulta
```

## 🚀 Tecnologias Utilizadas

- **Java JDK 8**: Linguagem principal
- **Maven**: Gerenciamento de dependências e build
- **MySQL**: Banco de dados relacional
- **PostgreSQL**: Suporte alternativo de banco de dados
- **JUnit 4/5**: Framework de testes unitários
- **Git/GitHub**: Controle de versão

## 📦 Dependências

As principais dependências do projeto (definidas no `pom.xml`):

- MySQL Connector Java 8.0.26
- PostgreSQL 42.2.23
- JUnit 4.13.2
- JUnit Jupiter 5.10.2

## 🔧 Configuração do Ambiente

### Pré-requisitos

1. Java JDK 8 ou superior
2. Maven (incluído na pasta `maven/` do projeto)
3. MySQL Server 8.0 ou superior
4. Git

### Configuração do Banco de Dados

1. Instale e inicie o MySQL Server
2. Execute o script de criação do banco:

```bash
mysql -u root -p < criar_banco_idosoapp.sql
```

3. Configure as credenciais de acesso no código conforme necessário:
   - URL: `jdbc:mysql://localhost:3306/idosoapp`
   - Usuário: `root`
   - Senha: (conforme sua instalação)

## 🏃 Como Executar

### Compilar e Testar

No Windows (PowerShell):

```powershell
.\maven\mvn\bin\mvn.cmd clean test -f .\Idosoapp\pom.xml
```

No Linux/Mac:

```bash
./maven/bin/mvn clean test -f ./Idosoapp/pom.xml
```

### Executar os Testes

```bash
.\maven\mvn\bin\mvn.cmd test -f .\Idosoapp\pom.xml
```

### Build do Projeto

```bash
.\maven\mvn\bin\mvn.cmd clean package -f .\Idosoapp\pom.xml
```

## 🎮 Funcionalidades

### Módulo de Idosas
- Cadastro de residentes com dados pessoais completos
- Consulta e atualização de informações
- Visualização do histórico médico

### Módulo de Prontuário Médico
- Criação automática de prontuário para cada idosa
- Registro de todas as ações de saúde
- Histórico completo e individualizado

### Módulo de Consultas
- Registro de consultas médicas
- Vinculação com profissionais de saúde
- Diagnósticos e motivos de atendimento

### Módulo de Prescrições
- Registro de medicamentos prescritos
- Posologia e duração do tratamento
- Observações e orientações

### Módulo de Vacinas
- Controle de vacinação
- Registro de datas e tipos de vacina
- Histórico completo de imunização

### Módulo de Eventos Sentinela
- Registro de eventos importantes de saúde
- Tipos diversos (quedas, internações, etc)
- Acompanhamento temporal

### Módulo de Relatórios
- Geração de relatórios diversos
- Acompanhamento de indicadores
- Suporte à tomada de decisão

## 🧪 Testes

O projeto possui cobertura de testes unitários para todas as entidades principais:

- `IdosaTest`: Testes da entidade Idosa
- `ProntuarioMedicoTest`: Testes do prontuário médico
- `ConsultaTest`: Testes de consultas
- `PrescricaoTest`: Testes de prescrições
- `VacinaTest`: Testes de vacinas
- `EventoSentinelaTest`: Testes de eventos sentinela
- `RelatorioTest`: Testes de relatórios
- `ProfissionalSaudeTest`: Testes de profissionais

## 🔒 Segurança e Boas Práticas

- Validação de dados de entrada
- Tratamento de exceções e erros
- Uso de prepared statements para prevenir SQL injection
- Estrutura modular facilitando manutenção
- Código testado e documentado

### Recomendações de Segurança

- Implementar autenticação de usuários (Spring Security)
- Adicionar controle de acesso baseado em perfis
- Realizar backups periódicos do banco de dados
- Utilizar HTTPS em produção
- Criptografar dados sensíveis

## 📈 Melhorias Futuras

- [ ] Interface gráfica web responsiva (Angular/React/Thymeleaf)
- [ ] Autenticação e autorização de usuários
- [ ] Dashboards com visualização de indicadores
- [ ] Exportação de relatórios em PDF
- [ ] Integração com sistemas externos (e-SUS)
- [ ] Módulo de agendamento de consultas
- [ ] Alertas e notificações automáticas
- [ ] Deploy em nuvem com CI/CD
- [ ] App mobile para acesso remoto

## 🤝 Dificuldades e Aprendizados

Durante o desenvolvimento, enfrentamos alguns desafios:

- **Integração**: Conexão entre interface gráfica e banco de dados
- **Modelagem**: Definição dos relacionamentos entre entidades
- **Ambiente**: Configuração do Maven no Windows
- **Conversão**: Tratamento de datas e tipos de dados
- **Git**: Resolução de conflitos e submódulos

Todas as dificuldades foram superadas com orientação, pesquisa e colaboração da equipe.

## 📚 Referências

- [Recanto Sagrado Coração (Facebook)](https://www.facebook.com/RECANTOSAGRADOCORA/)
- [MySQL Documentation](https://www.mysql.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/)
- [Maven Documentation](https://maven.apache.org/)
- [JUnit Documentation](https://junit.org/)
- [Java Documentation](https://docs.oracle.com/javase/8/docs/)

## 👥 Contribuição

Este projeto foi desenvolvido como trabalho de extensão universitária. Contribuições são bem-vindas através de:

1. Fork do projeto
2. Criação de branch para feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit das alterações (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para o branch (`git push origin feature/nova-funcionalidade`)
5. Abertura de Pull Request

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte de um trabalho de extensão universitária.

## 📞 Contato

Para mais informações sobre o projeto ou sobre o Recanto do Sagrado Coração, visite a [página no Facebook](https://www.facebook.com/RECANTOSAGRADOCORA/).

---

**Desenvolvido com ❤️ para o Recanto do Sagrado Coração**
