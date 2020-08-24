package edu.escuelaing.arep.http.services;

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

//import edu.escuelaing.arep.http.persistence.ConexionBD;
import edu.escuelaing.arep.http.resources.Html5Resource;
import edu.escuelaing.arep.http.resources.ImageResource;

public class HttpServer implements Runnable {

	private final Socket clientSocket;
	private ServerSocket serverSocket;
	private HttpClient client;

	public HttpServer(final Socket clientSocket) throws IOException {
		serverSocket = null;
		this.clientSocket = clientSocket;

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
		String inputLine, res = "";
		int contador = 0;
		while ((inputLine = in.readLine()) != null) {
			// System.out.println("Received: " + inputLine);
			if (contador == 0) {
				res = inputLine;
				contador++;
			}

			if (!in.ready()) {
				break;
			}
		}

		getPetitions(res, out);

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
		if (type.equals("GET")) {
			GET(res, out);
		} else if (type.equals("POS")) {
			POST(res, out);
		}
	}

	public void POST(String res, PrintWriter out) throws IOException {
		System.out.println("¡¡¡¡¡¡¡¡¡¡Entrando EN POST!!!!!!!!!!!!: " + res);
		client.POST(res);
	}

	public void GET(String res, PrintWriter out) throws IOException {
		System.out.println("Peticion original: " + res);
		String outputLine = "";
		res = res.substring(5, res.length() - 9);
		File archivoEncontrado = findFile(res);
		System.out.println("Peticion modificada:" + res);
		if (client.containsURL(res, out)) { // Esta forma de poner conficional puede cambiar
			System.out.println("EFECTIVAMENTE EL CLIENTE TIENE EL URL");

		} else if (archivoEncontrado != null) {
			try {
				getRequestFile(archivoEncontrado, out, res, clientSocket);
			} catch (java.io.FileNotFoundException ex) {
				System.out.println("An error was occurred while reading a file: " + ex);
				error(outputLine, res, out);
			}
		} else {
			System.out.println("An error was occurred because NULL FILE:" + res);
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

			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/html");

		} else if (res.contains(".js")) {

			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/javascript");

		} else if (res.contains(".css")) {

			texto.writeText(clientSocket.getOutputStream(), out, archivoEncontrado, "text/css");

		} else {
			error("", res, out);
		}
	}

	/**
	 * Finds file given a name by parameter return null if doesn't exists. static
	 * files should be in the src/main/resources route.
	 *
	 * @param res
	 * @return
	 */
	private File findFile(String res) {
		return new File(System.getProperty("user.dir") + "/src/main/resources/" + res);
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	/**
	 * Returns HTTP Header to be concatenated with your own files or answers.
	 * 
	 * @return String HTTP header.
	 */
	public static String getHeader() {
		return "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n";
	}

	public static void main(String[] args) {
		ExecutorService pool = null;
		int port = getPort();
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Escuchando en el puerto: " + port);
			pool = Executors.newCachedThreadPool();
			HttpClient client = new HttpClient();

			while (true) {
				Socket socket = serverSocket.accept();
				HttpServer req = new HttpServer(socket);
				req.setClient(client);
				pool.execute(req);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
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

		} catch (FileNotFoundException ex) {
			System.err.println("File not found exception while executing thread.");
		} catch (IOException ex) {
			System.err.println("Run exception while executing thread.");
			Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
