package controle;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Departamento;
import persistencia.DepartamentoDAO;
import servico.Utilitario;

public class TelaListagemDepartamento implements Initializable,EventHandler<Event> {
    
    @FXML
    private VBox caixa;
    
    @FXML
    private Label titulo;
    
    @FXML
    private ToolBar barraFerramenta;
    
    @FXML
    private Button botaoNovo;
    
    @FXML
    private Button botaoVoltar;
    
    @FXML
    private TableView<Departamento> tabela;
    
    @FXML
    private TableColumn<Departamento,Integer> colunaTabelaId;
    
    @FXML
    private TableColumn<Departamento,String> colunaTabelaNome;
    
    private final Utilitario utilitario = new Utilitario();

    @Override
    public void initialize(URL endereco, ResourceBundle recurso) {
        try {
            tabela.setOnMousePressed(this);
            tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            colunaTabelaId.setCellValueFactory(new PropertyValueFactory<Departamento,Integer>("id"));
            colunaTabelaNome.setCellValueFactory(new PropertyValueFactory<Departamento,String>("nome"));
            DepartamentoDAO departamentoBd = new DepartamentoDAO();
            List<Departamento> departamentos = departamentoBd.buscaTodos();
            ObservableList<Departamento> lista = FXCollections.observableArrayList(departamentos);
            tabela.setItems(lista);
        } 
        catch (SQLException ex) {
            String mensagem = "Erro de servidor : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @Override
    public void handle(Event evento) {
        try {
            if (evento instanceof MouseEvent) {
                Departamento departamento = tabela.getSelectionModel().getSelectedItem();
                Stage estagio = new Stage();
                utilitario.abreTela(estagio, "/visao/tela_formulario_departamento.fxml", "Projeto JavaFX + JDBC", departamento);
            }
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }

    @FXML
    public void onBotaoNovoAction() {
        try {
            Stage estagio = new Stage();
            utilitario.abreTela(estagio, "/visao/tela_formulario_departamento.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @FXML
    public void onBotaoVoltarAction() {
        try {
            Stage estagio = new Stage();
            utilitario.abreTela(estagio, "/visao/tela_inicial.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
}
