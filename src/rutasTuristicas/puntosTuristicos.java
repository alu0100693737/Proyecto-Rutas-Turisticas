package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class puntosTuristicos {
	private int numLugares;

	private ArrayList<Lugar> lugaresTuristicos;
	private matrizDeTiempos matrizTiempos;
	private matrizDeDistancias matrizDistancias;

	public puntosTuristicos(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos) throws FileNotFoundException, IOException {
		lugaresTuristicos = new ArrayList<Lugar>();
		leerFicheroPuntosTuristicos(ficheroLugares); 
		
		matrizDistancias = new matrizDeDistancias(ficheroMatrizDistancias, getNumLugares());
		matrizTiempos = new matrizDeTiempos(ficheroMatrizTiempos, getNumLugares());
		
		//mostrarLugaresTuristicos();
		//mostrarInfoLugarTuristico(2);
	}

	public void leerFicheroPuntosTuristicos(String fichero) {
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fichero);
			br = new BufferedReader(fr);

			String sCurrentLine;
			numLugares = Integer.parseInt(br.readLine());
			System.out.println("EL numero de lugares es " + getNumLugares());
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

	public void mostrarInfoLugarTuristico(int i) {
		getLugaresTuristicos().get(i).mostrarLugar();
		System.out.println("Distancia al resto de lugares(km): ");
		for(int j = 0; j < getNumLugares(); j++) {
			System.out.println(getLugaresTuristicos().get(j).getNombreLugar() + " " + getMatrizDistancias().getMatrizDistancias()[i][j] + "km " + getMatrizTiempos().getMatrizTiempos()[i][j] + "min");
		}
	}
	
	public void mostrarLugaresTuristicos() {
		for(int i = 0; i < getLugaresTuristicos().size(); i++) {
			System.out.print("Lugar " + i + " ");
			getLugaresTuristicos().get(i).mostrarLugar();
		}
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
	
	public int getNumLugares() {
		return numLugares;
	}
}
