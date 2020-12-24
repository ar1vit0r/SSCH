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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Frederico Peixoto Antunes
 */
public class Ligador {
    
    public static String main(String objeto1, String objeto2) throws FileNotFoundException, IOException {        
        //Esse cÃ³digo estÃ¡ programado pra somente receber dois objetos, ou seja, ligar somemente dois cÃ³digos
        File textfile1 = new File(objeto1);
        Scanner reader1 = new Scanner(textfile1);
        File textfile2 = new File(objeto2);
        Scanner reader2 = new Scanner(textfile2);

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

        ///---------------------------------------------------------------------Algoritmo pra transformar o texto em hashmap
        Map<String, ArrayList<Integer>> TabelaUso1 = new HashMap<String, ArrayList<Integer>>();
        Map<String, ArrayList<Integer>> TabelaDef1 = new HashMap<String, ArrayList<Integer>>();
        Map<String, ArrayList<Integer>> TabelaUso2 = new HashMap<String, ArrayList<Integer>>();
        Map<String, ArrayList<Integer>> TabelaDef2 = new HashMap<String, ArrayList<Integer>>();
        
        //String pra HashMap de sÃ³ uma das linhas de um objeto
        ///---------------------------------------------------------------------String pra HashMap de TabelaUso1
        String chave = "";
        ArrayList<Integer> valores = new ArrayList<Integer>();
        String[] str2 =  Seg1Line1.split("[!\\}\\{\\]]+");
                /*
                System.out.println(str2[0]);
                System.out.println(str2[1]);
                System.out.println(str2[2]);
                //*/
        //for(String i : str2){
        for(int i=1; i<str2.length; i++){
          //System.out.println(str2[i]);        //Testa str2
          int count = 0;
          String[] str3 = str2[i].split("[=\\[, ]+");
                /*
                System.out.println(str2[0]);
                System.out.println(str2[1]);
                System.out.println(str2[2]);
                //*/
          //for(String j : str3){
          for(int j=0; j<str3.length; j++){
            if(i!=1 && j==0)                    //Gambiarra, pois split elimina elementos no inicio da string, devolvendo string vazia
                    j++;
            //System.out.println(str3[j]);
            if(count==0){
                System.out.println("Key= "+str3[j]);
                chave=str3[j];
            }
            else{
              System.out.println("V= "+str3[j]);
              valores.add(Integer.parseInt(str3[j]));     //Faz parse pra int
            }
            count++;
          }
          //adiciona key e valores no map
          TabelaUso1.put(chave, valores);
        }
        
        ///---------------------------------------------------------------------String pra HashMap de TabelaDef1
        String[] str2_2 =  Seg1Line2.split("[!\\}\\{\\]]+");
        for(int i=1; i<str2_2.length; i++){
          int count = 0;
          String[] str3 = str2_2[i].split("[=\\[, ]+");
          for(int j=0; j<str3.length; j++){
            if(i!=1 && j==0)                    //Gambiarra, pois split elimina elementos no inicio da string, devolvendo string vazia
                    j++;
            if(count==0){
                System.out.println("Key= "+str3[j]);
                chave=str3[j];
            }
            else{
              System.out.println("V= "+str3[j]);
              valores.add(Integer.parseInt(str3[j]));     //Faz parse pra int
            }
            count++;
          }
          TabelaDef1.put(chave, valores);
        }
        
        ///---------------------------------------------------------------------String pra HashMap de TabelaDef1
        String[] str2_3 =  Seg1Line2.split("[!\\}\\{\\]]+");
        for(int i=1; i<str2_3.length; i++){
          int count = 0;
          String[] str3 = str2_3[i].split("[=\\[, ]+");
          for(int j=0; j<str3.length; j++){
            if(i!=1 && j==0)                    //Gambiarra, pois split elimina elementos no inicio da string, devolvendo string vazia
                    j++;
            if(count==0){
                System.out.println("Key= "+str3[j]);
                chave=str3[j];
            }
            else{
              System.out.println("V= "+str3[j]);
              valores.add(Integer.parseInt(str3[j]));     //Faz parse pra int
            }
            count++;
          }
          TabelaUso2.put(chave, valores);
        }
        
        ///---------------------------------------------------------------------String pra HashMap de TabelaDef1
        String[] str2_4 =  Seg1Line2.split("[!\\}\\{\\]]+");
        for(int i=1; i<str2_4.length; i++){
          int count = 0;
          String[] str3 = str2_4[i].split("[=\\[, ]+");
          for(int j=0; j<str3.length; j++){
            if(i!=1 && j==0)                    //Gambiarra, pois split elimina elementos no inicio da string, devolvendo string vazia
                    j++;
            if(count==0){
                System.out.println("Key= "+str3[j]);
                chave=str3[j];
            }
            else{
              System.out.println("V= "+str3[j]);
              valores.add(Integer.parseInt(str3[j]));     //Faz parse pra int
            }
            count++;
          }
          TabelaDef2.put(chave, valores);
        }
        
        ///---------------------------------------------------------------------Transforma a lista de 'absolutos' em array de inteiros
        String[] temp = Seg1Line4.split(" ");
        ArrayList<Integer> Absolutos1 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Absolutos1.add(Integer.parseInt(temp[i]));

        temp = Seg2Line4.split(" ");
        ArrayList<Integer> Absolutos2 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Absolutos2.add(Integer.parseInt(temp[i]));

        ///---------------------------------------------------------------------Transforma a string do cÃ³digo em um array de inteiros
        temp = Seg1Line6.split(" ");
        ArrayList<Integer> Codigo1 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Codigo1.add(Integer.parseInt(temp[i]));

        temp = Seg2Line6.split(" ");
        ArrayList<Integer> Codigo2 = new ArrayList<Integer>();
        for(int i = 0; i<temp.length; i++)
            Codigo2.add(Integer.parseInt(temp[i]));
        
        ///---------------------------------------------------------------------Pega tamanho da pilha
        String temp2;
        temp2 = Seg1Line3.replace("STACKSIZE=", "");
        String temp21 = Seg2Line3.replace("STACKSIZE=", "");

        Integer stackSize = Integer.parseInt(temp2) + Integer.parseInt(temp21);
        
        ///---------------------------------------------------------------------Passagem1
        /// Unifica as tabelas de definições dos vários segmentos em um única tabela (tabela de símbolos globais)
        /// Tabela de definições é copiada para a TSG (1ª tabela copiada sem alterações, na 2ª tabela o valor dos endereços é adicionado do tamanho do primeiro segmento)
        Map<String, ArrayList<Integer>> TSG = new HashMap<String, ArrayList<Integer>>(TabelaDef1);    //laço que passa toda primeira tabela hashmap pro TSG
        //Laço que passa pela segunda tabela elemento por elemento, ajustando conforme o tamanho da primeira, e colocando pro TSG
        Integer offsetSeg1 = Codigo1.size();
        for (String i : TabelaDef2.keySet()) {
//----------get retorna um ArrayList, e é necessário somar offset em todos elementos do array (ir atualizando no array), e depois atualizar a tabela com o novo ArrayList
            ArrayList<Integer> ListaEnd = new ArrayList<>(TabelaDef2.get(i));   //TabelaDef teoricamente somente pode ter um endereco, portanto esse array tera somente um valor
            for(int j=0; j<ListaEnd.size(); j++)                                
                ListaEnd.set(j,ListaEnd.get(j)+offsetSeg1);
            TabelaDef2.put(i, ListaEnd);
        }
        TSG.putAll(TabelaDef2);

        ///---------------------------------------------------------------------Passagem 2
        /// Atualiza os endereços para refletir a união dos segmentos
        //Atualiza endereços da TabelaUso2
        for(String i : TabelaUso2.keySet()){
//----------get retorna um ArrayList, e é necessário somar offset em todos elementos do array (ir atualizando no array), e depois atualizar a tabela com o novo ArrayList
            ArrayList<Integer> ListaEnd = new ArrayList<>(TabelaUso2.get(i));
            for(int j=0; j<ListaEnd.size(); j++)
                ListaEnd.set(j,ListaEnd.get(j)+offsetSeg1);
            TabelaUso2.put(i, ListaEnd);
        }
        
        //Passa codigo1 pro codigo final sem alteraÃ§Ã£o, enquanto passa codigo2 alterando endereÃ§os dos relocaveis
        ArrayList<Integer> Codigo = new ArrayList<>(Codigo1);
        //Para saber se valor Ã© relocavel, procura o endereÃ§o na lista de Absolutos
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
        
        //Junta lista de absolutos (somente depois de atualizar os endereÃ§os de Absolutos2)
        for(int i=0; i<Absolutos2.size(); i++)
            Absolutos2.set(i,Absolutos2.get(i)+offsetSeg1);
        ArrayList<Integer> Absolutos = new ArrayList<>(Absolutos1);
        Absolutos.addAll(Absolutos2);
        
        /// Atualizar as referencias externas baseado na TSG e tabelas de uso (o endereco e atualizado)
        //Ir nos enderecos apontados por cada Tabela de Uso, substituir/somar valores ao endereco, e remover endereco da lista de Absolutos
        for (String i : TabelaUso1.keySet()) {
            //int j = TabelaUso1.get(i);                  //No codigo nessa posicao e soma com o valor encontrado na TabDef com a mesma key
//----------TabelaUso1 vai retornar um ArrayList. Tem que cada um desses elementos ser atualizado.
            //Como até TabelaDef foi feita com ArrayList, apesar de não poder ter mais de um endereço, TSG funciona da mesma forma
            //Portanto será necessário pegar ArrayList retornado por TSG.get, e somar o primeiro valor contido nele
            ArrayList<Integer> ListaEnd = new ArrayList<>(TabelaUso1.get(i));
            ArrayList<Integer> ListaTSG = new ArrayList<>(TSG.get(i));
            //Pra cada endereço na TabelaUso, atualiza aquele endereço no código, com o primeiro valor da ListaTSG, e remove este endereço da lista de absolutos
            for(int j=0; j<ListaEnd.size(); j++){
                int EndUso = ListaEnd.get(j);                               //Tabela de Uso : [i] [EndUso]
                int EndDef = ListaTSG.get(0);                               //Tabela de Def : [i] [EndDef]
                Codigo.set(EndUso, Codigo.get(EndUso)+EndDef);              //Pega cada endereco do arraylist no codigo, e soma com o endereco de definicao da label desses enderecos
                
                Absolutos.remove(Integer.valueOf(EndUso));                  //Remove endereco da lista dos absolutos
            }
        }
        for (String i : TabelaUso2.keySet()) {
            //int j = TabelaUso2.get(i);                  //No codigo nessa posicao e soma com o valor encontrado na TabDef com a mesma key
//----------TabelaUso1 vai retornar um ArrayList. esse array teoricamente
            ArrayList<Integer> ListaEnd = new ArrayList<>(TabelaUso2.get(i));
            ArrayList<Integer> ListaTSG = new ArrayList<>(TSG.get(i));
            //Pra cada endereço na TabelaUso, atualiza aquele endereço no código, com o primeiro valor da ListaTSG, e remove este endereço da lista de absolutos
            for(int j=0; j<ListaEnd.size(); j++){
                int EndUso = ListaEnd.get(j);                               //Tabela de Uso : [i] [EndUso]
                int EndDef = ListaTSG.get(0);                               //Tabela de Def : [i] [EndDef]
                Codigo.set(EndUso, Codigo.get(EndUso)+EndDef);              //Pega cada endereco do arraylist no codigo, e soma com o endereco de definicao da label desses enderecos
                
                Absolutos.remove(Integer.valueOf(EndUso));                  //Remove endereco da lista dos absolutos
            }
        }
        
        /// Restante do cÃ³digo Ã© apenas copiado
    
        ///---------------------------------------------------------------------Escrever no arquivo:
        String nome = textfile1.getName();
        
        /// O ligador terÃ¡ que informar ao carregador o tamanho da pilha (soma dos tamanhos necessÃ¡rios para cada mÃ³dulo ligado) e o mapa de relocaÃ§Ã£o (quando necessÃ¡rio).
        ///TambÃ©m deverÃ¡ ser informado o endereÃ§o inicial para execuÃ§Ã£o.
        
        //SÃ³ usar o stacksize que o montador mandou?
        //stackSize = Codigo.size();
        
        int initEnd = stackSize + 2;
        
        String header = stackSize + "\n";       //Tamanho da Pilha
        for (Integer i : Absolutos){
            header += i + " ";                  //Mapa de RelocaÃ§Ã£o
        }
        header += "\n" + initEnd + "\n";        //EndereÃ§o inicial
        for (Integer i : Codigo){
            header += i + " ";                  //Mapa de RelocaÃ§Ã£o
        }
        header += "//p";
        header = header.replace(" //p", "");    // Retira espaÃ§oo da ultima linha
        header += "\n"; //EOF?
        System.out.println(header);
        File obj = new File(nome+".HPX");
        
        String saida = obj.getAbsolutePath();
        nome = nome.replace(".obj", "");
        FileWriter objWriter = new FileWriter(nome+".HPX");
        objWriter.write(header);
        objWriter.close();
        reader1.close();
        reader2.close();
        System.out.println("Arquivo HPX salvo");
        
        return saida;
    }
}