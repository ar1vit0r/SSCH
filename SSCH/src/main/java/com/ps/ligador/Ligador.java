/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.ligador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Frederico Peixoto Antunes
 */
public class Ligador {
    
    public static String main(String objeto1, String objeto2) throws FileNotFoundException, IOException {        
        //Esse código tá programado pra somente receber dois objetos, ou seja, ligar somemente dois códigos
        File textfile1 = new File(objeto1);
        Scanner reader1 = new Scanner(textfile1);
        File textfile2 = new File(objeto2);
        Scanner reader2 = new Scanner(textfile2);
        boolean wasConst = false;

        ///---------------------------------------------------------------------Salva cada linha do objeto1
        String Seg1Line1 = reader1.nextLine();  //TabelaUso:    !{DEFORA2=[5], DEFORA=[7, 9]}
        String Seg1Line2 = reader1.nextLine();  //TabelaDef:    {DEDENTRO=[17]}
        String Seg1Line3 = reader1.nextLine();  //Tamanho:      STACKSIZE=8
        String Seg1Line4 = reader1.nextLine();  //Don'tTouch:   11 12 13 14 15 16 17 18
        String Seg1Line5 = reader1.nextLine();  //              ***
        String Seg1Line6 = reader1.nextLine();  //Programa:     12 13 12 14 12 0 12 0 8 0 11 8  0  0  0  0  0  0  0 

        ///---------------------------------------------------------------------Salva cada linha do objeto2
        String Seg2Line1 = reader2.nextLine();
        String Seg2Line2 = reader2.nextLine();
        String Seg2Line3 = reader2.nextLine();
        String Seg2Line4 = reader2.nextLine();
        String Seg2Line5 = reader2.nextLine();
        String Seg2Line6 = reader2.nextLine();

        ///---------------------------------------------------------------------Aqui vai algoritmo pra transformar o texto em hashmap
        Map<String, Integer> TabelaUso1 = new HashMap<String, Integer>();
        Map<String, Integer> TabelaDef1 = new HashMap<String, Integer>();
        Map<String, Integer> TabelaUso2 = new HashMap<String, Integer>();
        Map<String, Integer> TabelaDef2 = new HashMap<String, Integer>();
        //laço que quebra string e armazena e coloca no hashmap
        
        
        ///---------------------------------------------------------------------Transforma a lista de 'absolutos' em array de inteiros
        String[] temp = Seg1Line4.split(" ");
        ArrayList<Integer> Absolutos1 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Absolutos1.add(Integer.parseInt(temp[i]));

        temp = Seg2Line4.split(" ");
        ArrayList<Integer> Absolutos2 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Absolutos2.add(Integer.parseInt(temp[i]));

        ///---------------------------------------------------------------------Transforma a string do código em um array de inteiros
        temp = Seg1Line6.split(" ");
        ArrayList<Integer> Codigo1 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Codigo1.add(Integer.parseInt(temp[i]));

        temp = Seg2Line6.split(" ");
        ArrayList<Integer> Codigo2 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Codigo2.add(Integer.parseInt(temp[i]));
        
        ///---------------------------------------------------------------------Pega tamanho da pilha
        temp = Seg1Line3.split("STACKSIZE=");
        Integer stackSize = Integer.parseInt(temp[0]);
        
        ///---------------------------------------------------------------------Passagem1
        /// Unifica as tabelas de definições dos vários segmentos em um única tabela (tabela de símbolos globais)
        /// Tabela de definições é copiada para a TSG (1ª tabela copiada sem alterações, na 2ª tabela o valor dos endereços é adicionado do tamanho do primeiro segmento)
        Map<String, Integer> TSG = new HashMap<>(TabelaDef1);    //laço que passa toda primeira tabela hashmap pro TSG
        //Laço que passa pela segunda tabela elemento por elemento, ajustando conforme o tamanho da primeira, e colocando pro TSG
        Integer offsetSeg1 = Codigo1.size();
        for (String i : TabelaDef2.keySet()) {
            TabelaDef2.put(i, TabelaDef2.get(i)+offsetSeg1);
        }
        TSG.putAll(TabelaDef2);

        ///---------------------------------------------------------------------Passagem 2
        /// Atualiza os endereços para refletir a união dos segmentos
        //Atualiza endereços da TabelaUso2 e valores relativos em Código2
        //Atualiza endereços da TabelaUso2
        for(String i : TabelaUso2.keySet()){
            TabelaUso2.put(i, TabelaUso2.get(i)+offsetSeg1);
        }
        
        //Passa codigo1 pro codigo final sem alteração, enquanto passa codigo2 alterando endereços dos relocaveis
        ArrayList<Integer> Codigo = new ArrayList<>(Codigo1);
        //Para saber se valor é relocavel, procura o endereço na lista de Absolutos
        for(int i=0; i<Codigo2.size(); i++){
            for(int j=0; j<Absolutos2.size(); j++){
                if(i == Absolutos2.get(j)){
                    Codigo2.set(i, Codigo2.get(i)-offsetSeg1);
                    break;
                }
            }
            Codigo2.set(i, Codigo2.get(i)+offsetSeg1);
        }
        Codigo.addAll(Codigo2);
        
        //Junta lista de absolutos (somente depois de atualizar os endereços de Absolutos2)
        for(int i=0; i<Absolutos2.size(); i++)
            Absolutos2.set(i,Absolutos2.get(i)+offsetSeg1);
        ArrayList<Integer> Absolutos = new ArrayList<>(Absolutos1);
        Absolutos.addAll(Absolutos2);
        
        /// Atualizar as referências externas baseado na TSG e tabelas de uso (o endereço é atualizado)
        //Ir nos endereços apontados por cada Tabela de Uso, substituir/somar valores ao endereço, e remover endereço da lista de Absolutos
        for (String i : TabelaUso1.keySet()) {
            //vai no codigo nessa posição e soma com o valor encontrado na TabDef com a mesma key
            int j = TabelaUso1.get(i);
            Codigo.set(j, Codigo.get(j)+TSG.get(i));  //Codigo.set(TabelaUso1.get(i), TSG.get(i));
            
            //remove endereço da lista dos absolutos
            //percorre todos absolutos e quando for igual ao endereço remove?
            Absolutos.remove(Integer.valueOf(j));
        }
        for (String i : TabelaUso2.keySet()) {
            //vai no codigo nessa posição e soma com o valor encontrado na TabDef com a mesma key
            int j = TabelaUso2.get(i);
            Codigo.set(j, Codigo.get(j)+TSG.get(i));  //Codigo.set(TabelaUso1.get(i), TSG.get(i));
            
            //remove endereço da lista dos absolutos
            Absolutos.remove(Integer.valueOf(j));
        }
        
        /// Restante do código é apenas copiado
        
    
        ///---------------------------------------------------------------------Escrever no arquivo:
        //ainda não 100% adapatado
        String nome = textfile1.getName();
        
        /// O ligador terá que informar ao carregador o tamanho da pilha (soma dos tamanhos necessários para cada módulo ligado) e o mapa de relocação (quando necessário).
        ///Também deverá ser informado o endereço inicial para execução.
        
        //só usar o stacksize que o montador mandou?
        stackSize = Codigo.size();
        
        int initEnd = stackSize + 2;
        
        String header = stackSize + "\n";       //Tamanho da Pilha
        for (Integer i : Absolutos){
            header += i + " ";                  //Mapa de Relocação
        }
        header += "\n" + initEnd + "\n";        //Endereço inicial
        for (Integer i : Codigo){
            header += i + " ";                  //Mapa de Relocação
        }
        header += "//p";
        header = header.replace(" //p", "");  // retira espaÃ§o da ultima linha
        header += "\n"; //EOF?
        System.out.println(header);
        File obj = new File(nome+".HPX");
        obj.createNewFile();
        
        FileWriter objWriter = new FileWriter(nome+".HPX");
        String endereco = abjWriter.getAbsolutePath();
        objWriter.write(header);
        objWriter.close();
        System.out.println("Arquivo HPX salvo");
        return endereco;
    }
}
