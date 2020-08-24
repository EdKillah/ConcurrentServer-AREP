package edu.escuelaing.arsw.intro.app.tallernetworking.Http;

/**
 * HttpServer class makes possible the connection between any client and a server
 * thanks to concurrency implemented 
 * @author Eduard Jimenez.
 */
import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer implements Runnable {

	private final Socket clientSocket;
	private ServerSocket serverSocket;

	//Esto debe ir en su clas o aqui?
	private ConexionBD conexion;
	
	public HttpServer(final Socket clientSocket) throws IOException {

		serverSocket = null;
		this.clientSocket = clientSocket;
		conexion = new ConexionBD();

	}

	/**
	 * Prepare the connection between server and client
	 * 
	 * @param clientSocket
	 * @throws IOException
	 */
	private void prepareRequest(Socket clientSocket) throws IOException {
		PrintWriter out;
		BufferedReader in;

		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine, res = "";
		int contador = 0;
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Received: " + inputLine + " CONTADOR: "+contador);
			if (contador == 0) {
				res = inputLine;
				getPetitions(res, out);
				contador++;
			}

			if (!in.ready()) {
				break;
			}
		}
		System.out.println("YA SALIO DEL CICLO Y VA A ENVIAR A GETPETITIONS: "+res);
		//getPetitions(res, out);

		out.close();
		in.close();
	}

	/**
	 * Validation of request type getPetitions catch FileNotFound exceptions but
	 * throws any other IOExeption
	 * 
	 * @param res
	 * @param out
	 * @throws IOException
	 */
	private void getPetitions(String res, PrintWriter out) throws IOException {
		
		String type = res.substring(0, 3);
		System.out.println("En get Petitions type: "+type);
		if (type.equals("GET")) {
			GET(res, out);
		} else if(type.equals("POS")) {	
			POST(res,out);
		}
	}
	
	public void POST(String res, PrintWriter out) throws IOException {
		System.out.println("Entrando EN POST!!!!!!!!!!!!");
		//System.out.println("RES: "+res);
		res = res.substring(5, res.length() - 9);
		//System.out.println("Nuevo res: "+res);
		String[] aux = res.split("&");
		//System.out.println("SEPARACION: "+aux[0]+" & "+aux[1]);
		String[] nickSeparation = aux[0].split("\\?");
		String nickname = nickSeparation[1].substring(9,nickSeparation[1].length());
		String score = aux[1].substring(6,aux[1].length()); 
		System.out.println("NICKNAME: "+nickname);
		System.out.println("PUNTAJE: "+score);
		
		
		conexion.saveRecord(nickname, Integer.parseInt(score));
		
	}

	public void GET(String res, PrintWriter out) throws IOException  {

		String outputLine = "";
		res = res.substring(5, res.length() - 9);
		File archivoEncontrado = buscarArchivo(res);
		//ACA DEBEMOS PARTIR EL SERVIDOR DE LA APP!
		System.out.println("################ PETICION:"+res);
		if(res.equals("colorsApp/ranking")) {
			System.out.println("****ESTA BUSCANDO LA LISTA DE RNAKING*******");			
			System.out.println("Listo creo la conexion ahora voy a pedir lista!");
			String outline = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" +conexion.getResults();
			System.out.println("-*-*_*-*-*-*-*_*-*-*-*-*-*-*-*-*-*-*-");
			System.out.println(outline);
			out.println(outline);
			System.out.println("TERMINO DE ENVIAR RANKING!");
		}
		else if (archivoEncontrado != null) {
			try {
				getRequestFile(archivoEncontrado, out, res, clientSocket);
			} catch (java.io.FileNotFoundException ex) {
				System.out.println("Error de leyendo archivo:");
				error(outputLine, res, out);
			}
		} else {
			System.out.println("Error de archivo nulo:" + res);
			error(outputLine, res, out);
		}
	}

	/**
	 * Makes the call to the class depending on the file's type
	 * 
	 * @param archivoEncontrado
	 * @param out
	 * @param res
	 * @param clientSocket
	 * @throws IOException
	 */
	private void getRequestFile(File archivoEncontrado, PrintWriter out, String res, Socket clientSocket)
			throws IOException {

		Html5Resource texto = new Html5Resource();
		
		if (res.contains("png") || res.contains("jpg")) {
			
			ImageResource imgr = new ImageResource();
			imgr.drawImage(clientSocket.getOutputStream(), out, res, archivoEncontrado);
			
		} else if (res.contains("html")) {		
			System.out.println("Va a pedir el archivo html! Archivo: "+archivoEncontrado);
			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/html");
			System.out.println("Encontro el archivo html: "+archivoEncontrado);
		} else if (res.contains(".js")) {
			
			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/javascript");

		} else if (res.contains(".css")) {
			
			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/css");
			
		} else {
			error("", res, out);
		}
	}

	/**
	 * Concats the file name with the root path
	 *
	 * @param res
	 * @return
	 */
	private File buscarArchivo(String res) {
		return new File(System.getProperty("user.dir") + "/src/main/resources/" + res);
	}

	public static void main(String[] args) {
		ExecutorService pool = null;
		try {
			int port = getPort();
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Escuchando en el puerto: " + port);
			pool = Executors.newCachedThreadPool();
			while (true) {
				Socket socket = serverSocket.accept();
				HttpServer req = new HttpServer(socket);
				pool.execute(req);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 8080.");
			System.exit(1);
		} finally {
			pool.shutdown();
		}

	}

	public static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 32000; // returns default port if heroku-port isn't set

	}

	/**
	 * Write an html with an error text.
	 * 
	 * @param outputLine
	 * @param res
	 * @return
	 */
	private void error(String outputLine, String res, PrintWriter out) {

		outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>" + "<html>"
				+ "<head>" + "<meta charset=\"UTF-8\">" + "<title>Title of the document</title>\n" + "</head>"
				+ "<body  style='text-align: center;'>" + "<h1>ERROR 404.<p><div style='color:red'>" + res.toUpperCase()
				+ "</div>" + " NO ENCONTRADO</p></h1>" + "</body>" + "</html>";

		out.println(outputLine);
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			prepareRequest(clientSocket);
			// throw new UnsupportedOperationException("Not supported yet."); //To change
			// body of generated methods, choose Tools | Templates.
		} catch (FileNotFoundException ex) {
			// String outputLine = error("", "Recurso no encontrado");

		} catch (IOException ex) {
			System.err.println("Run exception while executing thread.");
			Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
