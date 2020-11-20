package com.ps.ui;

import com.ps.carregador.Carregador;
import com.ps.executor.Instruction;
import com.ps.executor.VM;
import com.ps.montador.Montador;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;

public class PrimaryController implements Initializable {

    //Variaveis de abertura de Scenes
    public static Scene scene2;
    private Stage stage;
    //Variaveis de instancia de classes
    public Montador montador = new Montador();
    private SecondaryController controller;
    private VM vm = VM.getInstance();
    private Carregador carregador = new Carregador();
    //Variaveis auxiliares
    private Boolean fileChoser = false;
    private Boolean newProgram = true;
    private Boolean stope = false;
    private Boolean sceneOpen = false;
    private Boolean changepilha = true;

    private String textIntegrated = null; 
    private String selectedFileEnd = new String("");
    private static short stack_base = 2;
    private Alert alert = new Alert(AlertType.ERROR);
    
    //Variaveis FXML
    @FXML
    private ToggleGroup fileSelect;
    @FXML
    private ToggleGroup stackSelect;
    @FXML
    private File selectedFile = null;
    @FXML
    private TextArea integratedFile;
    @FXML
    private TextField stackField;


    //Função de inicialização da Scene atual
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackField.setVisible(false);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro de entrada");
        alert.setContentText("Não há nenhum arquivo carregado.");
    } 
    
    //Função de Criação de nova scene
    private void newScene() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root = (Parent) loader.load();
        controller = (SecondaryController) loader.getController();
        stage = new Stage();
        scene2 = new Scene(root, 750, 800);
        stage.setScene(scene2);
        stage.getIcons().add(new Image(PrimaryController.class.getResourceAsStream("icon.png")));
        stage.setTitle("Execução");
    }

    //Funções que chamam execução 
    //Funções de execução completa
    @FXML
    private void executeAll() throws IOException {    
        if(textIntegrated != null || selectedFile != null){
            if(newProgram){
                carregador.carregaMem( montador.main(selectedFileEnd,fileChoser,textIntegrated) , stack_base, 512);
                newProgram = false;
            }      
            
            this.executingAll();
            
            if(!sceneOpen){
                this.newScene();
                stage.show();
                sceneOpen = true;
            }    
            
            controller.inicializaNaTabelaMem();
        }else alert.showAndWait();
    }
    
    private void executingAll(){
        while(!vm.step().instruction.isSTOP()) {
            System.out.println("Ran instruction");
            stope = true;
        }
        System.out.println("Stopped");
    }

    //Funções para StepByStep
    @FXML
    private void executeSTEP(ActionEvent event) throws IOException {
            this.executeStep();
    }
    
    private void executeStep() throws IOException{
        if(textIntegrated != null || selectedFile != null){
            if(newProgram){
                carregador.carregaMem( montador.main(selectedFileEnd,fileChoser,textIntegrated) , stack_base, 512);
                newProgram = false;
            }
        
            if(!sceneOpen){
                this.newScene();
                stage.show();
                sceneOpen = true;
            }
        
            this.nextStep();
        }else alert.showAndWait();
    }
    
    @FXML
    private void stepbyStep(ActionEvent event) {
        if(textIntegrated != null || selectedFile != null){
            if(vm.step().instruction.isSTOP() || stope == true){
                stope = true;
                controller.inicializaNaTabelaMem();
            }else controller.inicializaNaTabelaMem();
        }else alert.showAndWait();
    }
    
    private void nextStep(){
        vm.step();
        controller.inicializaNaTabelaMem();
    }

    //Funções para migrar entre maneiras de execução
    @FXML
    private void resetAll(ActionEvent event) throws FileNotFoundException {
        if(textIntegrated != null || selectedFile != null){
            carregador.carregaMem( montador.main(selectedFileEnd,fileChoser,textIntegrated) , stack_base, 512);
            stope = false;
            this.nextStep();
            controller.inicializaNaTabelaMem();
        }else alert.showAndWait();
    }

    //Funções do FXML dados
    //Funções referentes ao tamanho da pilha
    @FXML
    private void defaultStack(ActionEvent event) {
        stackField.setVisible(false);
        newProgram = true;
        changepilha = true;
        stack_base = 2;
        System.out.println(stack_base);
    }
    
    @FXML
    private void otherStack(ActionEvent event) {
        stackField.setVisible(true);
        newProgram = true;
        changepilha = true;
        //stack_base = Short.valueOf(stackField.getText());
    }
    
    @FXML
    private void textChange(KeyEvent event) {
         //enter atualiza a variavel
        stack_base = (short) parseInt("0" + stackField.getText());
        System.out.println(stack_base);
        newProgram = true;
        changepilha = true;
    }

    public int getStack(){
        return stack_base;
    }
    
    @FXML
    private void thisFile(ActionEvent event) {
        newProgram = true;
        fileChoser = false;
    }
    
    //Funções referentes a entrada do programa
    //Função para pegar programa de arquivo externo
    @FXML
    private void loadFile(ActionEvent event) {
        newProgram = true;
        fileChoser = true;
        FileChooser fc = new FileChooser();
        fc.setTitle("Arquivo de Entrada");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File("C:\\Users"));
        selectedFile = fc.showOpenDialog(null);
        selectedFileEnd = selectedFile.getAbsolutePath();
    }
    //Função para pegar programa digitado internamente na UI
 
    @FXML
    void getTextArea(KeyEvent event) {
        textIntegrated = integratedFile.getText();
        System.out.println(textIntegrated);
        newProgram = true;
    }


    
}