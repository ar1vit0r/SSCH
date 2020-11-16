package com.ps.ui;


import com.ps.executor.Instruction;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.Scene;
import com.ps.executor.VM;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ps.models.EntradaTabela;


public class SecondaryController implements Initializable{
    
    
    private static SecondaryController INSTANCE;
    private List<EntradaTabela> listaDeEntradaTabela = new ArrayList<>();
    private ObservableList<EntradaTabela> obsEntradaTabela;
    
    private List<EntradaTabela> listaDeEntradaPilha = new ArrayList<>();
    private ObservableList<EntradaTabela> obsEntradaPilha;
    
    public static final String SingleTon = "Esse eu vejo fora";
    
    private SecondaryController(){    
    }
    
    public static SecondaryController getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SecondaryController();
        }
        
        return INSTANCE;
    }
    
    @Override
    public void initialize (URL location, ResourceBundle resources) {
    }
    
    @FXML
    private TableView< EntradaTabela > tbMemoriaGeral;

    @FXML
    private TableColumn<EntradaTabela, Integer> tbEnderecoMemoriaUm;

    @FXML
    private TableColumn<EntradaTabela, Short> tbDadoMemoriaUm;

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
    private TableView<EntradaTabela> tbPilhaGeral;

    @FXML
    private TableColumn<EntradaTabela, Integer> tbOrdemPilha;

    @FXML
    private TableColumn<EntradaTabela, Short> tbDadosPilha;

    @FXML
    void setACC(VM vm) {
        regACC.setText(String.valueOf(vm.regs.acc));
    }   

    @FXML
    void setMOP(VM vm) {
        regMOP.setText(String.valueOf(vm.regs.re_mode));
    }

    @FXML
    void setPC(VM vm) {
        regPC.setText(String.valueOf(vm.regs.pc));
    }

    @FXML
    void setRE(VM vm) {
        regRE.setText(String.valueOf(vm.regs.re));
    }

    @FXML
    void setRI(VM vm) {
        regRI.setText(String.valueOf(vm.regs.ri));
    }

    @FXML
    void setSP(VM vm) {
        regSP.setText(String.valueOf(vm.regs.sp));
    }
    
     @FXML
    public void inicializaNaTabelaMem(VM memoria) {
         System.out.println("Entrou");
        this.setPC(memoria);
        this.setSP(memoria);
        this.setACC(memoria);
        this.setRE(memoria);
        this.setRI(memoria);
        this.setMOP(memoria);
        
        tbEnderecoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tbDadoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("memory"));
        
        tbMemoriaGeral.setItems(listaDeTabela(memoria));
    }

    private ObservableList<EntradaTabela> listaDeTabela(VM memoria){
        listaDeEntradaTabela.clear();
        listaDeEntradaPilha.clear();
          for(int i=0; i<memoria.memory.length;i++){
            EntradaTabela entrada = new EntradaTabela( i, memoria.memory[i]);
            listaDeEntradaTabela.add(entrada);
            if(i>2 && i<=(memoria.memory[2]+2)){
                enchePilha(entrada);
            }
            
        }
        return FXCollections.observableArrayList(listaDeEntradaTabela);
    }
    
    private void enchePilha(EntradaTabela entrada){
        tbOrdemPilha.setCellValueFactory(new PropertyValueFactory<>("enderecoP"));
        tbDadosPilha.setCellValueFactory(new PropertyValueFactory<>("memory"));
        listaDeEntradaPilha.add(entrada);
        obsEntradaPilha = FXCollections.observableArrayList(listaDeEntradaPilha);
        tbPilhaGeral.setItems(obsEntradaPilha);
        
    }
    
    public List<EntradaTabela> getListaDeEntradaTabela() {
        return listaDeEntradaTabela;
    }

    public TableView<EntradaTabela> getTbMemoriaGeral() {
        return tbMemoriaGeral;
    }
    
    
}