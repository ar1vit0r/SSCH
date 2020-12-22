package com.ps.ui;

import com.ps.carregador.Carregador;
import com.ps.executor.Instruction;
import com.ps.executor.VM;
import com.ps.montador.Montador;
import com.ps.macros.Processador_de_macros;
import com.ps.ligador.Ligador;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;

public class PrimaryController implements Initializable {

    //Variaveis de abertura de Scenes
    public static Scene scene2;
    public static Scene scene3;
    private Stage stage;
    private Stage stage1;
    //Variaveis de instancia de classes
    public Processador_de_macros proc_macros = new Processador_de_macros();
    public Montador montador = new Montador();
    private SecondaryController controller;
    private DialogTwoController controller2;
    private VM vm = VM.getInstance();
    private Carregador carregador = new Carregador();
    private String textIntegrated = null; 
    private Alert alert = new Alert(AlertType.ERROR);
    //Variaveis auxiliares
    private Boolean newProgram = true; //false se o programa ja esta aberto.
    private Boolean stope = false; //true quando termina a execução
    private Boolean sceneOpen = false; //true se a tela secundaria ja estiver aberta
    private Boolean carregado = false; //true se ja passou pelo carregador alguma vez
    
    private static short stack_base = 12;
    private String lastPathsCarregator;
    
    //Variaveis FXML
    @FXML
    private ToggleGroup fileSelect;
    @FXML
    private ToggleGroup stackSelect;
    @FXML
    private TextArea integratedFile;
    @FXML
    private TextField stackField;
    @FXML
    private RadioButton isexecutingnow;
    @FXML
    private TextField pathMacros;
    @FXML
    private TextField pathMontador;
    @FXML
    private TextField pathLigador1;
    @FXML
    private TextField pathLigador2;
    @FXML
    private TextField pathCarregador;


