package com.ps.ui;

import com.ps.executor.VM;
import com.ps.executor.Instruction;
import com.ps.models.EntradaTabela;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SecondaryController implements Initializable{
    
    //Variaveis de Classes
    private static SecondaryController controller = new SecondaryController(); //Criação da instancia, singleton
    private VM memoria = VM.getInstance(); // Chamando a instancia via metodo 
    
    //Variaveis auxiliares
    private List<EntradaTabela> listaDeEntradaTabela = new ArrayList<>(); // Lista dos valores para as tabela de memoria
    private ObservableList<EntradaTabela> obsEntradaTabela; //Tipo padão necessario para colocar nas tabelas FXML
    private List<EntradaTabela> listaDeEntradaPilha = new ArrayList<>(); // Lista dos valores para a tabela de pilha
    private ObservableList<EntradaTabela> obsEntradaPilha; //Tipo padão necessario para colocar nas tabelas FXML
    private short b = 0; //identifica se o bit 6 foi alterado;
    
    //Variaveis FXML
    @FXML
    private TableView< EntradaTabela > tbMemoriaGeral; //Tabela da memoria geral
    @FXML
    private TableColumn<EntradaTabela, Integer> tbEnderecoMemoriaUm; // Coluna de endereços da tabela de memoria
    @FXML
    private TableColumn<EntradaTabela, Short> tbDadoMemoriaUm; // Coluna de dados da tabela de memoria;
    @FXML
    private TextField regPC;
    @FXML
    private TextField regSP;
    @FXML
    private TextField regMOP;
    @FXML
    private TextField regRI;
    @FXML
    private TextField regRE;
    @FXML
    private TextField regACC;
    @FXML
    private TableView<EntradaTabela> tbPilhaGeral; //Tabela da pilha geral
    @FXML
    private TableColumn<EntradaTabela, Integer> tbOrdemPilha; // Coluna da ordem da tabela de pilha
    @FXML
    private TableColumn<EntradaTabela, Short> tbDadosPilha; // Coluna de dados da tabela de pilha

    public SecondaryController(){
    
    }
    
    //Função de inicialização da scene
    @Override
    public void initialize (URL location, ResourceBundle resources) {
    }
    
    
    //Função para mostrar o valor do ACC na scene
    @FXML
    void setACC() {
        regACC.setText(String.valueOf(memoria.regs.acc));
    }   
    //Função para mostrar o valor do MOP na scene
    @FXML
    void setMOP() {
        regMOP.setText(String.valueOf(memoria.regs.re_mode));
    }
    //Função para mostrar o valor do PC na scene
    @FXML
    void setPC() {
        regPC.setText(String.valueOf(memoria.regs.pc));
    }
    //Função para mostrar o valor do RE na scene
    @FXML
    void setRE() {
        regRE.setText(String.valueOf(memoria.regs.re));
    }
    //Função para mostrar o valor do RI na scene
    @FXML
    void setRI() {
        if(memoria.regs.ri > 63){ //verifica se o valor do opcode esta com o bit 6 alterado
            b = 64;
        }else{ b = 0;}
        switch (memoria.regs.ri - b) { //Caso o opcode esteja com o bit alterado ele diminui esse valor para a verificação e mostrar qual a instruçãoa tual
                case 2:
                    regRI.setText("ADD");
                    break;
                case 0:
                    regRI.setText("BR");
                    break;
                case 5:
                    regRI.setText("BRNEG");
                    break;
                case 1:
                    regRI.setText("BRPOS");
                    break;
                case 4:
                    regRI.setText("BRZERO");
                    break;
                case 15:
                    regRI.setText("CALL");
                    break;
                case 13:
                    regRI.setText("COPY");
                    break;
                case 10:
                    regRI.setText("DIVIDE");
                    break;
                case 3:
                    regRI.setText("LOAD");;
                    break;
                case 14:
                    regRI.setText("MULT");;
                    break;
                case 12:
                    regRI.setText("READ");
                    break;
                case 9:
                    regRI.setText("RET");
                    break;
                case 11:
                    regRI.setText("STOP");
                    break;
                case 7:
                    regRI.setText("STORE");
                    break;
                case 6:
                    regRI.setText("SUB");
                    break;
                case 8:
                    regRI.setText("WRITE");
                    break;
                default:
                    regRI.setText(String.valueOf(memoria.regs.ri));
                    break;
        }
    }
    //Função para mostrar o valor do SP na scene
    @FXML
    void setSP() {
        regSP.setText(String.valueOf(memoria.regs.sp));
    }
    //Função para inicializar os valores das tabelas na scene
    @FXML
    public void inicializaNaTabelaMem() {
        //Chama as funções para alterar os valores dos registradores
        this.setPC();
        this.setSP();
        this.setACC();
        this.setRE();
        this.setRI();
        this.setMOP();
        
        //Seta quais campos da classe EntradaTabela vão ser utilizados
        tbEnderecoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tbDadoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("memory"));
        
        //Seta os itens para a tabela
        tbMemoriaGeral.setItems(listaDeTabela());// Retorna o setItems do retorno do metodo listaDeTabela
    }

    private ObservableList<EntradaTabela> listaDeTabela(){
        //No caso de uma atualização, limpa as tabelas atuais
        listaDeEntradaTabela.clear();
        listaDeEntradaPilha.clear();
        //For par aler cada valor das listas
          for(int i=0; i<memoria.memory.length;i++){
            //Instancia um novo EntradaTabela com a s informações necessarias
            EntradaTabela entrada = new EntradaTabela( i, memoria.memory[i]);
            listaDeEntradaTabela.add(entrada); // Adiciona a nova instancio a AbservableList da memória
            if(i>2 && i<(memoria.memory[memoria.stack_base])){ //Separa os valores que vão entrar para a pilha
                enchePilha(entrada); //Chama o metodo similar a este que enche os valores da pilah
            }
            
        }
        return FXCollections.observableArrayList(listaDeEntradaTabela); //retorna a lista completa da memoria
    }
    
    //Prepara a ObservableList e da o seItems  da pilha
    private void enchePilha(EntradaTabela entrada){
        
        tbOrdemPilha.setCellValueFactory(new PropertyValueFactory<>("enderecoP"));
        tbDadosPilha.setCellValueFactory(new PropertyValueFactory<>("memory"));
        listaDeEntradaPilha.add(entrada);
        obsEntradaPilha = FXCollections.observableArrayList(listaDeEntradaPilha);
        tbPilhaGeral.setItems(obsEntradaPilha);
        
    }
    
    //get da lista de entrada tabela
    public List<EntradaTabela> getListaDeEntradaTabela() {
        return listaDeEntradaTabela;
    }
    //get da TableView da memoriaGEral
    public TableView<EntradaTabela> getTbMemoriaGeral() {
        return tbMemoriaGeral;
    }
    //Metodo get Instance do SecondaryController
    public static synchronized SecondaryController getInstance(){
        return controller;
    }
}