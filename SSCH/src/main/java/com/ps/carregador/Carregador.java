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

/**
 *
 * @author gabriel
 */
public class Carregador {

    private VM vm = VM.getInstance();
    
    
    public short[] carregaMem( short[][] montador, int stackSize, int memSize ){
        short[] instructions = montador[0];
        short[] notTouch = montador[1];
        short[] mem = new short[memSize];
        //Começa com o opcode para "BR"
        mem[0] = (short) Instruction.BR.opcode;
        //mem[1]  possui o tamanho da pilha + 3 para pular direto para as instruções
        mem[1] = (short) (stackSize + 3);
        //mem[2] possui o tamanho da pilha 
        mem[2] = (short) (stackSize);
        List<Short> dontTouch = new ArrayList<>();
        int j;
        for(j=0;j<notTouch.length;j++){
            dontTouch.add( notTouch[j]);
        }
        int Opd = 0;
        for(int i = 0; i < instructions.length; i++){
            
            if(Opd != 0){
                //Se estamos em algum opd que não seja imediato soma o offset
                if(!dontTouch.contains((short)(i))){
                    mem[i + stackSize + 3] = (short) (instructions[i] + stackSize + 3);
                    Opd--;
                } else {
                    mem[i + stackSize + 3] = instructions[i];
                }
            } else {
                if(isImediate(instructions[i])){
                    if(!dontTouch.contains((short)(i))){
                        //Se a instrução for imediata então pula os opd para proxima instrução
                        if(Instruction.withOpcode[instructions[i] & 0b1111].operands == 2){
                            mem[i + stackSize + 3] = instructions[i];
                            //Se a instrução for copy e imediata, o opd1 é somado o offset e o opd2 fica imediato, dps pula para a proxima instrução
                            mem[i + stackSize + 4] = (short) (instructions[i+1] + stackSize + 3);
                            mem[i + stackSize + 5] = instructions[i+2];
                        } else {
                            //Se a instrução for de 1 opd, então coloca na mem sem alterar e depois pula para proxima instrução
                            mem[i + stackSize + 3] = instructions[i];
                            mem[i + stackSize + 4] = instructions[i+1];
                        }
                        i += Instruction.withOpcode[instructions[i] & 0b1111].operands; 
                    } else {
                        mem[i + stackSize + 3] = instructions[i];
                    }
                    
                } else {
                    if(!dontTouch.contains((short)(i))){
                        //Da um set se é um opd, e coloca a instrução na memoria
                        Opd =  Instruction.withOpcode[instructions[i] & 0b1111].operands; 
                        mem[i + stackSize + 3] = instructions[i];
                    } else {
                        mem[i + stackSize + 3] = instructions[i];
                    }
                }
            }
        }
        
        
        vm.regs.acc = 0;// Quando é resetado o valor de um programa o ACC é zerado para a execução do proximo programa
        vm.memory = mem;// Carrega na memoria o programa
        return mem;
    }
    
    public static boolean isImediate ( short instruction ){
        int bit7 = (instruction & 0b0100_0000) >>> 6;
        return bit7 == 1;
    }
    
    
}
