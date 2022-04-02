package com.benesoft.cashierssystem;
import java.sql.*;
import javax.swing.JOptionPane;



public class ConnectionClass {
    String user = "benedictAdmin";
    String pass = "Mutisya1996!"; 
    Connection conn;
    //String dbUrl = "jdbc:mysql://ec2-3-220-53-255.compute-1.amazonaws.com:3306/testingdb";
    String dbUrl = "jdbc:mysql://localhost:3306/testingdb";
    String emailuser = "benemutua@gmail.com";
    String emailpass = "Benemutua1996!";
    
  
public boolean getConnection(){       
        try{
            conn = DriverManager.getConnection(dbUrl, user, pass);
            return true;            
        }catch(Exception e){
            return false;
        }
    }
}