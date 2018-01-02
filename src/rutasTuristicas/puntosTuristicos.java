package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase puntosTuristicos que almacena el conjunto de Lugares disponibles
 * para la resolución del problema, la matriz de tiempos necesarios para llegar de
 * uno a otro y la matriz de distancias entre ellos
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class puntosTuristicos {
	
	/**
	 * Numero de Lugares disponibles
	 */
	private int numLugares;
	/**
	 * ArrayList de tipo Lugar que almacena información relevante de los lugares disponibles
	 */
	private ArrayList<Lugar> lugaresTuristicos;
	/**
	 * Matriz de tiempos en minutos, cuánto se tarda en ir de un sitio a otro
	 */
	private matrizDeTiempos matrizTiempos;
	/**
	 * Matriz de distancias en km, cuánta distancia hay de un sitio a otro
	 */
	private matrizDeDistancias matrizDistancias;

	/**
	 * Constructor de la Clase puntosTuristicos
	 * @param ficheroLugares			Fichero que almacena la información sobre los lugares
	 * @param ficheroMatrizDistancias	Fichero que almacena las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero que almacena el tiempo entre todos los lugares
	 * @throws FileNotFoundException	Fichero no encontrado
	 * @throws IOException				Error entrada/salida
	 */
	public puntosTuristicos(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos) throws FileNotFoundException, IOException {
		lugaresTuristicos = new ArrayList<Lugar>();
		leerFicheroPuntosTuristicos(ficheroLugares); 
		
		matrizDistancias = new matrizDeDistancias(ficheroMatrizDistancias, getNumLugares());
		matrizTiempos = new matrizDeTiempos(ficheroMatrizTiempos, getNumLugares());
	}

	/**
	 * Método para leer ficheroLugares, información relevante de lugares (Nombre, Temática, Puntuación y Duración)
	 * @param fichero ficheroLugares
	 */
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

	/**
	 * Método para mostrar la información relevante de un lugar turístico
	 * @param i, lugar deseado para mostrar
	 */
	public void mostrarInfoLugarTuristico(int i) {
		getLugaresTuristicos().get(i).mostrarLugar();
		System.out.println("Distancia al resto de lugares(km): ");
		for(int j = 0; j < getNumLugares(); j++) {
			System.out.println(getLugaresTuristicos().get(j).getNombreLugar() + " " + getMatrizDistancias().getMatrizDistancias()[i][j] + "km " + getMatrizTiempos().getMatrizTiempos()[i][j] + "min");
		}
	}
	
	/**
	 * Método para mostrar toda la información relevante de los lugares disponibles
	 */
	public void mostrarLugaresTuristicos() {
		for(int i = 0; i < getLugaresTuristicos().size(); i++) {
			System.out.print("Lugar " + i + " ");
			getLugaresTuristicos().get(i).mostrarLugar();
		}
	}

	///Métodos GETS
	
	/**
	 * Método que devuelve lugaresTurísticos 
	 * @return lugaresTuristicos ArrayList de tipo Lugar
	 */
	public ArrayList<Lugar> getLugaresTuristicos() {
		return lugaresTuristicos;
	}

	/**
	 * Método que devuelve la matriz de tiempos
	 * @return matrizTiempos matrizDeTiempos
	 */
	public matrizDeTiempos getMatrizTiempos() {
		return matrizTiempos;
	}

	/**
	 * Método que devuelve la matriz de distancias
	 * @return matrizDistancias matrizDeDistancias
	 */
	public matrizDeDistancias getMatrizDistancias() {
		return matrizDistancias;
	}
	
	/**
	 * Método que devuelve el número de lugares disponibles
	 * @return int lugares
	 */
	public int getNumLugares() {
		return numLugares;
	}
}
