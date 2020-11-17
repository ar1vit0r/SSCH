package com.ps.models;

/**
 *
 * @author Matheus Chaves
 */
public class EntradaTabela {
    
    private Integer endereco;
    private Short memory;
    private Integer enderecoP;
    private String hmemory;
  
    
    
    public EntradaTabela( Integer endereco,short memory){
        this.memory = memory;
        this.endereco = endereco;
        this.hmemory = Integer.toHexString(this.memory.intValue());
        this.enderecoP = endereco - 2;
    }

    public Integer getEnderecoP() {
        return enderecoP;
    }

    public void setEnderecoP(Integer enderecoP) {
        this.enderecoP = enderecoP;
    }
    
    public String getHmemory() {
        return hmemory;
    }

    public void setHmemory(String hmemory) {
        this.hmemory = hmemory;
    }

    public Integer getEndereco() {
        return endereco;
    }

    public void setEndereco(Integer endereco) {
        this.endereco = endereco;
    }

    public Short getMemory() {
        return memory;
    }

    public void setMemory(Short memory) {
        this.memory = memory;
    }
    
    
}
