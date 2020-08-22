package edu.escuelaing.arsw.intro.app.tallernetworking.Http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Class with the responsibility of return HTML5 elements
 * @author Eduard Jimenez
 */
public class Html5Resource {

    /**
     * Write HTML5 components on the clientSocket
     * @param clientSocket
     * @param out
     * @param archivoEncontrado
     * @param type
     * @throws IOException 
     */
    public void writeText(OutputStream clientSocket, PrintWriter out, File archivoEncontrado,String type) throws IOException {
        StringBuilder cadena = new StringBuilder();
        String line = null;
        FileReader prueba = new FileReader(archivoEncontrado);
        BufferedReader reader = new BufferedReader(prueba);
		String outputLine="";
        while ((line = reader.readLine()) != null) {
            cadena.append(line);
        }
		System.out.println("CADENA DE HTML");
		System.out.println(cadena);
		outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: "+type+"\r\n"
                + "\r\n"
                + cadena;
        //out.println("HTTP/1.1 200 OK");
        //out.println("Content-Type: "+type);
        //out.println();
        //out.println(cadena);
		out.println(outputLine);

    }

}
