/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.montador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;  


/**
 *
 * @author Ariam
 */
public class Montador {

    public static String montar(String diretorio, int stack) throws FileNotFoundException, IOException {

        //PEGA O NOME DO ARQUIVO PARA O ARQUIVO DE SAIDA !!!! avaliar se isso é necessario !!!!
        String pathSaida[] = diretorio.split("/")[-1];
        String nomeSaida;
        nomeSaida = pathSaida[pathSaida.length -1];
        nomeSaida = nomeSaida.split(".")[0];

        HashMap<String, ArrayList<Integer>> refDeFora = new HashMap<>();
        HashMap<String, ArrayList<Integer>> refPraFora = new HashMap<>();
        HashMap<String, Integer> rotulos = new HashMap<>();
        List<Integer> dontTouch = new ArrayList<>();

        //váriaveis/objetos auxiliares pra funcionar
        File textfile = new File(diretorio);
        Scanner reader = new Scanner(textfile);
        List<String> data = new ArrayList<>();
        //List<Short> bin = new ArrayList<>();

        //flags auxiliares pra errors
        boolean hasEnd = false;
        boolean hasStart = false;
        boolean hasStack = false;
        int linhaAtual = -1; //do{}while() é overrated
        int pc = 0;
        int stackSize = 0;
        String nomeMod = "";
        String erro = "";

        String lst = "";        
        
        while (reader.hasNextLine()) { //passo 1
            String trololo = reader.nextLine();
            trololo = trololo.toUpperCase();
            linhaAtual++;

            if (trololo.length() > 80) {
                erro += new ErrorManager(1, linhaAtual).errorWarning();
                System.out.println(erro); // erro de linha mucho grande
            }

            if (!trololo.startsWith("*")) { //joga comentários fora 
                String[] fenriz = trololo.split("\\*");//editar pro símbolo de comment correto — não tem no pdf, consideramos *
                String[] aux = fenriz[0].split(" ");
                //System.out.println(aux.length);
                if (aux[0].equals("START")) {
                    nomeMod = aux[1];
                    continue;
                }

                if (aux[0].equals("END")) {
                    hasEnd = true; // pra tratar erro do end
                    break;
                }
                if (aux[0].equals("EXTR")) {
                    refDeFora.put(aux[1], new ArrayList<>());
                    continue;
                }
                if (aux[0].equals("EXTDEF")) {
                    refPraFora.put(aux[1], new ArrayList<>());
                    continue;
                }
                if (aux[0].equals("STACK")) {
                    hasStack = true;
                    if (aux.length == 2) {
                        stackSize = Integer.parseInt(aux[1]);
                    } else {//erro de sintaxe extremamente preciso pra esse caso
                        erro += new ErrorManager(5, linhaAtual).errorWarning();
                    }
                    continue;
                }
                if (refPraFora.containsKey(aux[0])) {
                    refPraFora.get(aux[0]).add(pc);
                }

                if (aux[0].equals("CONST")) {
                    data.add(aux[0] + aux[1]);
                    pc++;
                    continue;
                }

                if (!(new Label(aux[0]).isLabel())) {

                    if (rotulos.containsKey(aux[0])) {
                        erro += new ErrorManager(6, linhaAtual).errorWarning();
                    }

                    rotulos.put(aux[0], pc);
                    pc += aux.length - 1;
                    if (aux[1].equals("CONST")) {
                        aux = new String[]{aux[0], aux[1] + aux[2]}; //ooohray
                    }
                    for (int i = 1; i < aux.length; i++) {
                        data.add(aux[i]);
                    }
                } else {
                    pc += aux.length;
                    data.addAll(Arrays.asList(aux));
                }

                //todos os lugares q tem a LABEL externa [1,5]
                // LABEL, [1,5] ta em outro arquivo
                //exdef LABEL
                //label space
            }

        }
        if(hasStack == false){
            stackSize = stack;
        }
        if (!hasEnd) { //tratadando erro na marra
            erro += new ErrorManager(9, linhaAtual).errorWarning();
        }

        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }
        linhaAtual = 0;
        short[] bin = new short[data.size()];
        pc = 0;
        for (int i = 0; i < data.size(); i++) {  //passo 2
            switch (data.get(i)) {
                case "ADD":
                    dontTouch.add(pc);                    
                    bin[i] = 2;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    linhaAtual++;
                    break;
                case "BR":
                    dontTouch.add(pc);
                    bin[i] = 0;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    linhaAtual++;
                    break;
                case "BRNEG":
                    dontTouch.add(pc);
                    bin[i] = 5;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    linhaAtual++;
                    break;
                case "BRPOS":
                    dontTouch.add(pc);
                    bin[i] = 1;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    linhaAtual++;
                    break;
                case "BRZERO":
                    dontTouch.add(pc);
                    bin[i] = 4;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    linhaAtual++;
                    break;
                case "CALL":
                    dontTouch.add(pc);
                    bin[i] = 15;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    linhaAtual++;
                    pc++;
                    break;
                case "COPY":
                    dontTouch.add(pc);
                    bin[i] = 13;
                    pc++;
                    break;
                case "DIVIDE":
                    dontTouch.add(pc);
                    bin[i] = 10;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    break;
                case "LOAD":
                    lst += "\n" + pc + " " + bin[i] + " ";
                    dontTouch.add(pc);
                    pc++;
                    bin[i] = 3;
                    break;
                case "MULT":
                    dontTouch.add(pc);
                    bin[i] = 14;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    linhaAtual++;
                    pc++;
                    break;
                case "READ":
                    dontTouch.add(pc);
                    bin[i] = 12;
                    linhaAtual++;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    break;
                case "RET":
                    dontTouch.add(pc);
                    bin[i] = 9;
                    linhaAtual++;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    break;
                case "STOP":
                    dontTouch.add(pc);
                    bin[i] = 11;
                    lst += "\n" + pc + " " + bin[i] + " " + linhaAtual;
                    pc++;
                    linhaAtual++;
                    break;
                case "STORE":
                    dontTouch.add(pc);
                    bin[i] = 7;
                    linhaAtual++;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    pc++;
                    break;
                case "SUB":
                    dontTouch.add(pc);
                    bin[i] = 6;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    linhaAtual++;
                    pc++;
                    break;
                case "WRITE":                    
                    dontTouch.add(pc);
                    bin[i] = 8;
                    lst += "\n" + pc + " " + bin[i] + " ";
                    linhaAtual++;
                    pc++;
                    break;
                case "SPACE":
                    lst += "\n" + pc + " " + bin[i] + " " + linhaAtual;
                    dontTouch.add(pc);
                    pc++;
                    linhaAtual++;
                    break;
                default:
                    
                    if (data.get(i).startsWith("CONST")) {
                        bin[i] = (short) Integer.parseInt(data.get(i).replace("CONST", ""));
                        dontTouch.add(pc);
                        System.out.println(pc);
                        lst += "\n" + pc + " " + bin[i] + " " + linhaAtual;
                        pc++;
                        linhaAtual++;
                        break;
                    }
                    pc++;
                    //String aux = data.get(i);
                    if(data.get(i).endsWith(",I")){                        
                        if ((i - 2) >= 0) {
                            if (data.get(i - 2).equals("COPY")) {
                                bin[i - 2] = (short) (bin[i - 2] + 32);
                            } else {
                                bin[i - 1] = (short) (bin[i - 1] + 16);
                            }
                        } else {
                            bin[i - 1] = (short) (bin[i - 1] + 16);
                        }
                        data.set(i, data.get(i).replace(",I", ""));
                        
                    }
                    if (rotulos.containsKey(data.get(i))) {
                        int aux_tabela = rotulos.get(data.get(i));
                        bin[i] = (short) aux_tabela;
                        lst = lst + bin[i] + " " + linhaAtual;
                        break;
                    }
                    if (refDeFora.containsKey(data.get(i))) {
                        refDeFora.get(data.get(i)).add(i);
                        bin[i] = 0; //placeholder, vai ser alterado no ligador
                        System.out.println(data.get(i) + " na pos +" + i);
                        lst = lst + bin[i] + " " + linhaAtual;

                        break;
                    }
                    //literal
                    //SE NÃO FOR UMA LABE, VERIFICA SE É IMEDIATO OU DIRETO
                    if (data.get(i).startsWith("#")) {
                        if ((i - 2) >= 0) {
                            if (data.get(i - 2).equals("COPY")) {
                                bin[i - 2] = (short) (bin[i - 2] + 64);
                            } else {
                                bin[i - 1] = (short) (bin[i - 1] + 64);
                            }
                        } else {
                            bin[i - 1] = (short) (bin[i - 1] + 64);
                        }
                        data.set(i, data.get(i).replace("#", ""));
                        bin[i] = (short) Integer.parseInt(data.get(i));
                    } else {
                        bin[i] = (short) Integer.parseInt(data.get(i));
                    }
                    lst = lst + bin[i] + " " + linhaAtual;
            }
        }
        for (int i = 0; i < bin.length; i++) {
            System.out.println(bin[i]);
        }
        //escrever no arquivo:

        
        String header = "!" + refDeFora + "\n" ;
        header += refPraFora + "\n";
        header += "STACKSIZE=" + stackSize + "\n";
        
        for (int i = 0; i < dontTouch.size(); i++) {
            header += dontTouch.get(i) + " ";
        }
        header += "//p";
        header = header.replace(" //p", ""); // retira espaço da ultima linha
        header += "\n***\n";
        
        for (int i = 0; i < bin.length; i++) {
            header += bin[i] + " ";
        }
        header += "\n"; //EOF?
        System.out.println(header);
        File obj = new File(nomeSaida+".obj");
        obj.createNewFile();
        String peterSteele = obj.getAbsolutePath();
        FileWriter objWriter = new FileWriter(nomeSaida+".obj");
        objWriter.write(header);
        objWriter.close();
        System.out.println("escripto");
        
        objWriter = new FileWriter(nomeSaida+".lst");
        objWriter.write(lst);
        lst+= erro;
        objWriter.close();
        
        return peterSteele;
        
    }

}
