package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Departamento implements Serializable {
    
    private Integer id;
    private String nome;
    private List<Vendedor> vendedores = new ArrayList<Vendedor>();

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Vendedor> getVendedores() {
        return this.vendedores;
    }

    public void setVendedores(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }
    
    @Override
    public String toString() {
        return this.nome;
    }
    
}
