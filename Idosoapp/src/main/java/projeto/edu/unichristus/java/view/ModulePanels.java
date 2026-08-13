package projeto.edu.unichristus.java.view;

import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import projeto.edu.unichristus.java.controller.ConsultaController;
import projeto.edu.unichristus.java.controller.EventoSentinelaController;
import projeto.edu.unichristus.java.controller.IdosaController;
import projeto.edu.unichristus.java.controller.PrescricaoController;
import projeto.edu.unichristus.java.controller.ProfissionalSaudeController;
import projeto.edu.unichristus.java.controller.ProntuarioController;
import projeto.edu.unichristus.java.controller.RelatorioController;
import projeto.edu.unichristus.java.controller.VacinaController;
import projeto.edu.unichristus.java.model.Consulta;
import projeto.edu.unichristus.java.model.EventoSentinela;
import projeto.edu.unichristus.java.model.Idosa;
import projeto.edu.unichristus.java.model.Prescricao;
import projeto.edu.unichristus.java.model.ProfissionalSaude;
import projeto.edu.unichristus.java.model.ProntuarioMedico;
import projeto.edu.unichristus.java.model.Relatorio;
import projeto.edu.unichristus.java.model.TipoEventoSentinela;
import projeto.edu.unichristus.java.model.Vacina;

class IdosasPanel extends DataModulePanel<Idosa> {
    private final IdosaController controller = new IdosaController();
    private JTextField nome;
    private JTextField cpf;
    private JTextField nascimento;
    private JTextField mae;
    private JTextField sus;
    private JTextField entrada;

