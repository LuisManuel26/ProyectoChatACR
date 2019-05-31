/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import GUI.ClientegrupoGUI;
import GUI.ClienteprivGUI;
import java.awt.Menu;
import GUI.Registro;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Manuel Montoya
 */
public class Controlclientes extends Thread{
   
    
   String nom= null;
   Socket cliente= null;
   final Controlclientes[] threads;
   BufferedReader in = null;
   PrintStream out = null;
   int numc;
   ClienteprivGUI cp;
   ClientegrupoGUI cg;
   Menu m;
   boolean bul;
   boolean tiEspera;
   int contadorG = 0;
   int contadorP = 0;
   
   public Controlclientes(Socket s1, Controlclientes[] ts){
   numc = ts.length;
   this.cliente = s1;
   this.threads = ts;
   m = new Menu();
   cp = new ClienteprivGUI();
   cg = new ClientegrupoGUI();
   m.setControlclientes(this);
   cp.setControlclientes(this);
   cg.setControlclientes(this);
   bul = false;
   opcion ="";
   this.tiEspera= false;
   
   
   
   }
     public void setNombre(String nom) {
     this.nom=nom;
     this.cp.setearConectados(new ArrayList<String>());
     this.cg.setearConectados(new ArrayList<String>());
     tiEspera = true;
     
     }
   
     public void setBul(boolean bul){
     this.bul = bul;
     
     }
     public void run(){
     int c= this.numc;
     Controlclientes[] threads = this.threads;
     try
     {
     in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
     out = new PrintStream(cliente.getOutputStream());
     Registro r = new Registro();
     r.setVisible(true);
     r.setControlclientes(this);
     r.setMenu(m);
     m.setClientePrivado(cp);
     m.setClienteGrupo(cg);
     while(!tiEspera){
     try{
     Thread.sleep(100);
     }catch(InterruptedException ex){
     
     
     }
     
     }
     ArrayList<String> conectados new ArrayList<>();
     for(int i = 0; i< c;i ++){
     if(threads[i] != null && threads[i] != this){
     conectados.add(threads[i].nom);
     threads[i].cp.añadirConectado(this.nom);
     threads[i].cg.añadirConectado(this.nom);
     threads[i].cp.escribirenareAc(" "+this.nom+"Bienvenido al chat");
     threads[i].cg.escribirenareaAc(" "+this.nom+"Bienvenido al chat");
     }
     
     }
     cp.setearConectados(conectados);
     cg.setearConectados(Conectados);
     }catch(IOException e){
     System.out.println(e.getMessage());
     
     }
     
     }
     String opcion;

    public void enviarOpcion(String opcion) {
        this.opcion = opcion;
        this.bul = true;
        int c = this.numc;
        System.out.print("");
        String lee = opcion;
        if (lee.contains("UNICAST$")) {
            lee = lee.replace("UNICAST$", "");
            String nombreContacto = "";
            int ind = 0;
            contadorP++;
            while (lee.charAt(ind) != '$') {
                nombreContacto += lee.charAt(ind);
                ind++;
            }
            ind++;
            lee = lee.replace(nombreContacto + "$", "");
            Controlclientes contacto = null;
            for (int i = 0; i < c; i++) {
                if (threads[i] != null && threads[i] != this && threads[i].nom.equals(nombreContacto)) {
                    out.println(i + ". " + threads[i].nom);
                    contacto = threads[i];
                }

            }
            this.cp.escribirEnArea("> " + this.nom + ": " + lee);
            
            if (contacto != null) {
                contacto.cp.escribirEnArea(">> " + this.nom + ": " + lee);
                contacto.m.setjLabelMsjP(contadorP);
            }
        } //termina if de UNICAST
        else if (lee.equals("$EXIT$")) {
            for (int i = 0; i < c; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].cp.escribirenareAc("~ " + this.nom + " ha salido del chat");
                    threads[i].cp.quitarConectado(this.nom);
                }
            }
            this.cp.dispose();
        }
    }
    String opcion2;

    public void enviarOpcionGrupo(String opcion) {
        this.opcion2 = opcion;
        this.bul = true;
        int c = this.nc;
        System.out.print("");
        String lee = opcion;

        if (lee.contains("MULTICAST$")) {
            contadorG++;
            lee = lee.replace("MULTICAST$", "");
            for (int i = 0; i < c; i++) {
                if (threads[i] != null) {
                    threads[i].cg.escribirEnArea(">> " + this.nom + ": " + lee);
                }

                if (threads[i] != this) {
                    threads[i].m.setjLabelMsjG(contadorG);
                }

            }
        } else if (lee.equals("$EXIT$")) {
            for (int i = 0; i < c; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].cg.escribirenareaAc("~ " + this.nom + " ha salido del chat");
                    threads[i].cg.quitarConectado(this.nom);
                }
            }
            this.cg.dispose();
        }
    }

    public int getContadorG() {
        return contadorG;
    }

    public void setContadorG(int contadorG) {
        this.contadorG = contadorG;
    }
    public int getContadorP() {
        return contadorP;
    }

    public void setContadorP(int contadorP) {
        this.contadorP = contadorP;
    }
}
