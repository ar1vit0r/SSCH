package com.ps.executor;

import com.ps.executor.PseudoInstructions;
import com.ps.executor.Registers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Scanner;

public class InstructionAction
{
    public interface Interface
    {
        public ExecutionLog run(Registers r, short[] m, int s, InputStream i, OutputStream o);
    }

    // ACC <- ACC + opd1
    public static Interface ADD = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);

            regs.acc += regs.re;
            regs.pc += 2;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // PC <- opd1
    public static Interface BR = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            assert regs.re_mode != 2:
                "Endereçamento Imediato não é permitido para instruções da familia BR";
            PseudoInstructions.memfetch(regs, memory);

            regs.pc = regs.re;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // PC <- opd1, se ACC < 0
    public static Interface BRNEG = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            if (regs.acc < 0) { return BR.run(regs, memory, _0, _1, _2); }

            regs.pc += 2;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // PC <- opd1, se ACC > 0
    public static Interface BRPOS = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            if (regs.acc > 0) { return BR.run(regs, memory, _0, _1, _2); }

            regs.pc += 2;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // PC <- opd1, se ACC == 0
    public static Interface BRZERO = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            if (regs.acc == 0) { return BR.run(regs, memory, _0, _1, _2); }

            regs.pc += 2;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // [SP] <- PC; PC <- opd1
    // TODO: arrumar stack overflow quando é chamado CALL em uma
    //       stack vazia de tamanho 1 (não deveria ocorrer overflow).
    public static Interface CALL = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int stack_base, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            assert regs.re_mode != 2:
                "Endereçamento Imediato não permitido para a intrução CALL";
            PseudoInstructions.memfetch(regs, memory);

            memory[regs.sp + stack_base + 1] = (short) (regs.pc + 2);
            regs.sp = (short) ((regs.sp + 1) % memory[stack_base]);
            assert regs.sp != 0: "STACK OVERFLOW";
            regs.pc = regs.re;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // opd1 <- opd2
    public static Interface COPY = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int stack_base, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory, PseudoInstructions.Opd.opd2);
            PseudoInstructions.memfetch(regs, memory, PseudoInstructions.Opd.opd2);
            short opd2 = regs.re;
    
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);
            memory[regs.re] = opd2;
    
            regs.pc += 3;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // ACC <- ACC / opd1
    public static Interface DIVIDE = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int stack_base, InputStream _0, OutputStream _1)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);
    
            regs.acc /= regs.re;
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // ACC <- opd1
    public static Interface LOAD = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);

            regs.acc = regs.re;
            regs.pc += 2;

            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // ACC <- ACC * opd1
    public static Interface MULT = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);
    
            regs.acc *= regs.re;
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // opd1 <- input_stream
    public static Interface READ = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream input, OutputStream _1)
        {
            PseudoInstructions.set_re(regs, memory);
            assert regs.re_mode != 2:
                "Endereçamento Imediato não permitido para a intrução READ";
            PseudoInstructions.memfetch(regs, memory);
    
            System.err.print(regs.re + "> ");
            Scanner scanner = new Scanner(input).useDelimiter("\n");
            memory[regs.re] = scanner.nextShort();
            scanner.close();
    
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // PC <- [SP]
    public static Interface RET = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int stack_base, InputStream _0, OutputStream _1)
        {
            assert regs.sp != 0: "RET chamado com stack vazia";
    
            regs.sp -= 1;
            regs.pc = memory[regs.sp + stack_base + 1];
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // término (fim) de execução
    public static Interface STOP = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // opd1 <- acc
    public static Interface STORE = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            assert regs.re_mode != 2:
                "Endereçamento Imediato não permitido para a intrução STORE";
            PseudoInstructions.memfetch(regs, memory);
    
            memory[regs.re] = regs.acc;
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // ACC <- ACC - opd1
    public static Interface SUB = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream _2)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);
    
            regs.acc -= regs.re;
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };

    // output_stream <- opd1
    public static Interface WRITE = new Interface()
    {
        public ExecutionLog run(Registers regs, short[] memory, int _0, InputStream _1, OutputStream output)
        {
            PseudoInstructions.set_re(regs, memory);
            PseudoInstructions.memfetch(regs, memory);
    
            try {output.write( Short.toString(regs.re).getBytes() );output.write('\n');}
            catch(IOException e) {/*ignore*/};
    
            regs.pc += 2;
    
            return PseudoInstructions.generateLog(regs, memory);
        }
    };
}
