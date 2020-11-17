package com.ps.ui;

import com.ps.executor.VM;
import com.ps.montador.Montador;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author Ari Vitor
 */
public class PrimaryController implements Initializable {

    public static Scene scene2;
    public Montador montador = new Montador();
    private SecondaryController controller;
    private VM vm = VM.getInstance();
    private Boolean fileChoser = false;
    private Boolean executeAll = false;
    private Boolean executeSTEP = false;
    private int stack_base = 20;

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
    private void executeAll() throws IOException {          
        //montador.main(selectedFile , vm.stack_base, fileChoser); 
        executeAll = true;
        if(!executeSTEP) { 
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
        
    }

    @FXML
    private void executeSTEP(ActionEvent event) throws IOException {
        executeSTEP = true;
        if (!executeAll){
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
    }

    private void nextStep(){
        vm.step();
        controller.inicializaNaTabelaMem();
    }
    
    @FXML
    private void stepbyStep(ActionEvent event) {
        if (!executeAll && executeSTEP){
            nextStep();
        }

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
