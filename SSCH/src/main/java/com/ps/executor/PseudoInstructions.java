package com.ps.executor;

import com.ps.executor.Instruction;
import com.ps.executor.ExecutionLog;
import com.ps.executor.Registers;

/// Pseudo instruções para facilitar a vida do implementador :).
/// Veja Registers.re e Registers.re_mode.
public class PseudoInstructions
{
    public enum Opd {opd1, opd2;};

    public static void set_re(Registers regs, short[] memory)
    {
        set_re(regs, memory, Opd.opd1);
    }
    // Popula Registers.re e Registers.re_mode com os valores
    // certos para o operando.
    public static void set_re(Registers regs, short[] memory, Opd opd)
    {
        regs.re = memory[regs.pc + (opd == Opd.opd2 ? 1 : 0) + 1]; // bool em java não é implicitamente int.

        int bit5 = (regs.ri & 0b0001_0000) >>> 4;
        int bit6 = (regs.ri & 0b0010_0000) >>> 5;
        int bit7 = (regs.ri & 0b0100_0000) >>> 6;

        // Se for instrução copy tem que ter tratamento especial:
        //     Bit 5 => opd1 indireto.
        //     Bit 6 => opd2 indireto.
        //     Bit 7 => opd2 imediato.
        if(Instruction.withOpcode[regs.ri & 0b1111].isCOPY())
        {
            assert bit6 + bit7 != 2:
                "COPY não pode ter 2 modos de endereçamento para o operando 2";

            if (opd == Opd.opd1) {
                regs.re_mode = (short) bit5;
            } else {
                regs.re_mode = (short) (bit6 + bit7 * 2);
            }
        }
        // Mas se for qualquer outra instrução o tratamento é uniforme:
        //     Bit 5 => opd1 indireto.
        //     Bit 6 => ignorado.
        //     Bit 7 => opd1 imediato.
        else
        {
            assert bit5 + bit7 != 2:
                "Instrução não pode ter 2 modos de endereçamento para o operando";

            regs.re_mode = (short) (bit5 + bit7 * 2);
        }
    }

    public static void memfetch(Registers regs, short[] memory) {
        memfetch(regs, memory, Opd.opd1);
    }
    // Pega algo da memória obedecendo o modo de endereçamento e bota no Registers.re.
    // O valor retornado é não especificado (pode retornar qualquer coisa) quando o endereço é fora da memória.
    public static void memfetch(Registers regs, short[] memory, Opd opd)
    {
        if (regs.re_mode == 2) {return;}
        
        Instruction ins = Instruction.withOpcode[regs.ri & 0b1111];
        // Essas instruções tem tratamento especial pois endereçamento direto
        // vai significar "ir para lá" ou "guardar lá" e indireto
        // "ir para o endereço apontado lá" e "guardar no endereço apontado lá".
        if (ins.isSTORE() || ins.isBR() ||
            ins.isBRNEG() || ins.isBRPOS() ||
            ins.isBRZERO() || ins.isREAD() ||
            ins.isCALL() || 
            (ins.isCOPY() && opd == Opd.opd1))
        {
            if(regs.re_mode == 0) {return;}
            if(regs.re >= memory.length) {regs.re = (short) 0xCAFE;return;}
            
            regs.re = memory[regs.re];
        }
        else
        {
            if(regs.re >= memory.length) {regs.re = (short) 0xBEEF;return;}

            regs.re = memory[regs.re];

            if(regs.re_mode == 0) {return;}
            if(regs.re >= memory.length) {regs.re = (short) 0xF00D;return;}

            regs.re = memory[regs.re];
        }
    }

