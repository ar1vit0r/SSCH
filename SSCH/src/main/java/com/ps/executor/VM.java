package com.ps.executor;

import java.io.InputStream;
import java.io.OutputStream;

import com.ps.executor.Instruction;
import com.ps.executor.Registers;

// Pequeno wrapper em volta de Instruction para
// manter o estado encapsulado.
public class VM
{
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

    public short[] memory;
    public Registers regs = new Registers();
}
