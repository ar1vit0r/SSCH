/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.ui;

import com.ps.executor.VM;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 *
 * @author Ari Vitor
 */
public class dialogOneController {
    
    private VM memoria = VM.getInstance(); // Chamando a instancia via metodo 
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    
    @FXML
    private TextField readwrite;
    
    public void initialize (URL location, ResourceBundle resources) {
        readwrite.setText("");
        alert.setTitle("Erro");
        alert.setHeaderText("Erro de entrada");
        alert.setContentText("Insira um valor.");
    }
    
       //Função para ler entrada de READ ou WRITE
    @FXML
    private void readwrite() throws IOException {
        if( !readwrite.getText().isBlank() ){
            memoria.memory[ memoria.regs.pc + 1 ] = (short) parseInt( readwrite.getText() );
        }else alert.showAndWait();
    }
}
