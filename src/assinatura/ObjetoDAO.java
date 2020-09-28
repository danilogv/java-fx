package assinatura;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ObjetoDAO {
    
    void insere(Object objeto) throws SQLException;
    void altera(Object objeto) throws SQLException;
    void remove(Integer id) throws SQLException;
    Object busca(Integer id) throws SQLException;
    boolean existe(Object objeto) throws SQLException;
    List<? extends Object> buscaTodos() throws SQLException;
    Connection getConexao();
    
}
