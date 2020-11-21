package com.ps.executor;

import com.ps.executor.InstructionAction;

public enum Instruction
{
    // Manter opcode (primeiro número) em ordem ao atualizar, senão withOpcode vai ficar errado.
    BR     (0,  2 ,1, InstructionAction.BR     , AddressingMode.except_immediate, AddressingMode.none),
    BRPOS  (1,  2, 1, InstructionAction.BRPOS  , AddressingMode.except_immediate, AddressingMode.none),
    ADD    (2,  2, 1, InstructionAction.ADD    , AddressingMode.all,              AddressingMode.none),
    LOAD   (3,  2, 1, InstructionAction.LOAD   , AddressingMode.all,              AddressingMode.none),
    BRZERO (4,  2, 1, InstructionAction.BRZERO , AddressingMode.except_immediate, AddressingMode.none),
    BRNEG  (5,  2, 1, InstructionAction.BRNEG  , AddressingMode.except_immediate, AddressingMode.none),
    SUB    (6,  2, 1, InstructionAction.SUB    , AddressingMode.all,              AddressingMode.none),
    STORE  (7,  2, 1, InstructionAction.STORE  , AddressingMode.except_immediate, AddressingMode.none),
    WRITE  (8,  2, 1, InstructionAction.WRITE  , AddressingMode.all,              AddressingMode.none),
    RET    (9,  1, 0, InstructionAction.RET    , AddressingMode.none,             AddressingMode.none),
    DIVIDE (10, 2, 1, InstructionAction.DIVIDE , AddressingMode.all,              AddressingMode.none),
    STOP   (11, 1, 0, InstructionAction.STOP   , AddressingMode.none,             AddressingMode.none),
    READ   (12, 2, 1, InstructionAction.READ   , AddressingMode.except_immediate, AddressingMode.none),
    COPY   (13, 3, 2, InstructionAction.COPY   , AddressingMode.except_immediate, AddressingMode.all),
    MULT   (14, 2, 1, InstructionAction.MULT   , AddressingMode.all,              AddressingMode.none),
    CALL   (15, 2, 1, InstructionAction.CALL   , AddressingMode.except_immediate, AddressingMode.none);
    public static final Instruction[] withOpcode = Instruction.values();
    
    public enum AddressingMode {
        Direct, Indirect, Immediate;

        public static final AddressingMode[] all = {Direct, Indirect, Immediate};
        public static final AddressingMode[] except_immediate = {Direct, Indirect};
        public static final AddressingMode[] none = {};
    }

    public final int opcode;
    public final int size; // em palavras da máquina (16 bits)
    public final int operands;
    public final InstructionAction.Interface action;
    public final AddressingMode[] opd1_addressing_modes;
    public final AddressingMode[] opd2_addressing_modes;
    
    private Instruction(
        int opcode,
        int size,
        int operands,
        InstructionAction.Interface action,
        AddressingMode[] opd1_addressing_modes,
        AddressingMode[] opd2_addressing_modes)
    {
        this.opcode = opcode;
        this.size = size;
        this.operands = operands;
        this.action = action;
        this.opd1_addressing_modes = opd1_addressing_modes;
        this.opd2_addressing_modes = opd2_addressing_modes;
    }

    public boolean isBR()     {return opcode == BR.opcode;}
    public boolean isBRPOS()  {return opcode == BRPOS.opcode;}
    public boolean isADD()    {return opcode == ADD.opcode;}
    public boolean isLOAD()   {return opcode == LOAD.opcode;}
    public boolean isBRZERO() {return opcode == BRZERO.opcode;}
    public boolean isBRNEG()  {return opcode == BRNEG.opcode;}
    public boolean isSUB()    {return opcode == SUB.opcode;}
    public boolean isSTORE()  {return opcode == STORE.opcode;}
    public boolean isWRITE()  {return opcode == WRITE.opcode;}
    public boolean isRET()    {return opcode == RET.opcode;}
    public boolean isDIVIDE() {return opcode == DIVIDE.opcode;}
    public boolean isSTOP()   {return opcode == STOP.opcode;}
    public boolean isREAD()   {return opcode == READ.opcode;}
    public boolean isCOPY()   {return opcode == COPY.opcode;}
    public boolean isMULT()   {return opcode == MULT.opcode;}
    public boolean isCALL()   {return opcode == CALL.opcode;}

    public boolean allowsOpd1AddressingMode(AddressingMode mode)
    {
        for(AddressingMode addressing_mode : opd1_addressing_modes)
        {
            if(addressing_mode == mode) {return true;}
        }
        return false;
    }

    public boolean allowsOpd2AddressingMode(AddressingMode mode)
    {
        for(AddressingMode addressing_mode : opd2_addressing_modes)
        {
            if(addressing_mode == mode) {return true;}
        }
        return false;
    }
}
