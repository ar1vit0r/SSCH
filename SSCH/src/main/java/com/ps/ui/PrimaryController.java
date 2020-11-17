/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.ui;

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
import com.ps.montador.Montador;


/**
 * FXML Controller class
 *
 * @author Ari Vitor
 */
public class PrimaryController implements Initializable {

    public static Scene scene2;
    private SecondaryController controller;
    private VM vm = VM.getInstance();
    private Boolean fileChoser = false;
    private int stack_base = 20;

    public Montador montador = new Montador();
    
    
    @FXML
    private ToggleGroup fileSelect;
    private File selectedFile;
    private TextArea integratedFile;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
<<<<<<< HEAD
    private void executeAll() throws IOException {       
        //if(!fileChoser){
            // ler o texto do programa e chamar o montador aqui
        //} //else montador.main(selectedFile , vm.stack_base);
        
=======
    private void executeAll() throws IOException {
>>>>>>> 597c8abb9f05fbd93f32983eedffbd9e45a29d09
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root = (Parent) loader.load();
        controller = (SecondaryController) loader.getController();
        System.out.println(controller);
        Stage stage = new Stage();
        scene2 = new Scene(root, 750, 800);
        stage.setScene(scene2);
        stage.show();
        controller.inicializaNaTabelaMem();
        
    }

    @FXML
    private void executeSTEP(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root = (Parent) loader.load();
        controller = (SecondaryController) loader.getController();
        System.out.println(controller);
        Stage stage = new Stage();
        scene2 = new Scene(root, 750, 800);
        stage.setScene(scene2);
        stage.show();
        
        nextStep();
    }

    private void nextStep(){
        vm.step();
       controller.inicializaNaTabelaMem();
    }
    
    @FXML
    private void stopAll(ActionEvent event) {
        nextStep();
    }

    @FXML
    private void resetAll(ActionEvent event) {
        //Chama o montador outra vez e recarrega a memoria com os valores originais do programa
    }

    @FXML
    private void thisFile(ActionEvent event) {
        fileChoser = false;
    }

    @FXML
    private void loadFile(ActionEvent event) {
        fileChoser = true;
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
