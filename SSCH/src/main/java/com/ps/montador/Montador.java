package com.ps.montador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author josefm, Ariam
 */

public class Montador {
         
    public static short[] main(String diretorio) throws FileNotFoundException{
        //ArrayList para armazenamento do conteudo do arquivo
        List<String> data = new ArrayList<>();
        //Tratamento de exceção para leitura do arquivo e teste para verificar se o arquivo está vazio.
            
            File textfile = new File(diretorio);
            Scanner reader = new Scanner(textfile);
            while(reader.hasNextLine()){
                String[] aux = reader.nextLine().split(" ");
                if(aux[0].equals("const")){
                    aux = new String[]{aux[1]};
                }
                data.addAll(Arrays.asList(aux));
            }
        //Envio do arquivo para um vetor auxiliar
        String[] code = new String[data.size()];
        code = data.toArray(code);
        //Vetor onde as operações são convertidas com seus respectivos numero
        short[] bin = new short[code.length];   
        //Leitura do vetor auxiliar e armazenamento do opcode de cada operação
        for (int i = 0; i < code.length; i++) {  
            switch (code[i]) {
                case "add":
                case "ADD":
                    bin[i] = 2;
                    break;
                case "br":
                case "BR":
                    bin[i] = 0;
                    break;
                case "brneg":
                case "BRNEG":
                    bin[i] = 5;
                    break;
                case "brpos":
                case "BRPOS":
                    bin[i] = 1;
                    break;
                case "brzero":
                case "BRZERO":
                    bin[i] = 4;
                    break;
                case "call":
                case "CALL":
                    bin[i] = 15;
                    break;
                case "copy":
                case "COPY":
                    bin[i] = 13;
                    break;
                case "divide":
                case "DIVIDE":
                    bin[i] = 10;
                    break;
                case "load":
                case "LOAD":
                    bin[i] = 3;
                    break;
                case "mult":
                case "MULT":
                    bin[i] = 14;
                    break;
                case "read":
                case "READ":
                    bin[i] = 12;
                    break;
                case "ret":
                case "RET":
                    bin[i] = 9;
                    break;
                case "stop":
                case "STOP":
                    bin[i] = 11;
                    break;
                case "store":
                case "STORE":
                    bin[i] = 7;
                    break;
                case "sub":
                case "SUB":
                    bin[i] = 6;
                    break;
                case "write":
                case "WRITE":
                    bin[i] = 8;
                    break;
                case "const":
                case "CONST":
                    break;
                case "space":
                case "SPACE":
                    break;
                default:
                    String aux = code[i];
                    if(aux.startsWith("@")){
                        if((i-2)>=0) {
                            if(code[i-2].equals("copy")){
                                bin[i-2] = (short)(bin[i-2] + 64);
                            } else bin[i-1] = (short)(bin[i-1] + 64);                
                        } else bin[i-1] = (short)(bin[i-1] + 64);                
                        aux = aux.replace("@", "");
                        bin[i] = (short) Integer.parseInt(aux);
                    } else {
                        bin[i] = (short) Integer.parseInt(code[i]);
                 }
                
                break;
            }
        }
        //Retorno do vetor com os opcodes do arquivo de operações
        return bin;
    }    
}
