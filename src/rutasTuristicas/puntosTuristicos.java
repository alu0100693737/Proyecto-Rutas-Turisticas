package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class puntosTuristicos {
	private static final String FILENAME = "Lugares.txt";
	private int numeroDiasEstancia;
	private int numHorasDiarias;
	
	private ArrayList<Lugar> lugaresTuristicos;
	private matrizDeTiempos matrizTiempos;
	private matrizDeDistancias matrizDistancias;
	
	public puntosTuristicos(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos) {
		//lugaresTuristicos = new ArrayList<Lugar>();
		matrizDistancias = new matrizDeDistancias(ficheroMatrizDistancias);
		//matrizTiempos = new matrizDeTiempos(ficheroMatrizTiempos);
		leerFicheroPuntosTuristicos(ficheroLugares); 
		
		mostrarLugaresTuristicos();
	}
	
	public void leerFicheroPuntosTuristicos(String fichero) {
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(fichero);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] auxiliar = sCurrentLine.split(",");
				if(auxiliar.length != 4) 
					System.out.println("Error en la linea " + sCurrentLine);
				else {
					getLugaresTuristicos().add(new Lugar(auxiliar[0], auxiliar[1], Float.parseFloat(auxiliar[2]), Float.parseFloat(auxiliar[3])));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void mostrarLugaresTuristicos() {
		for(int i = 0; i < getLugaresTuristicos().size(); i++) 
			getLugaresTuristicos().get(i).mostrarLugar();
	}
	
	public ArrayList<Lugar> getLugaresTuristicos() {
		return lugaresTuristicos;
	}
	
	public matrizDeTiempos getMatrizTiempos() {
		return matrizTiempos;
	}
	
	public matrizDeDistancias getMatrizDistancias() {
		return matrizDistancias;
	}
}
