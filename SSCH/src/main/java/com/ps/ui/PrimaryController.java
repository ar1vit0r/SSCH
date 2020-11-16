/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.ui;

import com.ps.executor.Instruction;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.ps.executor.VM;
import com.ps.montador.Montador;

/**
 * FXML Controller class
 *
 * @author Ari Vitor
 */
public class PrimaryController implements Initializable {

    public static Scene scene2;
    public VM vm;
    public Montador montador = new Montador();
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
    private SecondaryController controller;
    
    @FXML
    private ToggleGroup fileSelect;
    private File selectedFile;
    private TextArea integratedFile;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vm = new VM();
        
        vm.regs.pc = 1;
        vm.memory = new short[] {1,1,1,2,1,(short)Instruction.STOP.opcode};
        while(!vm.step().instruction.isSTOP()) {
            System.out.println("Ran instruction");
        }
        System.out.println("Stopped");
    }    
    
    @FXML
    private void executeAll() throws IOException {       
        Parent root = loader.load(getClass().getResource("secondary.fxml"));
        Stage stage = new Stage();
        scene2 = new Scene(root, 750, 800);
        stage.setScene(scene2);
        stage.show();
        controller.getInstance();
        controller.inicializaNaTabelaMem(vm);
        
    }

    @FXML
    private void executeSTEP(ActionEvent event) {
    }

    @FXML
    private void stopAll(ActionEvent event) {
    }

    @FXML
    private void resetAll(ActionEvent event) {
    }

    @FXML
    private void thisFile(ActionEvent event) {

    }

    @FXML
    private void loadFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Arquivo de Entrada");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File("C:\\Users"));
        selectedFile = fc.showOpenDialog(null);
        //short[] variavel recebe montador(selectedFiel);
        
    }

    @FXML
    private String getTextArea(KeyEvent event) {
        return integratedFile.getText();
    }

    static void setRoot(String fxml) throws IOException {
        scene2.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
