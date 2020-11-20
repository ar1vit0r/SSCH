package com.ps.montador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.HashMap;


public class Montador {
         
    public static short[] main(String diretorio) throws FileNotFoundException{
        //ArrayList para armazenamento do conteudo do arquivo
        List<String> data = new ArrayList<>();
        //HashMap criado para tabela de Símbolos, linkando o nome da label com o endereço
        //Exemplo: (FIM, 23)
        Map<String, Integer> tabelaDeSimbolos = new HashMap<String, Integer>();
        
        //Tratamento de exceção para leitura do arquivo e teste para verificar se o arquivo está vazio.
            File textfile = new File(diretorio);
            Scanner reader = new Scanner(textfile);
            while(reader.hasNextLine()){
                String[] aux = reader.nextLine().split(" ");
                // PQ ESSE IF???
                if(aux[0].equals("const")){
                    aux = new String[]{aux[1]};
                }
                data.addAll(Arrays.asList(aux));
            }
            
        //Tratamento das LABEL
        //Procura no código as Label
        //Quando as encontra, coloca na tabela de Simbolos com a posição
        //E não passa a string para a Próxima List aux_data
        List<String> aux_data = new ArrayList<>();
        int index_label = 0;
        for (String iterator : data){
            var doisPontos = iterator.charAt(iterator.length()-1);
            if (doisPontos == ':') {
                iterator = iterator.replace(":", "");
                iterator = iterator.toUpperCase();
                tabelaDeSimbolos.put(iterator, index_label);
                System.out.println("Key: " + iterator);
                System.out.println("Result: " + tabelaDeSimbolos.get(iterator));
            }
            else {
                aux_data.add(iterator);
            }
            ++index_label;
        }
        
        
        //Vetor onde as operações são convertidas com seus respectivos numero
        short[] bin = new short[aux_data.size()];  
        
        //Envio do arquivo para um vetor auxiliar
        String[] code = new String[aux_data.size()];
        code = aux_data.toArray(code);
        
        //Leitura do vetor auxiliar e armazenamento do opcode de cada operação
        for (int i = 0; i < code.length; i++) {  
            code[i] = code[i].toUpperCase();
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
                case "LOAD":
                    bin[i] = 3;
                    break;
                case "MULT":
                    bin[i] = 14;
                    break;
                case "READ":
                    bin[i] = 12;
                    break;
                case "RET":
                    bin[i] = 9;
                    break;
                case "STOP":
                    bin[i] = 11;
                    break;
                case "STORE":
                    bin[i] = 7;
                    break;
                case "SUB":
                    bin[i] = 6;
                    break;
                case "WRITE":
                    bin[i] = 8;
                    break;
                case "CONST":
                    break;
                case "SPACE":
                    break;
                default:
                    String aux = code[i];
                    
                    //VE SE É UMA LABEL
                    if (tabelaDeSimbolos.containsKey(aux)){
                        int aux_tabela = tabelaDeSimbolos.get(aux);
                        bin[i] = (short) aux_tabela;
                    }
                    
                    //SE NÃO FOR UMA LABE, VERIFICA SE É IMEDIATO OU DIRETO
                    else if(aux.startsWith("@")){
                        if((i-2)>=0) {
                            if(code[i-2].equals("copy")){
                                bin[i-2] = (short)(bin[i-2] + 64);
                            } else bin[i-1] = (short)(bin[i-1] + 64);                
                        } else bin[i-1] = (short)(bin[i-1] + 64);                
                        aux = aux.replace("@", "");
                        bin[i] = (short) Integer.parseInt(aux);
                    } 
                    
                    else {
                        bin[i] = (short) Integer.parseInt(code[i]);
                    }
                break;
            }
        }
        
        //IMEDIATO OU DIRETO
        //0000000001000001
        //Retorno do vetor com os opcodes do arquivo de operações
        return bin; 
    }    
}
