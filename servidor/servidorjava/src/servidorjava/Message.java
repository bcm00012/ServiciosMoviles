package servidorjava;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Message {

	private ArrayList<String> request = new ArrayList(Arrays.asList("retr","stor","list","user","pass","quit","help","cwd","dele","path","useb","pasb"));
	ArrayList<String> response = new ArrayList<String>();
	String input_request;
	String user, pass;

	public Message(){
	}
	
	public String listFiles( File dir) {
		String salida="\r\n";
		 File[] files = dir.listFiles();
		 	for (int x=0;x<files.length;x++){
		 		salida = salida + files[x].getName()+"\r\n";
		 		System.out.println(files[x].getName());
		 	}
		 return salida;
		 }
	
	public String[] Request(String input_request){
		Iterator<String> iterator = request.iterator();
		int pointer= 0;
		String date= null;
		String request= null;
		String req = input_request.toLowerCase();
		while (iterator.hasNext()){
				String r= iterator.next();
				if (req.contains(r)){
					 request = r ;	
					 pointer = 1;
					 
					 if ((request.substring(0, 3).equalsIgnoreCase(req.substring(0, 3))== false) ){
						 pointer = 0;
					 
					 }
					 if((request.equalsIgnoreCase("list"))&& (req.equalsIgnoreCase("list") == false))
						 pointer = 0;
								 
					 if (req.equalsIgnoreCase(request) == false ) {
						 	System.out.println("\nEntra en el if separar Comando: "+request+" Datos: "+date+"\n");
						 	int num = request.length();
						 	date = req.substring(num+1, req.length());
						 	System.out.println("\nComando: "+request+" Datos: "+date+"\n");
					 }
				}
					// if (req.equalsIgnoreCase("cwd")){
						// request = "cwd";
						 
					 	 //pointer = 1;
					 	 //}
					 if (pointer == 0)
						 request = "commands failed\r\n"; 		 
		}
		String[] array = {request , date};
		return array;
	}
	
	public String Response(){
		String response = null;
		return response;
	}
	
	
	
	public void setUser(String user){
		this.user = user;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public String getPass(){
		return this.pass;
	}
	
}