    //Função de inicialização da Scene atual
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isexecutingnow.setDisable(true);
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
        scene2 = new Scene(root, 750, 815);
        stage.setScene(scene2);
        stage.getIcons().add(new Image(PrimaryController.class.getResourceAsStream("icon.png")));
        stage.setTitle("Execução");
    }
    
    private void newScene2() throws IOException{
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("dialogTwo.fxml"));
        Parent root2 = (Parent) loader2.load();
        controller2 = (DialogTwoController) loader2.getController();
        stage1 = new Stage();
        scene3 = new Scene(root2, 600, 600);
        stage1.setScene(scene3);
        stage1.getIcons().add(new Image(PrimaryController.class.getResourceAsStream("icon.png")));
        stage1.setTitle("Help");
        stage1.show();
    }
     
    @FXML
    private void executeAll() throws IOException {    
        if(textIntegrated != null && carregado){
            isexecutingnow.setDisable(false);
            isexecutingnow.setSelected(true);
            isexecutingnow.setDisable(true);
            
            this.executingAll();
            
            if(!sceneOpen){
                this.newScene();
                stage.show();
                sceneOpen = true;
            }    
            
            controller.inicializaNaTabelaMem();
        }else alert.showAndWait();
    }
    

    private void executingAll() throws IOException{
        if(Instruction.withOpcode[vm.memory[vm.regs.pc] & 0b1111].isREAD()){
         controller.newScene();
        }
        while(!vm.step().instruction.isSTOP()) {
            System.out.println("Ran instruction");
            stope = true;
        }
        System.out.println("Stopped");
    }

    //Funções que chamam execução 
    //Funções para criação dos arquivos e preparação para execução
    @FXML
    void execCarregador(ActionEvent event) {
        String path = pathCarregador.getText();
        chamaCarregador(path);
    }

    private void chamaCarregador(String path){
        lastPathsCarregator = path;
        //Carregador.carregaMem(lastPathsCarregator, 512);
        carregado = true;
    }

    @FXML
    void execLigador(ActionEvent event) throws IOException {
        String path1 = pathLigador1.getText();
        String path2 = pathLigador2.getText();
        Ligador.main(path1,path2);
    }

    @FXML
    void execMontador(ActionEvent event) throws IOException {
        String path = pathMontador.getText();
        chamaMontador(path);
    }

    private void chamaMontador(String path) throws IOException{
        Montador.montar(path, stack_base);
    }

    @FXML
    void execProcMacros(ActionEvent event) {
        String path = pathMacros.getText();
        chamaMacro(path);
    }

    private void chamaMacro(String path){
        Processador_de_macros.run(path);
    }

    //Funções de execução completa
    @FXML
    private void opendialogTwo() throws IOException {    
       this.newScene2();
    }

    //Funções para StepByStep
    @FXML
    private void executeSTEP(ActionEvent event) throws IOException {
            this.executeStep();
    }
    
    private void executeStep() throws IOException{
        if(textIntegrated != null && carregado){
            isexecutingnow.setDisable(false);
            isexecutingnow.setSelected(true);
            isexecutingnow.setDisable(true);
            
        
            if(!sceneOpen){
                this.newScene();
                stage.show();
                sceneOpen = true;
            }
        
            this.nextStep();
        }else alert.showAndWait();
    }
    
    @FXML
    private void stepbyStep(ActionEvent event) throws IOException {
        if(textIntegrated != null && carregado){
            isexecutingnow.setDisable(false);
            isexecutingnow.setSelected(true);
            isexecutingnow.setDisable(true);
            if(Instruction.withOpcode[vm.memory[vm.regs.pc] & 0b1111].isREAD()){
                controller.newScene();
            }
            if(vm.step().instruction.isSTOP() || stope == true){
                stope = true;
                controller.inicializaNaTabelaMem();
            }else controller.inicializaNaTabelaMem();
        }else alert.showAndWait();
    }
    
    private void nextStep() throws IOException{
        if(Instruction.withOpcode[vm.memory[vm.regs.pc ] & 0b1111].isREAD()){
            System.out.println("Foi");
            controller.newScene();
        }
        vm.step();
        controller.inicializaNaTabelaMem();
    }

    //Funções para migrar entre maneiras de execução
    @FXML
    private void resetAll(ActionEvent event) throws FileNotFoundException, IOException {
        if(textIntegrated != null && carregado){
            isexecutingnow.setDisable(false);
            isexecutingnow.setSelected(false);
            isexecutingnow.setDisable(true);
            chamaCarregador(lastPathsCarregator);
            vm.regs.pc = 0;
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
        stack_base = 2;
        System.out.println(stack_base);
    }
    
    @FXML
    private void otherStack(ActionEvent event) {
        stackField.setVisible(true);
        newProgram = true;
        //stack_base = Short.valueOf(stackField.getText());
    }
    
    @FXML
    private void textChange(KeyEvent event) {
         //enter atualiza a variavel
        stack_base = (short) parseInt("0" + stackField.getText());
        System.out.println(stack_base);
        newProgram = true;
    }
    //Funções referentes a entrada do programa
    //Função que vai criar um arquivo com o programa digitado na interface e executar o proc_macros e o montador nele 
    @FXML
    void link_start(ActionEvent event) throws IOException {
        String nomeArq = nomeMod();
        File arquivo = new File(nomeArq+".txt", "w+");
        FileWriter fileWriter = new FileWriter(arquivo, false);
        BufferedWriter escrever = new BufferedWriter(fileWriter);
        String linhas[] = textIntegrated.split("\n");
        for(String iterator : linhas){
            escrever.write(iterator);
            escrever.newLine();
        }
        escrever.close();
        fileWriter.close();
        chamaMacro(nomeArq);
        chamaMontador(nomeArq);
        
    }

    private String nomeMod(){
        String tudo[] = textIntegrated.split(" ");
        for( int i=0; i< tudo.length; i++){
            if(tudo[i].toUpperCase().equals("START")){
                while(tudo[i+1].equals(" ")){
                    i++;
                }
                return tudo[i+1];    
            }
        }
        return null;
    }
    //Função para pegar programa digitado internamente na UI
 
    @FXML
    void getTextArea(KeyEvent event) {
        textIntegrated = integratedFile.getText();
        newProgram = true;
    }
    
}