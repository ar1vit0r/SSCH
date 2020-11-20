package montador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.HashMap;


public class Montador {
         
    public static short[][] main(String diretorio, boolean flag, String texto) throws FileNotFoundException{
        List<Integer> dontTouch = new ArrayList<>();
        int pc = 0;
        //ArrayList para armazenamento do conteudo do arquivo
        List<String> data = new ArrayList<>();
        //HashMap criado para tabela de Símbolos, linkando o nome da label com o endereço
        //Exemplo: (FIM, 23)
        Map<String, Integer> tabelaDeSimbolos = new HashMap<String, Integer>();
        if(flag){
        //Tratamento de exceção para leitura do arquivo e teste para verificar se o arquivo está vazio.
            File textfile = new File(diretorio);
            Scanner reader = new Scanner(textfile);
            boolean wasConst = false;


            while(reader.hasNextLine()){
                String trololo = reader.nextLine();
                String[] fenriz = trololo.split("//");
                String[] aux = fenriz[0].split(" ");
                
                if(aux[0].equals("CONST") || aux[0].equals("const")){
                    wasConst = true;
                    aux = new String[]{aux[1]};
                    dontTouch.add(pc);
                    pc = pc + 1;
                } else if(aux.length>1){
                    if (aux[1].equals("CONST") || aux[1].equals("const")){
                        wasConst = true;
                        aux = new String[]{aux[0],aux[2]};
                        dontTouch.add(pc);
                        pc = pc + 1;
                    }
                }
                
                //Verifica se é uma linha vazia
                if(!aux[0].isEmpty()){
                    //System.out.println("SEX");
                    //System.out.println(pc);
                    boolean hasLabel = false;
                    for(int q = 0; q < aux.length; q++){
                        if(aux[q].contains(":")){
                            hasLabel = true;
                        }
                    }
                    if(hasLabel && !wasConst){
                        pc = pc + aux.length - 1;
                    } else if (!wasConst){
                        pc = pc + aux.length;
                    }
                    data.addAll(Arrays.asList(aux));
                }
            }
        } else {
            String[] aux = texto.split("\n");
            for(int i = 0; i < aux.length; i++) {
                String[] fenriz = aux[i].split("//");
                String[] aux2 = fenriz[0].split(" ");
                if(aux2[0].equals("const")){
                    aux2 = new String[]{aux2[1]};
                }
                data.addAll(Arrays.asList(aux2));
            }
        }

        //Tratamento das LABEL
        //Procura no código as Label
        //Quando as encontra, coloca na tabela de Simbolos com a posição
        //E não passa a string para a Próxima List aux_data
        
        List<String> aux_data = new ArrayList<>();
        int index_label = 0;
        for (String iterator : data){
            
            char doisPontos = iterator.charAt(iterator.length()-1);
            //Tratamento de LABELS
            if (doisPontos == ':') {
                iterator = iterator.replace(":", "");
                iterator = iterator.toUpperCase();
                tabelaDeSimbolos.put(iterator, index_label);
            }
            
            else {
                aux_data.add(iterator);
                ++index_label;
            }
            
        }
        
        
        //Vetor onde as operações são convertidas com seus respectivos numero
        short[] bin = new short[aux_data.size()];  
        
        //Envio do arquivo para um vetor auxiliar
        String[] code = new String[aux_data.size()];
        code = aux_data.toArray(code);
        
        //Leitura do vetor auxiliar e armazenamento do opcode de cada operação
        pc = 0;
        for (int i = 0; i < code.length; i++) {  
            code[i] = code[i].toUpperCase();
            switch (code[i]) {
                case "ADD":
                    pc ++;
                    bin[i] = 2;
                    break;
                case "BR":
                    pc ++;
                    bin[i] = 0;
                    break;
                case "BRNEG":
                    pc ++;
                    bin[i] = 5;
                    break;
                case "BRPOS":
                    pc ++;
                    bin[i] = 1;
                    break;
                case "BRZERO":
                    pc ++;
                    bin[i] = 4;
                    break;
                case "CALL":
                    pc ++;
                    bin[i] = 15;
                    break;
                case "COPY":
                    pc ++;
                    bin[i] = 13;
                    break;
                case "DIVIDE":
                    pc ++;
                    bin[i] = 10;
                    break;
                case "LOAD":
                    pc ++;
                    bin[i] = 3;
                    break;
                case "MULT":
                    pc ++;
                    bin[i] = 14;
                    break;
                case "READ":
                    pc ++;
                    bin[i] = 12;
                    break;
                case "RET":
                    pc ++;
                    bin[i] = 9;
                    break;
                case "STOP":
                    pc ++;
                    bin[i] = 11;
                    break;
                case "STORE":
                    pc ++;
                    bin[i] = 7;
                    break;
                case "SUB":
                    pc ++;
                    bin[i] = 6;
                    break;
                case "WRITE":
                    pc ++;
                    bin[i] = 8;
                    break;
                case "CONST":
                    break;
                case "SPACE":
                    dontTouch.add(pc);
                    pc ++;
                    break;
                default:
                    String aux = code[i];
                    
                    //VE SE É UMA LABEL
                    if (tabelaDeSimbolos.containsKey(aux)){
                        pc ++;
                        int aux_tabela = tabelaDeSimbolos.get(aux);
                        bin[i] = (short) aux_tabela;
                    }
                    
                    //SE NÃO FOR UMA LABE, VERIFICA SE É IMEDIATO OU DIRETO
                    else if(aux.startsWith("@")){
                        pc ++;
                        if((i-2)>=0) {
                            if(code[i-2].equals("COPY")){
                                bin[i-2] = (short)(bin[i-2] + 64);
                            } else bin[i-1] = (short)(bin[i-1] + 64);                
                        } else bin[i-1] = (short)(bin[i-1] + 64);
                        aux = aux.replace("@", "");
                        bin[i] = (short) Integer.parseInt(aux);
                    } 
                    
                    else {
                        pc ++;
                        bin[i] = (short) Integer.parseInt(code[i]);
                    }
                break;
            }
        }
        
        //IMEDIATO OU DIRETO
        //0000000001000001
        //Retorno do vetor com os opcodes do arquivo de operações
        short robson[] = new short[dontTouch.size()];
        for (int i = 0; i < dontTouch.size(); i++) {
            int asd = dontTouch.get(i);
            robson[i] = (short) asd;
        }
        short finish[][] = new short[2][];
        finish[0] = bin;
        finish[1] = robson;
        return finish;
        //return bin; 
    }    
}