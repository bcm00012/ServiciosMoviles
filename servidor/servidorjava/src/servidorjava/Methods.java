package servidorjava;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Methods {

	ServerSocket ss;
	Socket socket;
	boolean controlFlag;

	public Methods() {
		try {
			ss = new ServerSocket(20, 100);
			socket = new Socket();
		} catch (IOException e) {
			
			controlFlag = false;
			e.printStackTrace();
		}

	}

	public void listFiles(File dir) {

		File[] files = dir.listFiles();
		for (int x = 0; x < files.length; x++) {

			System.out.println(files[x].getName());

		}

	}

	public File ChangeDirectory(String newPatch, File dir,
			LinkedList<String> patchList) {

		String patchNewComplete = patchList.get(0) + "\\" + newPatch;
		System.out.println(patchNewComplete);
		patchList.add(0, patchNewComplete);
		String salida = "\r\n";

		dir = new File(patchNewComplete);
		File[] files = dir.listFiles();
		for (int x = 0; x < files.length; x++) {

			System.out.println(files[x].getName());
			salida = salida + files[x].getName() + "\r\n";
		}
		// patchList.add(2,salida);
		patchList.add(2, "200 OK\r\n");
		return dir;
	}

	public File PreviusDirectory(File dir, LinkedList<String> patchList) {

		String currentPatch = dir.getAbsolutePath();
		String mainPatch = "c:\\ftpserver";

		if (currentPatch.equalsIgnoreCase(mainPatch)) {
			patchList.add(2,
					"Error: Usted se encuentra en el directorio raiz\r\n");
		} else {

			int pos = 0;
			for (int i = 0; i < currentPatch.length(); i++) {
				if (currentPatch.charAt(i) == '\\') {
					pos = i;
				}
			}

			String previousPatch = currentPatch.substring(0, pos);
			dir = new File(previousPatch);
			patchList.add(0, previousPatch);
			String salida = "\r\n";
			File[] files = dir.listFiles();
			for (int x = 0; x < files.length; x++) {

				System.out.println(files[x].getName());
				salida = salida + files[x].getName() + "\r\n";
				
			}
			patchList.add(2, "200 OK\r\n");
		}

		return dir;

	}

	public void DeleteFile(String fileToDelete, File dir,
			LinkedList<String> patchList) {

		String currentPatch = dir.getAbsolutePath();
		dir = new File(currentPatch + "\\" + fileToDelete);
		System.out.println(currentPatch);

		if (dir.delete()) {
			System.out.println("El fichero " + fileToDelete
					+ " ha sido borrado correctamente\r\n");
			patchList.add(2, "200 OK\r\n");
		} else {
			System.out.println("El fichero " + fileToDelete
					+ " no se ha podido borrar\r\n");
			patchList.add(2, "500 BAD\r\n");
		}
	}

	public void DownloadFile(String fileDownload, File dir,
			LinkedList<String> patchList) throws IOException {

		controlFlag = true;

		String currentPatch = dir.getAbsolutePath();
		// String
		// currentPatch="E:\\Documents-Windows\\Nueva carpeta\\Aquaerobic\\Taxi - Pitbull Ft Sensato.mp3";
		dir = new File(currentPatch + "\\" + fileDownload);

		new Thread() {
			public void run() {
				try {

					socket = ss.accept();

					System.out.println("puerto usado para el socket de envio de archivos: "
									+ ss.getLocalPort()
									+ " , "
									+ socket.getPort());
					new MultiServer(socket, currentPatch, fileDownload)
							.start();

					// try {
					//
					//
					// in = new FileInputStream(currentPatch+"\\"+fileDownload);
					// //InputStream in = new FileInputStream(currentPatch);
					// } catch (Exception e) {
					// in = new FileInputStream("c:\\ftserver\\error.txt");
					//
					// }
					// OutputStream out = socket.getOutputStream();
					// copy(in, out);
					// out.flush();
					// out.close();
					// in.close();
					//
					// socket.close();
					// ss.close();
					//
					// System.out.println("Termina el envio");

				} catch (IOException e) {
					e.printStackTrace();
					controlFlag = false;
					System.out.println("Falla la descarga DownloadFile 1");
					try {
						socket.close();
					} catch (IOException e1) {
						System.out.println("Falla la descarga DownloadFile 2");

						e1.printStackTrace();
					}

				}
			}
		}.start();

	}

	public void UploadFile(String fileDownload, File dir,
			LinkedList<String> patchList) throws IOException {

		controlFlag = true;
		String currentPatch = dir.getAbsolutePath();
		// String
		// currentPatch="E:\\Documents-Windows\\Nueva carpeta\\Aquaerobic\\Taxi - Pitbull Ft Sensato.mp3";
		dir = new File(currentPatch + "\\" + fileDownload);

		new Thread() {
			public void run() {

				try {
					socket = ss.accept();

					System.out
							.println("puerto usado para el socket de envio de archivos: "
									+ ss.getLocalPort()
									+ " , "
									+ socket.getPort());
					new MultiServer2(socket, currentPatch, fileDownload, ss)
							.start();

					// try {
					//
					//
					// in = new FileInputStream(currentPatch+"\\"+fileDownload);
					// //InputStream in = new FileInputStream(currentPatch);
					// } catch (Exception e) {
					// in = new FileInputStream("c:\\ftserver\\error.txt");
					//
					// }
					// OutputStream out = socket.getOutputStream();
					// copy(in, out);
					// out.flush();
					// out.close();
					// in.close();
					//
					// socket.close();
					// ss.close();
					//
					// System.out.println("Termina el envio");

				} catch (IOException e) {
					e.printStackTrace();
					controlFlag = false;
					System.out.println("Falla la descarga UploadFile 1");
					try {
						socket.close();
					} catch (IOException e1) {
						System.out.println("Falla la descarga UploadFile 2");

						e1.printStackTrace();
					}

				}
			}
		}.start();

	}

	static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}

	public void closePort() {
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean UpDownIsOk() {
		return controlFlag;
	}

	public void cerrarConexion(){
		
		try {
			//ss.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
