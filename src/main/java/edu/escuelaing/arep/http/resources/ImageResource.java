package edu.escuelaing.arep.http.resources;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.imageio.ImageIO;

/**
 *
 * @author Eduard Jimenez
 */
public class ImageResource {

	public void drawImage(OutputStream clientSocket, PrintWriter out, String res, File archivoEncontrado)
			throws IOException {

		String outputLine = "";
		if (res.contains("img/")) {

			res = res.substring(4, res.length());
		}
		BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/resources/img/" + res));
		ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
		DataOutputStream writeImg = new DataOutputStream(clientSocket);
		ImageIO.write(image, "PNG", ArrBytes);
		writeImg.writeBytes("HTTP/1.1 200 OK \r\n");
		writeImg.writeBytes("Content-Type: image/png \r\n");
		writeImg.writeBytes("\r\n");
		writeImg.write(ArrBytes.toByteArray());
		

	}
}
