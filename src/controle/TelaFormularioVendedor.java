package controle;

import excecao.ValidationException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modelo.Departamento;
import modelo.Vendedor;
import persistencia.DepartamentoDAO;
import persistencia.VendedorDAO;
import servico.Utilitario;

public class TelaFormularioVendedor implements Initializable,ChangeListener<String>,Runnable {
    
    @FXML
    private AnchorPane painel;
    
    @FXML
    private GridPane painelFormulario;
    
    @FXML
    private Label textoId;
    
    @FXML
    private Label textoNome;
    
    @FXML
    private Label textoEmail;
    
    @FXML
    private Label textoDataNascimento;
    
    @FXML
    private Label textoSalario;
    
    @FXML
    private Label textoDepartamento;
    
    @FXML
    private TextField formularioId;
    
    @FXML
    private TextField formularioNome;
    
    @FXML
    private TextField formularioEmail;
    
    @FXML
    private DatePicker formularioDataNascimento;
    
    @FXML
    private TextField formularioSalario;
    
    @FXML
    private ComboBox<Departamento> formularioDepartamento;
    
    @FXML
    private HBox caixa;
    
    @FXML
    private Button botaoSalvar;
    
    @FXML
    private Button botaoExcluir;
    
    @FXML
    private Button botaoCancelar;
    
    private final BigDecimal SALARIO_MINIMO = BigDecimal.valueOf(1045.00);
    
    private final Integer IDADE_MINIMA = 18;
    
    private final Utilitario utilitario = new Utilitario();
    
    @Override
    public void initialize(URL endereco, ResourceBundle recurso) {
        try {
            Platform.runLater(this);
            formularioSalario.textProperty().addListener(this);
            DepartamentoDAO departamentoBd = new DepartamentoDAO();
            List<Departamento> departamentos = departamentoBd.buscaTodos();
            ObservableList<Departamento> lista = FXCollections.observableArrayList(departamentos);
            formularioDepartamento.setItems(lista);
        }
        catch (SQLException ex) {
            String mensagem = "Erro de servidor : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        DecimalFormat valorFormatado = new DecimalFormat("#,###");
        newValue = newValue.replace(".", "").trim();
        if (!newValue.matches("\\d*([\\,]\\d{0,2})?"))
            formularioSalario.setText(oldValue);
        else {
            if (newValue.contains(","))
                return;
            if (!newValue.equals(""))
                formularioSalario.setText(valorFormatado.format(Double.parseDouble(newValue)));
        }
    }
    
    @Override
    public void run() {
        formularioNome.requestFocus();
    }
  
    @FXML
    public void onBotaoSalvarAction() {
        VendedorDAO vendedorBd = new VendedorDAO();
        try {
            Integer id = 0;
            if (!formularioId.getText().equals(""))
                id = Integer.parseInt(formularioId.getText());
            String nome = formularioNome.getText();
            String email = formularioEmail.getText();
            Date dataNascimento = Date.from(formularioDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            BigDecimal salario = new BigDecimal(Double.parseDouble(formularioSalario.getText().replace(".", "").replace(",", ".").trim()));
            Departamento departamento = formularioDepartamento.getSelectionModel().getSelectedItem();
            Vendedor vendedor = new Vendedor();
            vendedor.setId(id);
            vendedor.setNome(nome);
            vendedor.setEmail(email);
            vendedor.setDataNascimento(dataNascimento);
            vendedor.setSalario(salario);
            vendedor.setDepartamento(departamento);
            validaCampos(vendedor, vendedorBd);
            vendedorBd.getConexao().setAutoCommit(false);
            if (id == 0)
                vendedorBd.insere(vendedor);
            else
                vendedorBd.altera(vendedor);
            vendedorBd.getConexao().commit();
            utilitario.mostrarAlerta("Sucesso", Alert.AlertType.CONFIRMATION, null, "Operação realizada com sucesso.");
            esvaziaCampos();
        }
        catch (SQLException ex) {
            try {
                if (vendedorBd.getConexao() != null)
                    vendedorBd.getConexao().rollback();
            } 
            catch (SQLException e) {
                String mensagem = "Erro de servidor : " + e.getMessage();
                utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
            }
            String mensagem = "Erro de servidor : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
        catch (ValidationException ex) {
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, ex.getMessage());
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @FXML
    public void onBotaoExcluirAction() {
        VendedorDAO vendedorBd = new VendedorDAO();
        try {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Deseja realmente excluir o vendedor ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alerta.showAndWait();
            if (alerta.getResult() == ButtonType.YES) {
                Integer id = Integer.valueOf(formularioId.getText());
                vendedorBd.getConexao().setAutoCommit(false);
                vendedorBd.remove(id);
                vendedorBd.getConexao().commit();
                utilitario.mostrarAlerta("Sucesso", Alert.AlertType.CONFIRMATION, null, "Exclusão realizada com sucesso.");
                esvaziaCampos();
            }
        }
        catch (SQLException ex) {
            try {
                if (vendedorBd.getConexao() != null)
                    vendedorBd.getConexao().rollback();
            } 
            catch (SQLException e) {
                String mensagem = "Erro de servidor : " + e.getMessage();
                utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
            }
            String mensagem = "Erro de servidor : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @FXML
    public void onBotaoCancelarAction() {
        try {
            Stage estagio = new Stage();
            utilitario.abreTela(estagio, "/visao/tela_listagem_vendedor.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    public void inicializaCampos(Vendedor vendedor) {
        DecimalFormat valorFormatado = new DecimalFormat("#,###.00");
        formularioId.setText(String.valueOf(vendedor.getId()));
        formularioNome.setText(vendedor.getNome());
        formularioEmail.setText(vendedor.getEmail());
        formularioDataNascimento.setValue(vendedor.getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        formularioSalario.setText(valorFormatado.format(vendedor.getSalario()));
        formularioDepartamento.setValue(vendedor.getDepartamento());
    }
    
    private void esvaziaCampos() {
        formularioId.setText("");
        formularioNome.setText("");
        formularioEmail.setText("");
        formularioDataNascimento.setValue(null);
        formularioSalario.setText("");
        formularioDepartamento.getSelectionModel().select(null);
    }
    
    private void validaCampos(Vendedor vendedor, VendedorDAO vendedorBd) throws SQLException {
        if (vendedor.getNome().equals(""))
            throw new ValidationException("Informe o NOME");
        if (vendedor.getEmail().equals(""))
            throw new ValidationException("Informe o E-mail");
        if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(vendedor.getEmail()).find())
            throw new ValidationException("E-mail inválido");
        if (vendedor.getDataNascimento() == null)
            throw new ValidationException("Informe a DATA DE NASCIMENTO");
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -this.IDADE_MINIMA);
        if (vendedor.getDataNascimento().compareTo(calendario.getTime()) > 0)
            throw new ValidationException("Idade mínima de 18 anos para o vendedor");
        if (vendedor.getSalario().compareTo(this.SALARIO_MINIMO) < 0)
            throw new ValidationException("Salário inferior ao mínimo");
        if (vendedorBd.existe(vendedor))
            throw new ValidationException("Vendedor já existente");
    }
    
}
