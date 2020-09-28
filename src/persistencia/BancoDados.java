package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDados {
    
    protected static Connection conexao;
    
    public static void abreBancoDados() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/java_fx";
        String usuario = "root";
        String senha = "";
        Class.forName(driver);
        conexao = DriverManager.getConnection(url, usuario, senha);
    }
    
    public static void fechaBancoDados() throws SQLException {
        if (conexao != null) 
            conexao.close();
    }
}
