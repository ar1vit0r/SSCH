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
import javafx.scene.input.InputMethodEvent;


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
    private Boolean executeAll = false;
    private Boolean executeSTEP = false;
    private short stack_base = 2;
    private String textIntegrated; 
    private Boolean stope = false;

    //Variaveis FXML
    @FXML
    private ToggleGroup fileSelect;
    private ToggleGroup stackSelect;
    private File selectedFile;
    private TextArea integratedFile;
    private TextField stackField;


    //Função de inicialização da Scene atual
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        //montador.main(selectedFile , vm.stack_base, fileChoser); 
        if(!executeSTEP) { 
            vm.memory = new short[] {21,21,21,22,21,21,21,21,21,21,21,21,21,21,21,21,(short)Instruction.STOP.opcode,21,21,21,21,21,21};
            executeAll = true;
            this.newScene();

            //trabalho do carregador start
            stack_base += 2;
            vm.memory[2] = stack_base;
            vm.regs.pc = stack_base;
            //end
            
            this.executingAll();
            
            stage.show();

            
            
            controller.inicializaNaTabelaMem();
        }else{
            this.executingAll();
            controller.inicializaNaTabelaMem();
        }
        
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
        System.out.println(stope);
        if (!executeAll){
            this.executeStep();
        }else{
            //fechar segunda tela
            //executeStep();
        }
    }
    @FXML
    private void stepbyStep(ActionEvent event) {
        System.out.println(stope);
        if (!executeAll && executeSTEP){
            if(vm.step().instruction.isSTOP() || stope == true){
                stope = true;
            }else nextStep();
        }

    }
    private void executeStep() throws IOException{
        vm.memory = new short[] {21,21,21,22,21,21,21,21,21,21,21,21,21,21,21,21,(short)Instruction.STOP.opcode,21,21,21,21,21,21};
        executeSTEP = true;
        
        this.newScene();

        stage.show();
        
        //trabalho do carregador start
        stack_base += 2;
        vm.memory[2] = stack_base;
        vm.regs.pc = stack_base;
        //end
        
        this.nextStep();
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
        vm.regs.pc = (short) (stack_base + 2);
        stope = false;
        controller.inicializaNaTabelaMem();
    }


    //Funções do FXML dados
    //Funções referentes ao tamanho da pilha
    @FXML
    private void defaultStack(ActionEvent event) {
        stack_base = 2;
        
    }
    
    @FXML
    private void otherStack(ActionEvent event) {
        stack_base = Short.valueOf(stackField.getText());
    }
    
    @FXML
    private void textChange(InputMethodEvent event) {
        //quando o texto mudar, att a variavel
        
        

    }

    //Funções referentes a entrada do programa
    //Função para pegar programa de arquivo externo
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
    //Função para pegar programa digitado internamente na UI
    @FXML
    private void getTextArea(InputMethodEvent event) {
        textIntegrated = integratedFile.getText();
    }
    @FXML
    private void thisFile(ActionEvent event) {
        fileChoser = false;
    }

    
    

}