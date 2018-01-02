package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase matrizdeTiempos que almacena los tiempos
 * que se tarda en trasladarse de un lugar turistico a otro. Es Bidireccional. 
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class matrizDeTiempos {
	
	/**Array privado que almacena los tiempos en llegar de un lugar
	 * turístico a otro
	 */
	private int[][] matrizTiempos;

	/**
	 * Constructor de la clase matrizDeTiempos
	 * @param ficheroMatrizTiempos 		Fichero para la lectura de los datos
	 * @param numLugares				Número de lugares disponibles
	 * @throws FileNotFoundException	Error, que no se encuentre el fichero
	 * @throws IOException				Error de entrada/salida
	 */
	public matrizDeTiempos(String ficheroMatrizTiempos, int numLugares)
			throws FileNotFoundException, IOException {

		matrizTiempos = new int[numLugares][numLugares];

		leerMatrizTiempos(ficheroMatrizTiempos, numLugares);
	}

	/**
	 * Lee todos los tiempos en llegar de lugar a lugar y los almacena en el array matrizTiempos
	 * @param ficheroMatrizTiempos		Fichero para la lectura de datos
	 * @param numLugares				Número de lugares disponibles
	 * @throws FileNotFoundException	Error, que no se encuentre el fichero
	 * @throws IOException				Error de entrada/salida
	 */
	private void leerMatrizTiempos(String ficheroMatrizTiempos, int numLugares)
			throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(ficheroMatrizTiempos))) {
			
			String line = br.readLine();
			for (int i = 0; i < numLugares; i++) {
				String[] auxiliar = line.split("\\s+");
				int contador = 0;
				for (int j = i; j < numLugares; j++) {
					getMatrizTiempos()[i][j] = Integer.parseInt(auxiliar[contador]);
					getMatrizTiempos()[j][i] = Integer.parseInt(auxiliar[contador]);
					contador++;
				}
				line = br.readLine();
			}
		}
	}

	/**
	 * Método para mostrar por pantalla la matriz de Tiempos
	 */
	public void mostrarMatrizTiempos() {
		for (int i = 0; i < getMatrizTiempos().length; ++i) {
			for (int j = 0; j < getMatrizTiempos().length; j++) {
				System.out.print(getMatrizTiempos()[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	///GETS
	
	/**
	 * Método que retorna el array matrizTiempos
	 * @return matrizTiempos
	 */
	public int[][] getMatrizTiempos() {
		return matrizTiempos;
	}
}