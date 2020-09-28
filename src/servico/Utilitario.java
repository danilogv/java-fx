package servico;

import controle.TelaFormularioDepartamento;
import controle.TelaFormularioVendedor;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.Departamento;
import modelo.Vendedor;
import persistencia.BancoDados;

public class Utilitario implements EventHandler<WindowEvent> {
    
    private static Stage estagioAtual;
    
    public void abreTela(Stage estagio, String endereco, String titulo, Object objeto) {
        try {
            FXMLLoader projeto = new FXMLLoader(Utilitario.class.getResource(endereco));
            Scene cena = new Scene(projeto.load());
            estagio.setScene(cena);
            estagio.setTitle(titulo);
            estagio.setOnCloseRequest(this);
            if (objeto != null)
                if (objeto instanceof Departamento) {
                    TelaFormularioDepartamento tela = projeto.getController();
                    tela.inicializaCampos((Departamento) objeto);
                }
                else {
                    TelaFormularioVendedor tela = projeto.getController();
                    tela.inicializaCampos((Vendedor) objeto);
                }
            estagio.show();
            if (estagioAtual != null)
                estagioAtual.close();
            estagioAtual = estagio;
        }
        catch (IOException ex) {
            String mensagem = "XML não encontrado : " + ex.getMessage();
            mostrarAlerta("Erro", AlertType.ERROR, null, mensagem);
        }
    }
    
    public void mostrarAlerta(String titulo, AlertType tipo, String cabecalho, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecalho);
        alerta.setContentText(mensagem);
        alerta.show();
    }

    @Override
    public void handle(WindowEvent event) {
        try {
            BancoDados.fechaBancoDados();
        } 
        catch (SQLException ex) {
            String mensagem = "Erro de Servidor : " + ex.getMessage();
            mostrarAlerta("Erro", Alert.AlertType.ERROR, null, mensagem);
        }
    }
    
}
