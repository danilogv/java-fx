package persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo.Departamento;
import modelo.Vendedor;
import static persistencia.BancoDados.conexao;
import assinatura.ObjetoDAO;

public class VendedorDAO extends BancoDados implements ObjetoDAO {

    @Override
    public void insere(Object objeto) throws SQLException {
        Vendedor vendedor = (Vendedor) objeto;
        String sql = "INSERT INTO Vendedor VALUES ('" + vendedor.getId() + "','" + vendedor.getNome() + "','" + vendedor.getEmail() + "','" + new java.sql.Date(vendedor.getDataNascimento().getTime()) + "','" + vendedor.getSalario() + "','" + vendedor.getDepartamento().getId() + "')";
        executaSql(sql);
    }

    @Override
    public void altera(Object objeto) throws SQLException {
        Vendedor vendedor = (Vendedor) objeto;
        String sql = "UPDATE Vendedor SET nome = '" + vendedor.getNome() + "',email = '" + vendedor.getEmail() + "',data_nascimento = '" + new java.sql.Date(vendedor.getDataNascimento().getTime()) + "',salario = '" + vendedor.getSalario() + "',departamento_id = '" + vendedor.getDepartamento().getId() + "' WHERE id = '" + vendedor.getId() + "'";
        executaSql(sql);
    }
    
    @Override
    public void remove(Integer id) throws SQLException {
        String sql = "DELETE FROM Vendedor WHERE id = '" + id + "'";
        executaSql(sql);
    }

    @Override
    public Object busca(Integer id) throws SQLException {
        String sql = "SELECT * FROM Vendedor WHERE id = '" + id + "'";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        Vendedor vendedor = new Vendedor();
        if (resultado.next())
            vendedor = (Vendedor) obtemDados(resultado);
        encerraServicos(declaracao, resultado);
        return vendedor;
    }

    @Override
    public List<Vendedor> buscaTodos() throws SQLException {
        List<Vendedor> vendedores = new ArrayList<Vendedor>();
        String sql = "SELECT * FROM Vendedor ORDER BY nome";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        while(resultado.next()) {
            Vendedor vendedor = obtemDados(resultado);
            vendedores.add(vendedor);
        }
        encerraServicos(declaracao, resultado);
        return vendedores;
    }
    
    @Override
    public boolean existe(Object objeto) throws SQLException {
        Vendedor vendedor = (Vendedor) objeto;
        String sql = "SELECT id FROM Vendedor WHERE nome = '" + vendedor.getNome().trim() + "' OR email = '" + vendedor.getEmail().trim() + "'";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        if (resultado.next()) {
            resultado.close();
            declaracao.close();
            return true;
        }
        encerraServicos(declaracao, resultado);
        return false;
    }
    
    @Override
    public Connection getConexao() {
        return conexao;
    }
    
    private void executaSql(String sql) throws SQLException {
        Statement declaracao = conexao.createStatement();
        declaracao.executeUpdate(sql);
        encerraServicos(declaracao, null);
    }
    
    private Vendedor obtemDados(ResultSet resultado) throws SQLException {
        Integer id = resultado.getInt("id");
        String nome = resultado.getString("nome");
        String email = resultado.getString("email");
        Date dataNascimento = resultado.getTimestamp("data_nascimento");
        BigDecimal salario = resultado.getBigDecimal("salario");
        Vendedor vendedor = new Vendedor();
        vendedor.setId(id);
        vendedor.setNome(nome);
        vendedor.setEmail(email);
        vendedor.setDataNascimento(dataNascimento);
        vendedor.setSalario(salario);
        Statement declaracao = conexao.createStatement();
        String sql = "SELECT * FROM Departamento WHERE id = '" + resultado.getInt("departamento_id") + "'";
        ResultSet resultado2 = declaracao.executeQuery(sql);
        if (resultado2.next()) {
            id = resultado2.getInt("id");
            nome = resultado2.getString("nome");
            Departamento departamento = new Departamento();
            departamento.setId(id);
            departamento.setNome(nome);
            vendedor.setDepartamento(departamento);
        }
        encerraServicos(declaracao, resultado2);
        return vendedor;
    }
    
    private void encerraServicos(Statement declaracao, ResultSet resultado) throws SQLException {
        if (resultado != null)
            resultado.close();
        if (declaracao != null)
            declaracao.close();
    }
    
}
