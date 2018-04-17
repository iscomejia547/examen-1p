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
public class ServerClass extends Thread{
    private ServerSocket server;
    private Socket com;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isConnected;
    
    public ServerClass() throws IOException {
        server=new ServerSocket(1207);      
    }

    public boolean isIsConnected() {
        return isConnected;
    }
    
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
    
    public void passwordCheck() throws IOException, Exception{
        if(!isConnected){
            throw new Exception("No esta conectado");
        }
        Properties prop=new Properties();
        prop.load(new FileReader("properties/pswd.properties"));
        
        //0 es usuario, 1 es contraseña
        String[] rev=dis.readUTF().split(",");
        String user=rev[0], pswd=rev[1];
        System.out.println(user);
        System.out.println(pswd);
        System.out.println(prop.getProperty(user));
        System.out.println(prop.getProperty(user).equalsIgnoreCase(pswd));
        if(prop.getProperty(user)!=null){
            dos.writeUTF(String.valueOf(prop.getProperty(user).equalsIgnoreCase(pswd)));
        }else{
            dos.writeUTF("false");
            return;
        }
    }
    
    
}
