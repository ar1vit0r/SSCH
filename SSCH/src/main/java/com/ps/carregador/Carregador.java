/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.carregador;

import com.ps.executor.VM;
import com.ps.executor.Instruction;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 *
 * @author gabriel
 */
public class Carregador {
    private VM vm = VM.getInstance();
    
    
    public static void carregaMem(String objeto, int memSize) throws FileNotFoundException{
        short[] mem = new short[memSize];
        File textfile = new File(objeto);
        Scanner reader = new Scanner(textfile);
        //Lendo arquivo
        String[] file = new String[4];
        int i = 0;
        for(i=0;i<4;i++){
            file[i] = reader.nextLine();
        }
        
        String[] temp = file[1].split(" ");
        List<Short> dontTouch = new ArrayList<Short>();
        for(i = 0; i<temp.length; i++){
            dontTouch.add(Short.parseShort(temp[i]));
        }
        //Inicializando vetores do mapa de relocação e código
        temp = file[3].split(" ");
        short[] code = new short[temp.length];
        for(i = 0; i<temp.length; i++){
            Short temp2 = new Short(temp[i]);
            code[i] = temp2;
        }
        //Startando o tamanho da pilha e o offset
        Short stackSize = new Short(file[0]);
        Short offSet = new Short(file[2]);
        
        //Começa com o opcode para "BR"
        mem[0] = (short) Instruction.BR.opcode;
        //mem[1]  possui o tamanho da pilha + 3 para pular direto para as instruções
        mem[1] = (short) (offSet);
        //mem[2] possui o tamanho da pilha 
        mem[2] = (short) (stackSize);
        
        for(i=0;i<code.length;i++){
            if(!dontTouch.contains((short) i)){
                mem[i + offSet] = (short) (code[i] + offSet);
            } else{
                mem[i + offSet] = code[i];
            }
        }
      
        //vm.regs.acc = 0;// Quando é resetado o valor de um programa o ACC é zerado para a execução do proximo programa
        //vm.memory = mem;// Carrega na memoria o programa
        System.out.println(Arrays.toString(mem));
    }
    
}
