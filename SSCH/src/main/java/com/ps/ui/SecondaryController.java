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
    
    
    private static SecondaryController controller = new SecondaryController();
    private VM memoria = VM.getInstance();
    private List<EntradaTabela> listaDeEntradaTabela = new ArrayList<>();
    private ObservableList<EntradaTabela> obsEntradaTabela;
    
    private List<EntradaTabela> listaDeEntradaPilha = new ArrayList<>();
    private ObservableList<EntradaTabela> obsEntradaPilha;
    private short b = 0;
    
    public SecondaryController(){
    
    }
    
    @Override
    public void initialize (URL location, ResourceBundle resources) {
       // memoria.memory = new short[] {1,1,1,2,1,(short)Instruction.STOP.opcode};
        
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
        if(vm.regs.ri > 63){
            b = 64;
        }else{ b = 0;}
        switch (vm.regs.ri - b) {
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
                    regRI.setText(" ");
                    break;
        }
    }

    @FXML
    void setSP(VM vm) {
        regSP.setText(String.valueOf(vm.regs.sp));
    }
    
     @FXML
    public void inicializaNaTabelaMem() {
         System.out.println("Entrou");
        this.setPC(memoria);
        this.setSP(memoria);
        this.setACC(memoria);
        this.setRE(memoria);
        this.setRI(memoria);
        this.setMOP(memoria);
        
        tbEnderecoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tbDadoMemoriaUm.setCellValueFactory(new PropertyValueFactory<>("memory"));
        
        tbMemoriaGeral.setItems(listaDeTabela());
    }

    private ObservableList<EntradaTabela> listaDeTabela(){
        listaDeEntradaTabela.clear();
        listaDeEntradaPilha.clear();
          for(int i=0; i<memoria.memory.length;i++){
            EntradaTabela entrada = new EntradaTabela( i, memoria.memory[i]);
            listaDeEntradaTabela.add(entrada);
            if(i>2 && i<(memoria.memory[memoria.stack_base])){
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
    
    public static synchronized SecondaryController getInstance(){
        return controller;
    }
}