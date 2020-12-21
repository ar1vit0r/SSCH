package com.ps.processador_de_macros;

import java.util.ArrayList;
import java.util.List;

public class Macro {
    public String nome;
    public List<String> definicao = new ArrayList<>();
    public List<String> argumentos = new ArrayList<>();
    public List<String> labels = new ArrayList<>();
    
    public void print(){
        System.out.println(nome);
        System.out.println(definicao);
        System.out.println(argumentos);
        System.out.println(labels);
    }
    
}
