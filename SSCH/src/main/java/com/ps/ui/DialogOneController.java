package com.ps.ui;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Ari Vitor
 */
public class DialogOneController implements Initializable{
    
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    
    @FXML
    private TextField readwrite;
    @FXML
    private Button ok;
    
    public DialogOneController(){
    
    }
    
    @Override
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
            short aux = (short) parseInt( readwrite.getText() );
            System.out.print(aux + "\n");
        }else alert.showAndWait();
        
        Stage stage = (Stage) readwrite.getScene().getWindow(); //Obtendo a janela atual
        stage.close();
    }
}
