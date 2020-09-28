package controle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import servico.Utilitario;

public class TelaInicial {

    @FXML
    private AnchorPane painel;
    
    @FXML
    private MenuBar barraMenu;
    
    @FXML
    private Menu menuCadastro;
    
    @FXML
    private MenuItem itemMenuVendedor;
    
    @FXML
    private MenuItem itemMenuDepartamento;
    
    private final Utilitario utilitario = new Utilitario();
    
    @FXML
    public void onItemMenuVendedorAction() {
        try {
            Stage estagio = new Stage();
            utilitario.abreTela(estagio, "/visao/tela_listagem_vendedor.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
    @FXML
    public void onItemMenuDepartamentoAction() {
        try {
            Stage estagio = new Stage();
            utilitario.abreTela(estagio, "/visao/tela_listagem_departamento.fxml", "Projeto JavaFX + JDBC", null);
        }
        catch (Exception ex) {
            String mensagem = "Erro inesperado : " + ex.getMessage();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }

}
