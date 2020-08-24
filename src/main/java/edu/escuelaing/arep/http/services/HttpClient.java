package edu.escuelaing.arep.http.services;

import java.io.PrintWriter;

import edu.escuelaing.arep.http.persistence.ConexionBD;

/**
 * Class that implements HttpServer as server to run Java web apps.
 * @author Eduard Jimenez
 *
 */
public class HttpClient {

	private ConexionBD conexion;

	public HttpClient() {	
		connectDB();
	}

	private void connectDB() {
		conexion = new ConexionBD();
	}

	/**
	 * Validates if an URL has set to has a special actions.
	 * @param url to validate
	 * @param out from HttpServer to write the answer to the URL.
	 * @return boolean if is an special URL
	 */
	public boolean containsURL(String url, PrintWriter out) {
		boolean band = false;
		if (url.contains("colorsApp/")) {
			if(url.equals("colorsApp/ranking")) {
				band = true;
			}
			GET(url, out);
		}		
		return band;
	}

	/**
	 * Gets the URL to add new information or actions.
	 * @param url
	 * @param out
	 */
	public void GET(String url, PrintWriter out) {		
		if (url.equals("colorsApp/ranking")) {
			System.out.println("ok");
			configureLinks(url, out);
		}

	}

	/**
	 * Here you configure your routes and define its actions
	 * 
	 * @param url
	 * @param out
	 */
	private void configureLinks(String url, PrintWriter out) {
		if (url.contains("colorsApp/ranking")) {
			String outline = HttpServer.getHeader() + conexion.getResults();
			out.println(outline);			
		}
	}

	public void POST(String res) {
		res = res.substring(5, res.length() - 9);
		String[] aux = res.split("&");
		String[] nickSeparation = aux[0].split("\\?");
		String nickname = nickSeparation[1].substring(9, nickSeparation[1].length());
		String score = aux[1].substring(6, aux[1].length());
		System.out.println("NICKNAME: " + nickname);
		System.out.println("PUNTAJE: " + score);
		conexion.saveRecord(nickname, Integer.parseInt(score));
		System.out.println("Guardo en post Correctamente");

	}

	public void setConnection(ConexionBD conexion) {
		this.conexion = conexion;
	}

}
