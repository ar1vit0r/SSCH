package com.ps.executor;

public class Registers
{
    // Mantém o endereço da próxima instrução a ser executada.
    public short pc;
    // Aponta para o topo da pilha do sistema.
    public short sp;
    // Armazena os dados (carregados e resultantes) das operações da Unid. de Lógica e Aritmética
    public short acc;

    // Mantém o opcode da instrução em execução.
    public short ri;

    // Mantém o endereço de acesso à memória de dados.
    public short re;
    // Mantém o modo de accesso ao re.
    //     0 => Direto
    //     1 => Indireto
    //     2 => Imediato.
    public short re_mode;
}
