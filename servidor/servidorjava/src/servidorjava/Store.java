package servidorjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class Store {

	File direc = new File("c:\\ftpserver");

 	public Store(){
		 if(direc.mkdirs()==true){
		 System.out.println("Directorio creado correctamente");
		 }
	listFilesForFolderInOrder( direc);
 	}
 
 
 	public void listFilesForFolderInOrder( File dir) {
		 File[] files = dir.listFiles();
		 	for (int x=0;x<files.length;x++){
		 		System.out.println(files[x].getName());
		 
		 		if (files[x].getName().contains(".txt")){
		 			System.out.println("arvhivo txt encontrado");
		 			tidyStore("C:\\ftpserver\\"+files[x].getName(), "C:\\ftpserver\\text\\"+files[x].getName());
		 		}
		 		if (files[x].getName().contains(".mp3")){
		 			System.out.println("arvhivo mp3 encontrado");
		 			tidyStore("C:\\ftpserver\\"+files[x].getName(), "C:\\ftpserver\\music\\"+files[x].getName());
		 		}
		 		if (files[x].getName().contains(".pdf")){
		 			System.out.println("arvhivo pdf encontrado");
		 			tidyStore("C:\\ftpserver\\"+files[x].getName(), "C:\\ftpserver\\pdf\\"+files[x].getName());
		 		}
		 		if (files[x].getName().contains(".mp4")){
		 			System.out.println("arvhivo mp4 encontrado");
		 			tidyStore("C:\\ftpserver\\"+files[x].getName(), "C:\\ftpserver\\mp4\\"+files[x].getName());
		 		}
		 
		 	}
	 }
 
	 public static void tidyStore(String sourceFile, String destinationFile) {
		 System.out.println("Ruta inicial: " + sourceFile);
		 System.out.println("Ruta final: " + destinationFile);
		
		 try {
			 File inFile = new File(sourceFile);
			 File outFile = new File(destinationFile);
			 FileInputStream in = new FileInputStream(inFile);
			 FileOutputStream out = new FileOutputStream(outFile);
			
			 int c;
			 while ((c = in.read()) != -1)
				out.write(c);		
			 	in.close();
			 	out.close();		
			 	File file = new File(sourceFile);
			 	if (file.exists()) {
			 		file.delete();
			 	}
		 } catch (IOException e) {System.err.println("Hubo un error de entrada/salida!!!");}
	 }
 
 
}