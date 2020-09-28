import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import persistencia.BancoDados;
import servico.Utilitario;

public class Principal extends Application {
    
    @Override
    public void start(Stage estagio) {
        try {
            BancoDados.abreBancoDados();
            Utilitario evento = new Utilitario();
            evento.abreTela(estagio, "/visao/tela_inicial.fxml", "Projeto JavaFX + JDBC", null);
        } 
        catch (ClassNotFoundException | SQLException ex) {
            String mensagem = "Erro de servidor : " + ex.getMessage();
            Utilitario utilitario = new Utilitario();
            utilitario.mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
