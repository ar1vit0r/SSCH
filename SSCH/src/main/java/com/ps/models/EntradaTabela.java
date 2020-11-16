/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.models;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



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
