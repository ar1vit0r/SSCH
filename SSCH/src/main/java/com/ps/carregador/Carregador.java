/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.carregador;

import com.ps.executor.VM;

/**
 *
 * @author gabriel
 */
public class Carregador {

    private VM vm = VM.getInstance();
    
    
    public void carregaMem( short[] instructions, int stackSize, int memSize ){
        short[] mem = new short[memSize];
        //Começa iniciando os valores da mem
        mem[0] = 0;
        //mem[1] se torna a op "br"
        mem[1] = 0;
        //mem[2] possui o tamanho da pilha + 3 para pular direto para as instruções
        mem[2] = (short) (stackSize + 3);
        int Opd = 0;
        for(int i = 0; i < instructions.length; i++){
            if(Opd != 0){
                //Se estamos em algum opd que não seja imediato soma o offset
                mem[i + stackSize + 3] = (short) (instructions[i] + stackSize + 3);
                Opd--;
            } else {
                if(isImediate(instructions[i])){
                    //Se a instrução for imediata então pula os opd para proxima instrução
                    if(howManyOpd(instructions[i]) == 2){
                        mem[i + stackSize + 3] = instructions[i];
                        //Se a instrução for copy e imediata, o opd1 é somado o offset e o opd2 fica imediato, dps pula para a proxima instrução
                        mem[i + stackSize + 4] = (short) (instructions[i+1] + stackSize + 3);
                        mem[i + stackSize + 5] = instructions[i+2];
                    } else {
                        //Se a instrução for de 1 opd, então coloca na mem sem alterar e depois pula para proxima instrução
                        mem[i + stackSize + 3] = instructions[i];
                        mem[i + stackSize + 4] = instructions[i+1];
                    }
                    i += howManyOpd(instructions[i]);
                    
                } else {
                    //Da um set se é um opd, e coloca a instrução na memoria
                    Opd = howManyOpd(instructions[i]);
                    mem[i + stackSize + 3] = instructions[i];
                }
            }
        }
        
        vm.regs.pc = 1;
        vm.memory = mem;
        
    }
    
    public static boolean isImediate ( short instruction ){
        int bit7 = (instruction & 0b0100_0000) >>> 6;
        return bit7 == 1;
    }
    
    public static short howManyOpd ( short instruction ){
        if( instruction == 13 || instruction == 77) return 2;
        if( instruction == 9 || instruction == 11 ) return 0;
        return 1;
    }
    
    
    
}