    IdosasPanel() {
        super("Modulo assistencial", "Cadastro de idosas", "Dados basicos das residentes e entrada na instituicao.",
            new String[] {"ID", "Nome", "CPF", "Nascimento", "Entrada"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        nome = new JTextField();
        cpf = new JTextField();
        nascimento = new JTextField();
        mae = new JTextField();
        sus = new JTextField();
        entrada = new JTextField();
        Ui.addField(form, 0, "Nome *", nome);
        Ui.addField(form, 1, "CPF *", cpf);
        Ui.addField(form, 2, "Nascimento", nascimento);
        Ui.addField(form, 3, "Nome da mae", mae);
        Ui.addField(form, 4, "Cartao SUS", sus);
        Ui.addField(form, 5, "Entrada", entrada);
        return form;
    }

    protected List<Idosa> loadRows() { return controller.listarIdosas(); }
    protected Object[] toColumns(Idosa i) { return new Object[] {i.getId(), i.getNome(), i.getCpf(), Ui.value(i.getDataNascimento()), Ui.value(i.getDataEntrada())}; }
    protected String details(Idosa i) { return "ID: " + i.getId() + "\nNome: " + i.getNome() + "\nCPF: " + i.getCpf() + "\nNascimento: " + Ui.value(i.getDataNascimento()) + "\nMae: " + Ui.value(i.getNomeMae()) + "\nCartao SUS: " + Ui.value(i.getCartaoSUS()) + "\nEntrada: " + Ui.value(i.getDataEntrada()); }
    protected Idosa readForm() {
        require(nome, "Nome");
        require(cpf, "CPF");
        return new Idosa(0, nome.getText().trim(), cpf.getText().trim(), Ui.parseDate(nascimento.getText(), "Nascimento"), mae.getText().trim(), sus.getText().trim(), Ui.parseDate(entrada.getText(), "Entrada"));
    }
    protected boolean save(Idosa value) { return controller.adicionarIdosa(value); }
    protected boolean update(Idosa value) { return controller.atualizarIdosa(value); }
    protected boolean remove(Idosa value) { return controller.removerIdosa(value.getId()); }
    protected String entityName() { return "Idosa"; }
    protected int idOf(Idosa value) { return value.getId(); }
    protected void setId(Idosa value, int id) { value.setId(id); }
    protected void populateForm(Idosa i) {
        nome.setText(Ui.value(i.getNome()));
        cpf.setText(Ui.value(i.getCpf()));
        nascimento.setText(Ui.formatDate(i.getDataNascimento()));
        mae.setText(Ui.value(i.getNomeMae()));
        sus.setText(Ui.value(i.getCartaoSUS()));
        entrada.setText(Ui.formatDate(i.getDataEntrada()));
    }
    protected void clearForm() {
        nome.setText("");
        cpf.setText("");
        nascimento.setText("");
        mae.setText("");
        sus.setText("");
        entrada.setText("");
    }
}

class ConsultasPanel extends DataModulePanel<Consulta> {
    private final ConsultaController controller = new ConsultaController();
    private JTextField dataHora;
    private JTextField profissionalId;
    private JTextField tipo;
    private JTextField motivo;
    private JTextField diagnostico;

    ConsultasPanel() {
        super("Acompanhamento clinico", "Agenda de consultas", "Consultas registradas com profissional, motivo e diagnostico.",
            new String[] {"ID", "Data/hora", "Profissional", "Tipo", "Motivo"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        dataHora = new JTextField();
        profissionalId = new JTextField();
        tipo = new JTextField();
        motivo = new JTextField();
        diagnostico = new JTextField();
        Ui.addField(form, 0, "Data/hora *", dataHora);
        Ui.addField(form, 1, "ID profissional *", profissionalId);
        Ui.addField(form, 2, "Tipo", tipo);
        Ui.addField(form, 3, "Motivo", motivo);
        Ui.addField(form, 4, "Diagnostico", diagnostico);
        return form;
    }

    protected List<Consulta> loadRows() { return controller.listarConsultas(); }
    protected Object[] toColumns(Consulta c) { return new Object[] {c.getId(), Ui.value(c.getDataHora()), c.getProfissional() != null ? c.getProfissional().getNome() : "", c.getTipo(), c.getMotivo()}; }
    protected String details(Consulta c) { return "ID: " + c.getId() + "\nData/hora: " + Ui.value(c.getDataHora()) + "\nProfissional: " + (c.getProfissional() != null ? c.getProfissional().getNome() : "") + "\nTipo: " + Ui.value(c.getTipo()) + "\nMotivo: " + Ui.value(c.getMotivo()) + "\nDiagnostico: " + Ui.value(c.getDiagnostico()); }
    protected Consulta readForm() {
        require(dataHora, "Data/hora");
        require(profissionalId, "ID profissional");
        ProfissionalSaude prof = new ProfissionalSaude();
        prof.setId(parseInt(profissionalId, "ID profissional"));
        return new Consulta(0, Ui.parseDateTime(dataHora.getText(), "Data/hora"), prof, tipo.getText().trim(), motivo.getText().trim(), diagnostico.getText().trim());
    }
    protected boolean save(Consulta value) { return controller.adicionarConsulta(value); }
    protected boolean update(Consulta value) { return controller.atualizarConsulta(value); }
    protected boolean remove(Consulta value) { return controller.removerConsulta(value.getId()); }
    protected String entityName() { return "Consulta"; }
    protected int idOf(Consulta value) { return value.getId(); }
    protected void setId(Consulta value, int id) { value.setId(id); }
    protected void populateForm(Consulta c) {
        dataHora.setText(Ui.formatDateTime(c.getDataHora()));
        profissionalId.setText(c.getProfissional() != null ? String.valueOf(c.getProfissional().getId()) : "");
        tipo.setText(Ui.value(c.getTipo()));
        motivo.setText(Ui.value(c.getMotivo()));
        diagnostico.setText(Ui.value(c.getDiagnostico()));
    }
    protected void clearForm() {
        dataHora.setText("");
        profissionalId.setText("");
        tipo.setText("");
        motivo.setText("");
        diagnostico.setText("");
    }
}

class ProntuariosPanel extends DataModulePanel<ProntuarioMedico> {
    private final ProntuarioController controller = new ProntuarioController();
    private JTextField dataHora;
    private JTextField idosaId;

    ProntuariosPanel() {
        super("Acompanhamento clinico", "Prontuarios medicos", "Contexto central que conecta idosa, consultas, prescricoes, vacinas e eventos.",
            new String[] {"ID", "Data/hora", "Idosa", "CPF"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        dataHora = new JTextField();
        idosaId = new JTextField();
        Ui.addField(form, 0, "Data/hora", dataHora);
        Ui.addField(form, 1, "ID idosa *", idosaId);
        return form;
    }

    protected List<ProntuarioMedico> loadRows() { return controller.listarProntuarios(); }
    protected Object[] toColumns(ProntuarioMedico p) { return new Object[] {p.getId(), Ui.value(p.getDataHoraIdosa()), p.getIdosa() != null ? p.getIdosa().getNome() : "", p.getIdosa() != null ? p.getIdosa().getCpf() : ""}; }
    protected String details(ProntuarioMedico p) { return "ID: " + p.getId() + "\nData/hora: " + Ui.value(p.getDataHoraIdosa()) + "\nIdosa: " + (p.getIdosa() != null ? p.getIdosa().getNome() : "") + "\nCPF: " + (p.getIdosa() != null ? p.getIdosa().getCpf() : "") + "\n\n" + p.gerarResumoHistorico(); }
    protected ProntuarioMedico readForm() {
        require(idosaId, "ID idosa");
        Idosa idosa = new Idosa();
        idosa.setId(parseInt(idosaId, "ID idosa"));
        LocalDateTime when = dataHora.getText().trim().isEmpty() ? LocalDateTime.now() : Ui.parseDateTime(dataHora.getText(), "Data/hora");
        ProntuarioMedico prontuario = new ProntuarioMedico();
        prontuario.setDataHoraIdosa(when);
        prontuario.setIdosa(idosa);
        return prontuario;
    }
    protected boolean save(ProntuarioMedico value) { return controller.adicionarProntuario(value); }
    protected boolean update(ProntuarioMedico value) { return controller.atualizarProntuario(value); }
    protected boolean remove(ProntuarioMedico value) { return controller.removerProntuario(value.getId()); }
    protected String entityName() { return "Prontuario"; }
    protected int idOf(ProntuarioMedico value) { return value.getId(); }
    protected void setId(ProntuarioMedico value, int id) { value.setId(id); }
    protected void populateForm(ProntuarioMedico p) {
        dataHora.setText(Ui.formatDateTime(p.getDataHoraIdosa()));
        idosaId.setText(p.getIdosa() != null ? String.valueOf(p.getIdosa().getId()) : "");
    }
    protected void clearForm() {
        dataHora.setText("");
        idosaId.setText("");
    }
}

class PrescricoesPanel extends DataModulePanel<Prescricao> {
    private final PrescricaoController controller = new PrescricaoController();
    private JTextField prontuarioId;
    private JTextField medicamento;
    private JTextField posologia;
    private JTextField duracao;
    private JTextField observacoes;

    PrescricoesPanel() {
        super("Cuidado medicamentoso", "Prescricoes e medicacoes", "Registro de medicamentos, posologia, duracao e observacoes.",
            new String[] {"ID", "Medicamento", "Posologia", "Duracao"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        prontuarioId = new JTextField();
        medicamento = new JTextField();
        posologia = new JTextField();
        duracao = new JTextField();
        observacoes = new JTextField();
        Ui.addField(form, 0, "ID prontuario *", prontuarioId);
        Ui.addField(form, 1, "Medicamento *", medicamento);
        Ui.addField(form, 2, "Posologia", posologia);
        Ui.addField(form, 3, "Duracao", duracao);
        Ui.addField(form, 4, "Observacoes", observacoes);
        return form;
    }

    protected List<Prescricao> loadRows() { return controller.listarPrescricoes(); }
    protected Object[] toColumns(Prescricao p) { return new Object[] {p.getId(), p.getMedicamento(), p.getPosologia(), p.getDuracao()}; }
    protected String details(Prescricao p) { return "ID: " + p.getId() + "\nMedicamento: " + Ui.value(p.getMedicamento()) + "\nPosologia: " + Ui.value(p.getPosologia()) + "\nDuracao: " + Ui.value(p.getDuracao()) + "\nObservacoes: " + Ui.value(p.getObservacoes()); }
    protected Prescricao readForm() { if (!isEditing()) { require(prontuarioId, "ID prontuario"); } require(medicamento, "Medicamento"); return new Prescricao(0, medicamento.getText().trim(), posologia.getText().trim(), duracao.getText().trim(), observacoes.getText().trim()); }
    protected boolean save(Prescricao value) { return controller.adicionarPrescricao(value, parseInt(prontuarioId, "ID prontuario")); }
    protected boolean update(Prescricao value) { return controller.atualizarPrescricao(value); }
    protected boolean remove(Prescricao value) { return controller.removerPrescricao(value.getId()); }
    protected String entityName() { return "Prescricao"; }
    protected int idOf(Prescricao value) { return value.getId(); }
    protected void setId(Prescricao value, int id) { value.setId(id); }
    protected void populateForm(Prescricao p) {
        prontuarioId.setText("");
        medicamento.setText(Ui.value(p.getMedicamento()));
        posologia.setText(Ui.value(p.getPosologia()));
        duracao.setText(Ui.value(p.getDuracao()));
        observacoes.setText(Ui.value(p.getObservacoes()));
    }
    protected void clearForm() {
        prontuarioId.setText("");
        medicamento.setText("");
        posologia.setText("");
        duracao.setText("");
        observacoes.setText("");
    }
}

class ProfissionaisPanel extends DataModulePanel<ProfissionalSaude> {
    private final ProfissionalSaudeController controller = new ProfissionalSaudeController();
    private JTextField nome;
    private JTextField especialidade;
    private JTextField registro;

    ProfissionaisPanel() {
        super("Equipe assistencial", "Profissionais de saude", "Cadastro de profissionais vinculados as consultas.",
            new String[] {"ID", "Nome", "Especialidade", "Registro"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        nome = new JTextField();
        especialidade = new JTextField();
        registro = new JTextField();
        Ui.addField(form, 0, "Nome *", nome);
        Ui.addField(form, 1, "Especialidade", especialidade);
        Ui.addField(form, 2, "Registro", registro);
        return form;
    }

    protected List<ProfissionalSaude> loadRows() { return controller.listarProfissionais(); }
    protected Object[] toColumns(ProfissionalSaude p) { return new Object[] {p.getId(), p.getNome(), p.getEspecialidade(), p.getRegistroProfissional()}; }
    protected String details(ProfissionalSaude p) { return "ID: " + p.getId() + "\nNome: " + Ui.value(p.getNome()) + "\nEspecialidade: " + Ui.value(p.getEspecialidade()) + "\nRegistro: " + Ui.value(p.getRegistroProfissional()); }
    protected ProfissionalSaude readForm() { require(nome, "Nome"); return new ProfissionalSaude(0, nome.getText().trim(), especialidade.getText().trim(), registro.getText().trim()); }
    protected boolean save(ProfissionalSaude value) { return controller.adicionarProfissional(value); }
    protected boolean update(ProfissionalSaude value) { return controller.atualizarProfissional(value); }
    protected boolean remove(ProfissionalSaude value) { return controller.removerProfissional(value.getId()); }
    protected String entityName() { return "Profissional"; }
    protected int idOf(ProfissionalSaude value) { return value.getId(); }
    protected void setId(ProfissionalSaude value, int id) { value.setId(id); }
    protected void populateForm(ProfissionalSaude p) {
        nome.setText(Ui.value(p.getNome()));
        especialidade.setText(Ui.value(p.getEspecialidade()));
        registro.setText(Ui.value(p.getRegistroProfissional()));
    }
    protected void clearForm() {
        nome.setText("");
        especialidade.setText("");
        registro.setText("");
    }
}

class VacinasPanel extends DataModulePanel<Vacina> {
    private final VacinaController controller = new VacinaController();
    private JTextField prontuarioId;
    private JTextField nome;
    private JTextField data;

    VacinasPanel() {
        super("Prevencao e historico", "Vacinas", "Registro de vacinas aplicadas e data de ocorrencia.",
            new String[] {"ID", "Nome", "Data"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        prontuarioId = new JTextField();
        nome = new JTextField();
        data = new JTextField();
        Ui.addField(form, 0, "ID prontuario *", prontuarioId);
        Ui.addField(form, 1, "Nome *", nome);
        Ui.addField(form, 2, "Data", data);
        return form;
    }

    protected List<Vacina> loadRows() { return controller.listarVacinas(); }
    protected Object[] toColumns(Vacina v) { return new Object[] {v.getId(), v.getNome(), Ui.value(v.getDataOcorrencia())}; }
    protected String details(Vacina v) { return "ID: " + v.getId() + "\nNome: " + Ui.value(v.getNome()) + "\nData: " + Ui.value(v.getDataOcorrencia()); }
    protected Vacina readForm() { if (!isEditing()) { require(prontuarioId, "ID prontuario"); } require(nome, "Nome"); return new Vacina(0, nome.getText().trim(), Ui.parseDate(data.getText(), "Data")); }
    protected boolean save(Vacina value) { return controller.adicionarVacina(value, parseInt(prontuarioId, "ID prontuario")); }
    protected boolean update(Vacina value) { return controller.atualizarVacina(value); }
    protected boolean remove(Vacina value) { return controller.removerVacina(value.getId()); }
    protected String entityName() { return "Vacina"; }
    protected int idOf(Vacina value) { return value.getId(); }
    protected void setId(Vacina value, int id) { value.setId(id); }
    protected void populateForm(Vacina v) {
        prontuarioId.setText("");
        nome.setText(Ui.value(v.getNome()));
        data.setText(Ui.formatDate(v.getDataOcorrencia()));
    }
    protected void clearForm() {
        prontuarioId.setText("");
        nome.setText("");
        data.setText("");
    }
}

class EventosPanel extends DataModulePanel<EventoSentinela> {
    private final EventoSentinelaController controller = new EventoSentinelaController();
    private JTextField prontuarioId;
    private JComboBox<TipoEventoSentinela> tipo;
    private JTextField data;

    EventosPanel() {
        super("Seguranca assistencial", "Eventos sentinela", "Ocorrencias relevantes para acompanhamento e relatorios.",
            new String[] {"ID", "Tipo", "Data"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        prontuarioId = new JTextField();
        tipo = new JComboBox<TipoEventoSentinela>(TipoEventoSentinela.values());
        data = new JTextField();
        Ui.addField(form, 0, "ID prontuario *", prontuarioId);
        Ui.addComponent(form, 1, "Tipo *", tipo);
        Ui.addField(form, 2, "Data", data);
        return form;
    }

    protected List<EventoSentinela> loadRows() { return controller.listarEventos(); }
    protected Object[] toColumns(EventoSentinela e) { return new Object[] {e.getId(), Ui.value(e.getTipo()), Ui.value(e.getDataOcorrencia())}; }
    protected String details(EventoSentinela e) { return "ID: " + e.getId() + "\nTipo: " + Ui.value(e.getTipo()) + "\nData: " + Ui.value(e.getDataOcorrencia()); }
    protected EventoSentinela readForm() { if (!isEditing()) { require(prontuarioId, "ID prontuario"); } return new EventoSentinela(0, (TipoEventoSentinela) tipo.getSelectedItem(), Ui.parseDate(data.getText(), "Data")); }
    protected boolean save(EventoSentinela value) { return controller.adicionarEvento(value, parseInt(prontuarioId, "ID prontuario")); }
    protected boolean update(EventoSentinela value) { return controller.atualizarEvento(value); }
    protected boolean remove(EventoSentinela value) { return controller.removerEvento(value.getId()); }
    protected String entityName() { return "Evento"; }
    protected int idOf(EventoSentinela value) { return value.getId(); }
    protected void setId(EventoSentinela value, int id) { value.setId(id); }
    protected void populateForm(EventoSentinela e) {
        prontuarioId.setText("");
        tipo.setSelectedItem(e.getTipo());
        data.setText(Ui.formatDate(e.getDataOcorrencia()));
    }
    protected void clearForm() {
        prontuarioId.setText("");
        tipo.setSelectedIndex(0);
        data.setText("");
    }
}

class RelatoriosPanel extends DataModulePanel<Relatorio> {
    private final RelatorioController controller = new RelatorioController();
    private JTextField prontuarioId;
    private JTextField descricao;
    private JTextField tipo;

    RelatoriosPanel() {
        super("Operacao e analise", "Relatorios operacionais", "Descricoes e tipos de relatorios persistidos no sistema.",
            new String[] {"ID", "Tipo", "Descricao"});
    }

    protected JPanel buildForm() {
        JPanel form = Ui.formPanel();
        prontuarioId = new JTextField();
        tipo = new JTextField();
        descricao = new JTextField();
        Ui.addField(form, 0, "ID prontuario *", prontuarioId);
        Ui.addField(form, 1, "Tipo *", tipo);
        Ui.addField(form, 2, "Descricao *", descricao);
        return form;
    }

    protected List<Relatorio> loadRows() { return controller.listarRelatorios(); }
    protected Object[] toColumns(Relatorio r) { return new Object[] {r.getId(), r.getTipo(), r.getDescricao()}; }
    protected String details(Relatorio r) { return "ID: " + r.getId() + "\nTipo: " + Ui.value(r.getTipo()) + "\nDescricao: " + Ui.value(r.getDescricao()); }
    protected Relatorio readForm() { if (!isEditing()) { require(prontuarioId, "ID prontuario"); } require(tipo, "Tipo"); require(descricao, "Descricao"); return new Relatorio(0, descricao.getText().trim(), tipo.getText().trim()); }
    protected boolean save(Relatorio value) { return controller.adicionarRelatorio(value, parseInt(prontuarioId, "ID prontuario")); }
    protected boolean update(Relatorio value) { return controller.atualizarRelatorio(value); }
    protected boolean remove(Relatorio value) { return controller.removerRelatorio(value.getId()); }
    protected String entityName() { return "Relatorio"; }
    protected int idOf(Relatorio value) { return value.getId(); }
    protected void setId(Relatorio value, int id) { value.setId(id); }
    protected void populateForm(Relatorio r) {
        prontuarioId.setText("");
        tipo.setText(Ui.value(r.getTipo()));
        descricao.setText(Ui.value(r.getDescricao()));
    }
    protected void clearForm() {
        prontuarioId.setText("");
        tipo.setText("");
        descricao.setText("");
    }
}
