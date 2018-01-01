/**
 * Clase matrizdeDistancias que almacena las distancias que existe
 * entre todos los lugares turisticos. Es bidireccional
 * 
 * @author Ivan Garcia Campos   
 * @email alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see asignatura "Sistemas Inteligentes e Interacci�n Persona Computador"
 * @see Master en Ingenier�a Inform�tica por la ULL
 */
package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class matrizDeDistancias {
	
	/** Vector privado que almacena las distancias entre todos los lugares turisticos*/
	private float[][] matrizDistancia;

	/** Constructor de la clase
	 * @param ficheroMatrizDistancias fichero que contiene las distancias.
	 * Igual valor en la ida y vuelta entre dos lugares. Bidireccional
	 * @param numLugares Numero de lugares 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public matrizDeDistancias(String ficheroMatrizDistancias, int numLugares)
			throws FileNotFoundException, IOException {

		matrizDistancia = new float[numLugares][numLugares];

		leerMatrizDistancias(ficheroMatrizDistancias, numLugares);
	}

	/**
	 * Lee todos las distancias entre lugares y los almacena en el array matrizDistancias
	 * @param ficheroMatrizDistancias Fichero que contiene las distancias
	 * @param numLugares
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void leerMatrizDistancias(String ficheroMatrizDistancias, int numLugares)
			throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(ficheroMatrizDistancias))) {
			
			String line = br.readLine();
			for (int i = 0; i < numLugares; i++) {
				String[] auxiliar = line.split("\\s+");
				int contador = 0;
				for (int j = i; j < numLugares; j++) {
					getMatrizDistancias()[i][j] = Float.parseFloat(auxiliar[contador]);
					getMatrizDistancias()[j][i] = Float.parseFloat(auxiliar[contador]);
					contador++;
				}
				line = br.readLine();
			}
		}
	}

	/**
	 * Metodo para mostrar por pantalla la matriz de Distancias
	 */
	public void mostrarMatrizDistancias() {
		for (int i = 0; i < getMatrizDistancias().length; ++i) {
			for (int j = 0; j < getMatrizDistancias().length; j++) {
				System.out.print(getMatrizDistancias()[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Metodo que retorna el array matrizDistancias
	 * @return matrizDistancias
	 */
	public float[][] getMatrizDistancias() {
		return matrizDistancia;
	}
}
