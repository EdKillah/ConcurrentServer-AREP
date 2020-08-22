package edu.escuelaing.arsw.intro.app.tallernetworking.Http;

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

    public void drawImage(OutputStream  clientSocket,PrintWriter out ,String res, File archivoEncontrado) throws IOException {
        
        System.out.println("entra?????? ****final: "+archivoEncontrado);
        System.out.println("ME ENCUENTRO EN IMAGEN Y ESTA ES LA RES: "+res);
		String outputLine="";
        if(res.contains("img/")){
            System.out.println("Antes de aortar:"+res);
            res = res.substring(4,res.length());
            System.out.println("Entro en condicion IMG: "+res);
        }
			BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir")+ "/src/main/resources/img/" + res));
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream writeImg = new DataOutputStream(clientSocket);
            ImageIO.write(image, "PNG", ArrBytes);
            writeImg.writeBytes("HTTP/1.1 200 OK \r\n");
            writeImg.writeBytes("Content-Type: image/png \r\n");
            writeImg.writeBytes("\r\n");
            writeImg.write(ArrBytes.toByteArray());
            System.out.println(System.getProperty("user.dir") + res);
        
    }
}
