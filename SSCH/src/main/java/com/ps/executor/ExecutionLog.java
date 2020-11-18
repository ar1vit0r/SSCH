package com.ps.executor;

public class ExecutionLog
{
    public class MemoryUpdate
    {
        short index;
        short new_value;
    }
    public class OpdInfo
    {
        short addressing_mode;
        short value;
    }

    public Instruction instruction;
    // Campos seguintes podem ser nulos.
    public MemoryUpdate memory_update = null;
    public OpdInfo opd1 = null;
    public OpdInfo opd2 = null;

    public ExecutionLog(Instruction instruction) {this.instruction = instruction;}
}
