package com.ps.executor;
public class ExecutionLog
{
    public static class MemoryUpdate
    {
        short index;
        short new_value;
    }
    public static class OpdInfo
    {
        short addressing_mode;
        short value;
    }
    //     Presença de qualquer um dos seguintes erros causa comportamento
    // não especificado na máquina.
    //     É recomendado parar a execução imediatamente para evitar corrompimento/invalidação 
    // da memória por leitura/escrita de valores errados e/ou escrita/sobrescrita em lugares
    // errados da memória.
    //     Como as versões UNSAFE das instruções não reportam um ExecutionLog, você não vai 
    // saber se isso está acontecendo.
    public enum Error
    {
        None(0),
        StackOverflow(1),  // Acontece quando é chamado CALL com uma stack cheia.
        StackUnderflow(2), // Acontece quando é chamado RET  com uma stack vazia.
        DivisionByZero(4), // Acontece em instruções DIVIDE quando o divisor é 0 e RET quando a stack tem tamanho 0.
        UnalowedOpd1AddressingMode(8),
        UnalowedOpd2AddressingMode(16),
        UnusedOpd1AddressingMode(32),
        UnusedOpd2AddressingMode(64),
        MultipleOpd1AddressingModes(128),
        MultipleOpd2AddressingModes(256),
        OutOfBoundsMemoryAccess(512);  //TODO: implementar checagem

        public final int v;
        private Error(int v) {this.v = v;}
    }

    public Instruction instruction;
    // Campos seguintes podem ser nulos dependendo da instrução executada.
    public MemoryUpdate memory_update = null;
    public OpdInfo opd1 = null;
    public OpdInfo opd2 = null;
    // Checar erros fazendo & com Error.v:
    //     if(log.errors & ExecutionLog.Error.StackOverflow.v) {/*deu stack overflow*/}
    public int errors = Error.None.v;

    public ExecutionLog(Instruction instruction) {this.instruction = instruction;}
}
