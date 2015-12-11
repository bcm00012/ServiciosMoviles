package servidorjava;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;


public class Connection implements Runnable {
	Socket mSocket;	//Variable tipo Socket.
	public static String MSG_WELCOME = "+OK Bienvenido al servidor de FTP\r\nUsuario: \r\n";	//Mensaje de bienvenida.
	
	public Connection(Socket s) {	//Metodo constuctor de la clase socket 
		mSocket = s;
	}
	Methods m= new Methods();
	

	@Override
	public void run() {
		LinkedList<String> patchList=new LinkedList<String> (); // ruta nueva posicion 0, ruta anterior en la posicion 1
		 patchList.add(0, "c:\\ftpserver");
		 patchList.add(1,"");
		 DB db = new DB();
		 String user="";
		 File dir = new File("c:\\ftpserver");
		 if(dir.mkdirs()==true){
		 System.out.println("Directorio creado por primera vez");
		 }
		 
		 //m.DownloadFile(date, dir, patchList);
		
		
		String inputData = null;	//String con el que recibiremos la cadena de texto enviada por el cliente.
		String outputData = "";		//String con el que enviaremos la cadena de texto al cliente.
		int state=0,nmessage=0,tamanio=0;	//Variables que usaremos posteriormente.
		
		
		
		
		if (mSocket != null) {										//Si la variable el distinto de null entramos, si no, 
			try {
				DataOutputStream output = new DataOutputStream(		//Objeto de la clase DataOutputStream con el que mandaremos los Stream.
						mSocket.getOutputStream());
				BufferedReader input = new BufferedReader(			//Objeto de la clase BufferedReader con el que recibimos los Stream de la aplicacion cliente.
						new InputStreamReader(mSocket.getInputStream()));
				
				output.write(MSG_WELCOME.getBytes());				//Mandamos a el cliente el mensaje de bienvenida.
				Message message=new Message();								//Creamos el objeto message de la clase Message a null.
				while((inputData = input.readLine())!=null){		//Bucle while , si la recepccion de datos distinta de nula entramos.
						
			
						//Si el metodo comprobando devuelve 1 es que la peticion del cliente existe y por tanto entramos, s i no devuelve un -ERR
					System.out.println("Esto es lo que entra: "+inputData);
					String[] request_date = message.Request(inputData);
					String request = request_date[0];
					String date = request_date[1];
					
					
					
				
							//String request = message.Request(inputData);
							//System.out.println(request);
					if (request == "quit"){
							state = 3;}
		
							switch(state){
							case -1:
								outputData ="\r\n";
								state = state+1;
								break;//Menu con el que tenemos los distintos estados.
							case 0:	//Estado 0 con el que pedimos el usuario.									
								System.out.println("Caso 0 " + date);
								message.setUser(date);
								outputData="";
								if(request.equalsIgnoreCase("user")){
									System.out.println("111111111111111111111111111111111111111");
									state = state+1;
									outputData = "Password: \r\n";
									
								}else if (request.equalsIgnoreCase("useb")){
									user = date;
									System.out.println("2222222222222222222222222222222222222");

									outputData = "200 OK \r\n";

								}else if (request.equalsIgnoreCase("pasb")){
									System.out.println("333333333333333333333333333333333333333333");

									db.upLogin(user, date);

									outputData = "LOGIN COMPLETADO\r\n";
									
								}
								
								break;
							case 1:														//Estado 1, en el la peticion es la contraseña. En el comprobamos el usuario y la contraseña.
								System.out.println("Caso 1 "+date);
								message.setPass(date);
								
								System.out.println("Lo que le paso a DB "+message.getUser()+" "+ message.getPass());
							    if (db.checkLogin(message.getUser(), message.getPass())){
							    	state = state+1;
							    	outputData ="200 OK\r\n";
//							    	outputData = "Login right\r\n\n Commands available\r\n"+
//							    					"|-------------------------------------------------------|\r\n"+
//							    					"| RETR	  Download a file\t\t\t\t|\r\n"+
//							    					"| STOR	  Upload a file\t\t\t\t\t|\r\n"+
//							    					"| PWD	  Change the directory\t\t\t\t|\r\n"+
//							    					"| LIST    List the folders of the current directory\t|\r\n"+
//							    					"| CWD	  Change the directory\t\t\t\t|\r\n"+
//							    					"| DELETE  Delete the target file\t\t\t|\r\n"+
//							    					"| HELP	  Show the available commands\t\t\t|\r\n"+
//							    					"| QUIT	  Shutdown transport connections\t\t|\r\n"+
//							    					"|-------------------------------------------------------|\r\n";
							    }
							    	else{
								    	state = state-1;
								    	outputData = "500 BAD \r\nUsers: \r\n";
							    	}
									
									
								break;
							case 2:	//Caso 2 En el que tenemos las distintas opciones del servidor.
								System.out.println("Caso 2");
								System.out.println(request);
								//System.out.println(date);

								switch(request){

								case "stor":
									 m.UploadFile(date, dir, patchList);
									 
									 if(m.UpDownIsOk() == true){
										 outputData ="200 OK\r\n";
									 }else{
										 outputData ="500 BAD\r\n";
									 }
								break;
								
								case "retr":
									System.out.println("DATE antes de descargar :"+ date);
									
									System.out.println("\n\n\nENTRA EN EL STORRRRRRRRRRRRRRR\n\n\n");

									//new Thread(new  polla(date, dir, patchList)).start();
								 m.DownloadFile(date, dir, patchList);
								 if(m.UpDownIsOk() == true){
									 outputData ="200 OK\r\n";
								 }else{
									 outputData ="500 BAD\r\n";
								 }
								break;
								case "pwd":
									
								break;
								case "list":
									outputData = message.listFiles(dir)+".\r\n";
								break;
								case "help":
							    	outputData = "Login right\r\n\n Commands available\r\n"+
					    					"|-------------------------------------------------------|\r\n"+
					    					"| RETR	  Download a file\t\t\t\t|\r\n"+
					    					"| STOR	  Upload a file\t\t\t\t\t|\r\n"+
					    					"| PWD	  Change the directory\t\t\t\t|\r\n"+
					    					"| LIST    List the folders of the current directory\t|\r\n"+
					    					"| CWD	  Change the directory\t\t\t\t|\r\n"+
					    					"| DELETE  Delete the target file\t\t\t|\r\n"+
					    					"| HELP	  Show the available commands\t\t\t|\r\n"+
					    					"| QUIT	  Shutdown transport connections\t\t|\r\n"+
					    					"|-------------------------------------------------------|\r\n";
									
								break;
								case "cwd":
									 if(date.equals("..")==false){
											System.out.println("sdftghdfghdfghjdfghdfgh 111 "+dir.getAbsolutePath());

									dir=m.ChangeDirectory(date, dir, patchList);
									System.out.println("sdftghdfghdfghjdfghdfgh 222 "+dir.getAbsolutePath());

									outputData= patchList.get(2);
									}
									 else{
											System.out.println("sdftghdfghdfghjdfghdfgh 333 "+dir.getAbsolutePath());

										 dir=m.PreviusDirectory(dir, patchList);
											System.out.println("sdftghdfghdfghjdfghdfgh 444 "+dir.getAbsolutePath());

											outputData= patchList.get(2);
											System.out.println("sdftghdfghdfghjdfghdfgh 5555 "+dir.getAbsolutePath());

									 }
								break;
								case "dele":
									m.DeleteFile(date, dir, patchList);
									outputData= patchList.get(2);
								break;
								case "path":
									System.out.println("Entra en el comando nuevo");
									outputData= dir.getAbsolutePath()+"\r\n";
								break;
								
								default:
									outputData = request;
								}

								
									break;
							
						case 3:
								inputData=null;									//Ponemos inputData igual a null para salir correctamente del while.
								System.out.println("Servidor [Finalizado]> "	//Mensaje dentro de la consola del servidor informando que la conexion con una ip y puerto a sido finalizada.
								+ mSocket.getInetAddress().toString() + ":"
								+ mSocket.getPort());
								m.closePort();
						break;
						case 4:
							break;
						case 5:
							break;
						default:
							outputData = request;
							}
							
								
									//outputData="-ERR Unknown command\r\n";
									
								if(state!=3){			//Si estado es distinto del estado 3 siendo este el estado de QUIT entramos y enviamos datos a el usuario.
									System.out.println("Envia esto : "+outputData);
									output.write(outputData.getBytes());		////Metodo con el que mandamos los mensajes a el usuario.
									output.flush();
									}
								else{			//si estado es idual a 3 siendo este el estado QUIT entramos y cerramos Socket.					
									outputData = "+OK Ha salido del servidor FTP.\r\n";		//Guardamos en outputData el mensaje de cierre del servidor.
									output.write(outputData.getBytes());				//Mandamos a el usuario el mensaje cierre de la conexion con el servidor.
									input.close();										//Cierre de la recepcion de datos.
									output.close();										//Cierre del envio de datos.
									mSocket.close();
									m.cerrarConexion();//Cierre de la conexion del socket.
									break;
								}								
							}			
				
			} catch (SocketException se) {
				se.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	
	
}
