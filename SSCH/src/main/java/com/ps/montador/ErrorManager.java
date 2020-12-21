/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.montador;

/**
 *
 * @author Ari Vitor
 */
public class ErrorManager {
    private int numErro;
    private int numLinha;
    
    public ErrorManager(int numErro, int numLinha){
        this.numErro = numErro;
        this.numLinha = numLinha;
    }
    
    public String errorWarning(){
        String erro;
        String descricao;
        switch(numErro) {
            case 0:
                erro = "Caractere inválido: ";
                descricao = "Unidade sintática não reconhecida (caractere inválido em algum elemento) ";
            break;
            case 1:
                erro = "Linha muito longa: ";
                descricao = "Não deve haver mais de 80 caracteres ";
            break;
            case 2:
                erro = "Dígito inválido: ";
                descricao = "Presença de um caractere não reconhecido como dígito para a base usada ";
            break;
            case 3:
                erro = "Espaço ou final de linha esperado: ";
                descricao = "Delimitador de final de linha (depois do último operando ou instrução) não reconhecido como válido ";
            break;
            case 4:
                erro = "Valor fora dos limites: ";
                descricao = "Constante muito longa para o tamanho de palavra (ou byte) do computador ";
            break;
            case 5:
                erro = "Erro de sintaxe: ";
                descricao = "Falta ou excesso de operandos em instruções, ou labels mal formados ";
            break;
            case 6:
                erro = "Símbolo redefinido: ";
                descricao = "Referência simbólica com definições múltiplas, ou seja, símbolo aparece como label em mais de uma linha de instrução ";
            break;
            case 7:
                erro = "Símbolo não definido: ";
                descricao = "Referência simbólica não definida ";
            break;
            case 8:
                erro = "Instrução inválida: ";
                descricao = "Mnemônico não corresponde a nenhuma instrução do sistema de programação ";
            break;
            case 9:
                erro = "Falta diretiva END: ";
                descricao = "Indicação da ausência de pseudo-instrução END ";
            break;
            default:
                erro = "Nenhum Erro encontrado. ";
                descricao = "";
        }
        return erro + descricao + "na linha " + String.valueOf(numLinha);
    }
}