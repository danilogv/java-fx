package controle;

import excecao.ValidationException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modelo.Departamento;
import persistencia.DepartamentoDAO;
import servico.Utilitario;

public class TelaFormularioDepartamento implements Initializable,Runnable {
    
    @FXML
    private AnchorPane painel;
    
    @FXML
    private GridPane painelFormulario;
    
    @FXML
    private Label textoId;
    
    @FXML
    private Label textoNome;
    
    @FXML
    private TextField formularioId;
    
    @FXML
    private TextField formularioNome;
    
    @FXML
    private HBox caixa;
    
    @FXML
    private Button botaoSalvar;
    
    @FXML
    private Button botaoExcluir;
    
    @FXML
    private Button botaoCancelar;
    
    private final Utilitario utilitario = new Utilitario();
    
    @Override
    public void initialize(URL endereco, ResourceBundle recurso) {
        Platform.runLater(this);
    }
    
    @Override
    public void run() {
        formularioNome.requestFocus();
    }
    
    @FXML
    public void onBotaoSalvarAction() {
        DepartamentoDAO departamentoBd = new DepartamentoDAO();
        try {
            Integer id = 0;
            if (!formularioId.getText().equals(""))
                id = Integer.parseInt(formularioId.getText());
            String nome = formularioNome.getText();
            Departamento departamento = new Departamento();
            departamento.setId(id);
            departamento.setNome(nome);
            validaCampos(departamento, departamentoBd);
            departamentoBd.getConexao().setAutoCommit(false);
            if (id == 0)
                departamentoBd.insere(departamento);
            else
                departamentoBd.altera(departamento);
            departamentoBd.getConexao().commit();
            utilitario.mostrarAlerta("Sucesso", Alert.AlertType.CONFIRMATION, null, "Operação realizada com sucesso.");
            esvaziaCampos();
        } 
        catch (SQLException ex) {
            try {
                if (departamentoBd.getConexao() != null)
                    departamentoBd.getConexao().rollback();
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
        DepartamentoDAO departamentoBd = new DepartamentoDAO();
        try {
            String mensagem = "Deseja realmente excluir o departamento ? Seus vendedores associados também serão apagados.";
            Alert alerta = new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alerta.showAndWait();
            if (alerta.getResult() == ButtonType.YES) {
                Integer id = Integer.valueOf(formularioId.getText());
                departamentoBd.getConexao().setAutoCommit(false);
                departamentoBd.remove(id);
                departamentoBd.getConexao().commit();
                utilitario.mostrarAlerta("Sucesso", Alert.AlertType.CONFIRMATION, null, "Exclusão realizada com sucesso.");
                esvaziaCampos();
            }
        }
        catch (SQLException ex) {
            try {
                if (departamentoBd.getConexao() != null)
                    departamentoBd.getConexao().rollback();
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
            utilitario.abreTela(estagio, "/visao/tela_listagem_departamento.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    public void inicializaCampos(Departamento departamento) {
        formularioId.setText(String.valueOf(departamento.getId()));
        formularioNome.setText(departamento.getNome());
    }
    
    private void esvaziaCampos() {
        formularioId.setText("");
        formularioNome.setText("");
    }
    
    private void validaCampos(Departamento departamento, DepartamentoDAO departamentoBd) throws SQLException {
        if (departamento.getNome().equals(""))
            throw new ValidationException("Informe o NOME");
        if (departamentoBd.existe(departamento))
            throw new ValidationException("Departamento já existente");
    }
    
}
