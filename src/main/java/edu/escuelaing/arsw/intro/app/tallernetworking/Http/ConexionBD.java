package edu.escuelaing.arsw.intro.app.tallernetworking.Http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConexionBD {

	private Connection conn;
	private int size = 0;
	private ResultSet result;
	private ArrayList<ArrayList<String>> records;
	PreparedStatement calculoStatement;

	public ConexionBD() {
		System.out.println("Entra en constructor");
		realizaConexion();
		createStatement();
	}

	public static void main(String[] args) {
		// realizaConexion();
		// createStatement();
		// saveRecord("Joshua",2);
	}

	public void realizaConexion() {
		System.out.println("Entrando en realizar conexion!");
		conn = null;

		try {
			//Class.forName("org.postgresql.Driver");
			String user = "fpllxeutrbzagl";
			String passwd = "18631cbb8567db69286f56cff7243069a465870dc32ecff807db4ee76b884ab8";
			String URL = "jdbc:postgresql://ec2-52-204-20-42.compute-1.amazonaws.com:5432/dai9v53g18ehbu?sslmode=require";

			conn = DriverManager.getConnection(URL, user, passwd);
			System.out.println("Se conecto correctamente!");
		} catch (Exception e) {
			System.out.println("Ocurrio un error : " + e);
		}
		System.out.println("La conexión se realizo sin problemas!");
	}

	public void createStatement() {

		calculoStatement = null;
		String query = "SELECT * FROM RECORDS ORDER BY score DESC";

		try {
			calculoStatement = conn.prepareStatement(query);
			result = calculoStatement.executeQuery();
			records = new ArrayList<ArrayList<String>>();

			records = getResults();
			size = records.size();
			// System.out.println("Records: " + records.toString());
			// calculoStatement.close();
			// result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<ArrayList<String>> getRecords() {
		closeConnection();
		return records;
	}

	public void closeConnection() {
		try {
			calculoStatement.close();
			result.close();
		} catch (SQLException e) {
			System.out.println("Error tratando de cerrar!");
			e.printStackTrace();
		}

	}

	public ArrayList<ArrayList<String>> getResults() {

		int id, score,contador=1;
		String nickname;		
		ArrayList<String> auxiliar;

		try {
			while (result.next()) {
				auxiliar = new ArrayList<String>();
				id = result.getInt(1);				
				auxiliar.add(contador + "");
				auxiliar.add(result.getString(2));
				auxiliar.add(result.getInt(3) + "");
				records.add(auxiliar);
				contador++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("NO QUISO CALCULAR EL RESULT EL ALTO HDP!!!" + e);
			e.printStackTrace();
		}
		// System.out.println("records a enviar: "+records.size());

		return records;
	}

	public void saveRecord(String nickname, int record) {

		PreparedStatement calculoStatement = null;
		String query = "SELECT * FROM RECORDS";
		ResultSet result;
		try {
			calculoStatement = conn.prepareStatement(query);
			result = calculoStatement.executeQuery();
			int id = getResults().size();

			PreparedStatement insertProductos = null;
			String insertar = "INSERT INTO RECORDS VALUES(?,?,?)";
			insertProductos = conn.prepareStatement(insertar);
			insertProductos.setInt(1, id + 1);
			insertProductos.setString(2, nickname);
			insertProductos.setInt(3, record);
			insertProductos.execute();
			createStatement();
			// conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}