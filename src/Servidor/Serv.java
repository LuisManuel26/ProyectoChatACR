/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Manuel Montoya
 */
public class Serv {
    
    public static void main (String args[]){
    // Declaracion de sockets
    ServerSocket s1=null; 
    Socket s2= null;
    int maxClientes = 15;
    final Controlclientes[] numClientes= new Controlclientes[maxClientes];
    
    try{
    s1 = new ServerSocket(2605);
    System.out.println("Servidor iniciado");
    
    while(true)// Dejar activo el servidor
    {
    s2= s1.accept();
    int i=0;
    for(i=0; i< maxClientes; i++){
    if(numClientes[i]== null){
    (numClientes[i] = new Controlclientes(s2, numClientes)).start();
    break;
    }
    
     }
    if(i == maxClientes){
    PrintStream serv = new PrintStream(s2.getOutputStream());
    serv.println("Servidor lleno, vuelva despues");
    serv.close();
    
    }
    
    
    }   
        
    }
    catch(IOException e){
    e.printStackTrace();
    }
    try{
    s1.close();
    s2.close();
    
    }
    catch(IOException ex){
    ex.printStackTrace();
    
    }
    }
}
