package com.ps.executor;

import java.io.InputStream;
import java.io.OutputStream;

// Pequeno wrapper em volta de Instruction para
// manter o estado encapsulado.
public class VM
{
    private static final VM vm = new VM();
    public Registers regs = new Registers();
    public short[] memory;
    
    private VM(){
    
    }
    // Endereço de memória que contém o tamanho maximo da stack.
    // A stack de fato está diretamente após esse indereço.
    public static final int stack_base = 2; 
    
    public ExecutionLog step()
    {
        return step(System.in, System.out);
    }

    public ExecutionLog step(InputStream in, OutputStream out)
    {
        regs.ri = memory[regs.pc];

        return Instruction.withOpcode[regs.ri & 0b1111].action.run(
            regs, memory, stack_base, in, out
        );
    }
    
    public static synchronized VM getInstance() {
        return vm;
    } 
}
