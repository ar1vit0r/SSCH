package com.ps.ui;

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
import javafx.scene.control.RadioButton;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;


public class PrimaryController implements Initializable {

    //Variaveis de abertura de Scenes
    public static Scene scene2;
    private Stage stage;
    //Variaveis de instancia de classes
    public Montador montador = new Montador();
    private SecondaryController controller;
    private VM vm = VM.getInstance();
    //Variaveis auxiliares
    private Boolean fileChoser = false;
    private Boolean newProgram = true;
    private Boolean stope = false;
    private Boolean sceneOpen = false;
    private Boolean changepilha = true;

    private String textIntegrated; 
    private short stack_base = 2;
    
    //Variaveis FXML
    @FXML
    private ToggleGroup fileSelect;
    @FXML
    private ToggleGroup stackSelect;
    @FXML
    private File selectedFile;
    @FXML
    private TextArea integratedFile;
    @FXML
    private TextField stackField;
    @FXML
    private RadioButton integrated;
    @FXML
    private RadioButton other;


    //Função de inicialização da Scene atual
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackField.setVisible(false);
        //if(integrated.isSelected()){ this.getTextArea();}
        //else if(other.isSelected()){}
        //String aux = String.valueOf(stack_base);
        //stackField.setText(aux);
    } 
    
    //Função de Criação de nova scene
    private void newScene() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root = (Parent) loader.load();
        controller = (SecondaryController) loader.getController();
        System.out.println(controller);
        stage = new Stage();
        scene2 = new Scene(root, 750, 800);
        stage.setScene(scene2);
    }

    //Funções que chamam execução 
    //Funções de execução completa
    @FXML
    private void executeAll() throws IOException {    
        //this.getTextArea();
        if(newProgram){
            //montador.main(selectedFile , vm.stack_base, fileChoser); 
            vm.memory = new short[] {21,21,21,22,21,21,21,21,21,21,21,21,21,21,21,21,(short)Instruction.STOP.opcode,21,21,21,21,21,21};
            //executeAll = true;
            //trabalho do carregador start
            if(changepilha){
                stack_base += 2;
                changepilha = false;
            }
            vm.memory[2] = stack_base;
            vm.regs.pc = stack_base;
            //end
            newProgram = false;
        }      
            
        this.executingAll();
            
        if(!sceneOpen){
            this.newScene();
            stage.show();
            sceneOpen = true;
        }    
            
        controller.inicializaNaTabelaMem();
        
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
        //this.getTextArea();
        System.out.println(stope);
            this.executeStep();
    }
    
    private void executeStep() throws IOException{
        if(newProgram){
            //montador.main(selectedFile , vm.stack_base, fileChoser); 
            vm.memory = new short[] {21,21,21,22,21,21,21,21,21,21,21,21,21,21,21,21,(short)Instruction.STOP.opcode,21,21,21,21,21,21};
            //executeAll = true;
            //trabalho do carregador start
            if(changepilha){
                stack_base += 2;
                changepilha = false;
            }
            vm.memory[2] = stack_base;
            vm.regs.pc = stack_base;
            //end
            newProgram = false;
        }
        
        if(!sceneOpen){
            this.newScene();
            stage.show();
            sceneOpen = true;
        }
        
        
        this.nextStep();
    }
    
    @FXML
    private void stepbyStep(ActionEvent event) {
        System.out.println(stope);
            if(vm.step().instruction.isSTOP() || stope == true){
                stope = true;
            }else nextStep();
    }
    
    private void nextStep(){
        vm.step();
        controller.inicializaNaTabelaMem();
    }

    //Funções para migrar entre maneiras de execução
    @FXML
    private void resetAll(ActionEvent event) {
        //Chama o montador outra vez e recarrega a memoria com os valores originais do programa e fecha e atualiza a janela 2
        //Só pra teste
        vm.memory = new short[] {21,21,21,22,21,21,21,21,21,21,21,21,21,21,21,21,(short)Instruction.STOP.opcode,21,21,21,21,21,21};
        vm.memory[2] = stack_base;
        vm.regs.pc = stack_base;
        stope = false;
        controller.inicializaNaTabelaMem();
    }

    //Funções do FXML dados
    //Funções referentes ao tamanho da pilha
    @FXML
    private void defaultStack(ActionEvent event) {
        stackField.setVisible(false);
        newProgram = true;
        changepilha = true;
        stack_base = 2;
    }
    
    @FXML
    private void otherStack(ActionEvent event) {
        stackField.setVisible(true);
        newProgram = true;
        changepilha = true;
        //stack_base = Short.valueOf(stackField.getText());
    }
    
    @FXML
    private void textChange(InputMethodEvent event) {
         //enter atualiza a variavel
        stack_base = (short) parseInt(stackField.getText());
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
        //short[] variavel recebe montador(selectedFiel);
        
    }
    //Função para pegar programa digitado internamente na UI
 
    @FXML
    private void getTextArea() {
        newProgram = true;
        textIntegrated = integratedFile.getText();
        System.out.println(textIntegrated);
    }

}