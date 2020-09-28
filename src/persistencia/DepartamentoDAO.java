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
import assinatura.ObjetoDAO;

public class DepartamentoDAO extends BancoDados implements ObjetoDAO {

    @Override
    public void insere(Object objeto) throws SQLException {
        Departamento departamento = (Departamento) objeto;
        String sql = "INSERT INTO Departamento VALUES ('" + departamento.getId() + "','" + departamento.getNome() + "')";
        executaSql(sql);
    }

    @Override
    public void altera(Object objeto) throws SQLException {
        Departamento departamento = (Departamento) objeto;
        String sql = "UPDATE Departamento SET nome = '" + departamento.getNome() + "' WHERE id = '" + departamento.getId() + "'";
        executaSql(sql);
    }
    
    @Override
    public void remove(Integer id) throws SQLException {
        String sql = "DELETE FROM Departamento WHERE id = '" + id + "'";
        executaSql(sql);
    }

    @Override
    public Object busca(Integer id) throws SQLException {
        String sql = "SELECT * FROM Departamento WHERE id = '" + id + "'";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        Departamento departamento = new Departamento();
        if (resultado.next())
            departamento = (Departamento) obtemDados(resultado);
        encerraServicos(declaracao,resultado);
        return departamento;
    }

    @Override
    public List<Departamento> buscaTodos() throws SQLException {
        List<Departamento> departamentos = new ArrayList<Departamento>();
        String sql = "SELECT * FROM Departamento ORDER BY nome";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        while (resultado.next()) {
            Departamento departamento = obtemDados(resultado);
            departamentos.add(departamento);
        }
        encerraServicos(declaracao,resultado);
        return departamentos;
    }
    
    @Override
    public boolean existe(Object objeto) throws SQLException {
        Departamento departamento = (Departamento) objeto;
        String sql = "SELECT id FROM Departamento WHERE nome = '" + departamento.getNome().trim() + "'";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado = declaracao.executeQuery(sql);
        if (resultado.next()) {
            resultado.close();
            declaracao.close();
            return true;
        }
        encerraServicos(declaracao,resultado);
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
    
    private Departamento obtemDados(ResultSet resultado) throws SQLException {
        Integer id = resultado.getInt("id");
        String nome = resultado.getString("nome");
        Departamento departamento = new Departamento();
        departamento.setId(id);
        departamento.setNome(nome);
        String sql = "SELECT * FROM Vendedor WHERE departamento_id = '" + id + "' ORDER BY nome";
        Statement declaracao = conexao.createStatement();
        ResultSet resultado2 = declaracao.executeQuery(sql);
        List<Vendedor> vendedores = new ArrayList<Vendedor>();
        while (resultado2.next()) {
            id = resultado2.getInt("id");
            nome = resultado2.getString("nome");
            String email = resultado2.getString("email");
            Date dataNascimento = resultado2.getDate("data_nascimento");
            BigDecimal salario = resultado2.getBigDecimal("salario");
            Vendedor vendedor = new Vendedor();
            vendedor.setId(id);
            vendedor.setNome(nome);
            vendedor.setEmail(email);
            vendedor.setDataNascimento(dataNascimento);
            vendedor.setSalario(salario);
            vendedores.add(vendedor);
        }
        departamento.setVendedores(vendedores);
        encerraServicos(declaracao, resultado2);
        return departamento;
    }
    
    private void encerraServicos(Statement declaracao, ResultSet resultado) throws SQLException {
        if (resultado != null)
            resultado.close();
        if (declaracao != null)
            declaracao.close();
    }
    
}
