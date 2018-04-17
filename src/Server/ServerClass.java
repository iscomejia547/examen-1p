/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sistema19
 */
public class ServerClass{
    private ServerSocket server;
    private Socket com;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isConnected;
    
    public ServerClass() throws IOException {
        server=new ServerSocket(1206);     
    }

    public boolean isIsConnected() {
        return isConnected;
    }
    
    private class Comm extends Thread{
        @Override
        public void run(){
            try {
                com=server.accept();
                dis=new DataInputStream(com.getInputStream());
                dos=new DataOutputStream(com.getOutputStream());
                isConnected=true;
            } catch (IOException ex) {
                Logger.getLogger(ServerClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void attemptComm(){
        Comm connect=new Comm();
        connect.start();
    }
    public void passwordCheck() throws IOException{
        if(!isConnected){
            throw new IOException("No esta conectado");
        }
        Properties prop=new Properties();
        prop.load(new FileReader("properties/pswd.properties"));
        
        //0 es usuario, 1 es contraseña
        String[] rev=dis.readUTF().split(",");
        String user=rev[0], pswd=rev[1];
        if(prop.getProperty(user)!=null){
            dos.writeUTF(String.valueOf(prop.getProperty(user).equalsIgnoreCase(pswd)));
            return;
        }else{
            dos.writeUTF("false");
            return;
        }
    }
    public void resetter() throws IOException{
        isConnected=false;
        com=null;
    }
    
    public void calculate() throws IOException{
        if(!isConnected){
            throw new IOException("No esta conectado");
        }
        float salario=dis.readFloat();
        float inss=(inss(salario*12)/12);
        float IR=(ir(salario*12)/12);
        float neto=salario-inss-IR;
        dos.writeUTF(String.format("%.2f", salario)+","+String.format("%.2f", IR)+","+
                String.format("%.2f", inss)+","+String.format("%.2f", neto));
    }
    //metodos para calcular
    private float inss(float base){
        return (float)(base*0.0625);
    }
    private float ir(float base){
        if(base<=100000){
            return 0.0f;
        }else if(base>100000 & base <=200000){
            return (float)(base*0.15);
        }else if(base>200000 & base <=350000){
            return (float)(base*0.20+15000);
        }else if(base>350000 & base <=500000){
            return (float)(base*0.25+45000);
        }else if(base>500000){
            return (float)(base*0.3+82500);
        }
        return 0.0f;
}
}
