package com.ps.montador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author josefm, Ariam
 */
public class Montador {
         
    public static short[] main(String diretorio, int stackSize) throws FileNotFoundException{
        //ArrayList para armazenamento do conteudo do arquivo
        List<String> data = new ArrayList<>();
        //Tratamento de exceção para leitura do arquivo e teste para verificar se o arquivo está vazio.
            
            File textfile = new File(diretorio);
            Scanner reader = new Scanner(textfile);
            while(reader.hasNextLine()){
                String[] aux = reader.nextLine().split(" ");
                if("const".equals(aux[0])){ // elimina palavra reservada const q n é convertida
                    aux = new String[]{"/p"+aux[1]}; //identificador para que um valor "imediato" não tenha offset sobre ele
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
                    bin[i] = 2;
                    break;
                case "br":
                    bin[i] = 0;
                    break;
                case "brneg":
                    bin[i] = 5;
                    break;
                case "brpos":
                    bin[i] = 1;
                    break;
                case "brzero":
                    bin[i] = 4;
                    break;
                case "call":
                    bin[i] = 15;
                    break;
                case "copy":
                    bin[i] = 13;
                    break;
                case "divide":
                    bin[i] = 10;
                    break;
                case "load":
                    bin[i] = 3;
                    break;
                case "mult":
                    bin[i] = 14;
                    break;
                case "read":
                    bin[i] = 12;
                    break;
                case "ret":
                    bin[i] = 16;
                    break;
                case "stop":
                    bin[i] = 11;
                    break;
                case "store":
                    bin[i] = 7;
                    break;
                case "sub":
                    bin[i] = 6;
                    break;
                case "write":
                    bin[i] = 8;
                    break;
                case "const":
                    break;
                case "space":
                    break;
                default:
                    if(code[i].startsWith("/p")){
                        String aux2;
                        aux2 = code[i];
                        aux2 = aux2.replace("/p", "");
                        bin[i] =  (short) Integer.parseInt(aux2);
                    }
                    else 
                        bin[i] =  (short) ((Integer.parseInt(code[i])) + stackSize + 2 );
                    break;
            }
        }
        //Retorno do vetor com os opcodes do arquivo de operações
        return bin;
    }
}
   
