package com.ps.executor;

import com.ps.executor.Instruction;
import com.ps.executor.ExecutionLog;
import com.ps.executor.Registers;

/// Pseudo instruções para facilitar a vida do implementador :).
/// Veja Registers.re e Registers.re_mode.
public class PseudoInstructions
{
    public enum Opd {opd1, opd2;};
    // Popula Registers.re e Registers.re_mode com os valores
    // certos para o operando.
    public static void set_re(Registers regs, short[] memory)
    {
        set_re(regs, memory, Opd.opd1);
    }
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

            assert regs.re < memory.length:
                "Tentativa de acessar endereço fora da memória com endereçamento Indireto";
            regs.re = memory[regs.re];
        }
        else
        {
            assert regs.re < memory.length:
                "Tentativa de acessar endereço fora da memória com endereçamento Direto ou Indireto";
            regs.re = memory[regs.re];
            if (regs.re_mode == 0) {return;}

            assert regs.re < memory.length:
                "Tentativa de acessar endereço fora da memória com endereçamento Indireto";
            regs.re = memory[regs.re];
        }
    }

    public static ExecutionLog generateLog(Registers regs, short[] memory) {
        ExecutionLog a = new ExecutionLog(Instruction.withOpcode[regs.ri & 0b1111]);
        // TODO: finish
        return a;
    }
}
