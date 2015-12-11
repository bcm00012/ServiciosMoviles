package servidorjava;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.mysql.jdbc.Statement;

public class DB {
 
 public String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
 public String URL_MYSQL = "jdbc:mysql://127.0.0.1/eje1serviciosmoviles";
 public Connection conn;
 
	 public DB(){
		 cargarDriver();
	 }
 
	 public void cargarDriver() {
		 try {
		 Class.forName(DRIVER_MYSQL);
		 } catch (ClassNotFoundException e) { e.printStackTrace();}
	 }
 
	 public void getConexion(){
		 try {
		 conn = DriverManager.getConnection(URL_MYSQL,"root","123456");
		 } catch (SQLException e) {e.printStackTrace();}
	 }

 
	 public boolean checkLogin (String inputUser,String inputPassword){
		 String userBdd; String passwordBdd;
		 ResultSet rs = null;
		 String res="";
		 String sql="SELECT * FROM login";
		 getConexion();
		 int flagAuth=0; // Si su valor es 1 implica autenticación correcta
		 try {
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 //stmt.setString(1, inputUser);
			 
			 rs = stmt.executeQuery();
			 while (rs.next()){
			 
				 userBdd=rs.getString(2); 
				 passwordBdd=rs.getString(3); 
				 
				 //System.out.println(userBdd);
				 //System.out.println(inputUser);
				 if ((userBdd.equalsIgnoreCase(inputUser)) && (passwordBdd.equalsIgnoreCase(inputPassword))){
					 flagAuth=1;
					 //System.out.println("CORRECT LOGIN"); 
				 } else {			 
				 //System.out.println("BAD LOGIN");
				 }	
			 }
			 rs.close(); 
			 stmt.close();
			 conn.close();
			 
		 } catch (Exception e) { 
			 e.printStackTrace();
		 }
		 
		 if (flagAuth==1){
			 return true;
		 }else{
			 return false;
		 } 
	 }
 
	 public boolean upLogin (String inputUser,String inputPassword){
		 String userBdd= inputUser; String passwordBdd= inputPassword;
		 ResultSet rs = null;
		System.out.println("\n\nBBBBBDDDDDDDDDD\n\n");

		// String sql="INSERT INTO login(users,passwords) VALUES('" + userBdd + "','" + inputPassword + "')";
		 //String sql="INSERT INTO login(users,passwords) VALUES(?,?,?)";
		String sql="INSERT INTO login(users,passwords) VALUES('" + userBdd + "','" + inputPassword + "')";
		
		System.out.println(sql);
		 	

		 getConexion();
		 int flagAuth=0; // Si su valor es 1 implica autenticación correcta
		 try {
			 java.sql.Statement stmt = conn.createStatement();
			 //stmt.setString(1, inputUser);
			
			 stmt.execute(sql);
			 
			 stmt.close();
			 conn.close();
			 
		 } catch (Exception e) { 
			 e.printStackTrace();
		 }
		 
		 if (flagAuth==1){
			 return true;
		 }else{
			 return false;
		 } 
	 }
}