    // Essa função tem overhead não trivial, se tem certeza que o programa está correto e/ou
    // não se importa com erros (NÃO ESQUEÇA QUE ELES CAUSAM COMPORTAMENTO NÃO ESPECIFICADO) 
    // use a versão UNSAFE das instruções.
    // TODO: adicionar versões UNSAFE das instruções.
    public static ExecutionLog generateLog(Registers regs, short[] memory, int stack_base)
    {
        // Mantenha em mente que quando chegar aqui a instrução ja foi executada e regs.pc já aponta para a próxima instrucão.
        ExecutionLog log = new ExecutionLog(Instruction.withOpcode[regs.ri & 0b1111]);

        if(log.instruction.operands == 0)
        {
            log.errors |= ExecutionLog.Error.UnusedOpd1AddressingMode.v    * ( (regs.ri & 0b0101_0000) != 0 ? 1 : 0 );
            log.errors |= ExecutionLog.Error.MultipleOpd1AddressingModes.v * ( (regs.ri & 0b0101_0000) == 0b0101_0000 ? 1 : 0 );
            log.errors |= ExecutionLog.Error.UnusedOpd2AddressingMode.v    * ( (regs.ri & 0b0010_0000) != 0 ? 1 : 0 );
        }
        else if(log.instruction.operands == 1)
        {
            log.errors |= ExecutionLog.Error.UnusedOpd2AddressingMode.v    * ( (regs.ri & 0b0010_0000) != 0 ? 1 : 0 );
            log.errors |= ExecutionLog.Error.MultipleOpd1AddressingModes.v * ( (regs.ri & 0b0101_0000) == 0b0101_0000 ? 1 : 0 );

            int immediate_addressing = regs.ri & 0b0100_0000;
            int indirect_addressing = regs.ri & 0b0001_0000;

            log.errors |= ExecutionLog.Error.UnalowedOpd1AddressingMode.v * ( immediate_addressing == 0 
                ? 0 
                : log.instruction.allowsOpd1AddressingMode(Instruction.AddressingMode.Immediate) ? 0 : 1
            );
            log.errors |= ExecutionLog.Error.UnalowedOpd1AddressingMode.v * ( indirect_addressing == 0 
                ? 0 
                : log.instruction.allowsOpd1AddressingMode(Instruction.AddressingMode.Indirect) ? 0 : 1
            );

            log.opd1 = new ExecutionLog.OpdInfo();
            log.opd1.addressing_mode = (short) (regs.ri * 0b0101_0000);
            log.opd1.value = memory[regs.pc - log.instruction.size + 1];
        }
        else if(log.instruction.operands == 2)
        {
            log.errors |= ExecutionLog.Error.MultipleOpd2AddressingModes.v * ( (regs.ri & 0b0110_0000) == 0b0110_0000 ? 1 : 0 );

            int opd1_indirect_addressing  = regs.ri & 0b0001_0000;
            int opd2_indirect_addressing  = regs.ri & 0b0010_0000;
            int opd2_immediate_addressing = regs.ri & 0b0100_0000;

            log.errors |= ExecutionLog.Error.UnalowedOpd1AddressingMode.v * ( opd1_indirect_addressing == 0 
                ? 0 
                : log.instruction.allowsOpd1AddressingMode(Instruction.AddressingMode.Indirect) ? 0 : 1
            );
            log.errors |= ExecutionLog.Error.UnalowedOpd2AddressingMode.v * ( opd2_indirect_addressing == 0 
                ? 0 
                : log.instruction.allowsOpd2AddressingMode(Instruction.AddressingMode.Indirect) ? 0 : 1
            );
            log.errors |= ExecutionLog.Error.UnalowedOpd2AddressingMode.v * ( opd2_immediate_addressing == 0 
                ? 0 
                : log.instruction.allowsOpd2AddressingMode(Instruction.AddressingMode.Immediate) ? 0 : 1
            );

            log.opd1 = new ExecutionLog.OpdInfo();
            log.opd1.addressing_mode = (short) (regs.ri * 0b0001_0000);
            log.opd1.value = memory[regs.pc - log.instruction.size + 1];

            log.opd2 = new ExecutionLog.OpdInfo();
            log.opd2.addressing_mode = (short) (regs.ri * 0b0110_0000);
            log.opd2.value = memory[regs.pc - log.instruction.size + 2];
        }

        if (log.instruction.isCALL()) {
            // Se tu botou algo na stack e ela está no mínimo significa que
            // o stack pointer deu a volta por cima.
            log.errors |= ExecutionLog.Error.StackOverflow.v * ( regs.sp == 0 ? 1 : 0 );
            log.errors |= ExecutionLog.Error.DivisionByZero.v * ( memory[stack_base] == 0 ? 1 : 0 );

            log.memory_update = new ExecutionLog.MemoryUpdate();
            log.memory_update.index = (short) (regs.sp + stack_base);
            log.memory_update.new_value = memory[log.memory_update.index];
        }
        else if (log.instruction.isRET())
        {
            // Se tu tirou algo da stack e ela está no máximo significa que
            // o stack pointer deu a volta por baixo.
            log.errors |= ExecutionLog.Error.StackUnderflow.v * ( regs.sp == memory[stack_base] ? 1 : 0 );
            log.errors |= ExecutionLog.Error.DivisionByZero.v * ( memory[stack_base] == 0 ? 1 : 0);
            // RET não faz nenhuma atualização na memoria, só diminui o stack pointer para "esquecer"
            // que existia um valor na stack.
        }
        
        return log;
    }

    // Chamado específicamente por COPY, READ e STORE
    public static ExecutionLog generateLogWithMemoryUpdate(Registers regs, short[] memory, int stack_base, short mem_update_addr)
    {
        ExecutionLog log = generateLog(regs, memory, stack_base);

        log.memory_update = new ExecutionLog.MemoryUpdate();
        log.memory_update.index = mem_update_addr;
        log.memory_update.new_value = memory[mem_update_addr];

        return log;
    }
}
