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
public class Label {
    private String umLabelOUnao;
    
    public Label(String umLabelOUnao){
        this.umLabelOUnao = umLabelOUnao;
    }
    
    public Boolean isLabel(){
        switch(umLabelOUnao) {
            case "CONST":
                return true;
            case "SPACE":
                return true;
            case "STOP":
                return true;
            case "LOAD":
                return true;
            case "STORE":
                return true;
            case "READ":
                return true;
            case "WRITE":
                return true;
            case "CALL":
               return true;
            case "COPY":
                return true;
            case "RET":
                return true;
            case "ADD":
                return true;
            case "SUB":
                return true;
            case "MULT":
                return true;
            case "DIVIDE":
                return true;
            case "BR":
                return true;
            case "BRNEG":
                return true;
            case "BRPOS":
                return true;
            case "BRZERO":
                return true;       
        }  
    return false;
    }
}