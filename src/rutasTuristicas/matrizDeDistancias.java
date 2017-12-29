package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class matrizDeDistancias {
	private float[][] matrizDistancia;

	public matrizDeDistancias(String ficheroMatrizDistancias, int numLugares)
			throws FileNotFoundException, IOException {

		matrizDistancia = new float[numLugares][numLugares];

		leerMatrizDistancias(ficheroMatrizDistancias, numLugares);
		// mostrarMatrizDistancias();
	}

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
			//mostrarMatrizDistancias();
		}
	}

	public void mostrarMatrizDistancias() {
		for (int i = 0; i < getMatrizDistancias().length; ++i) {
			for (int j = 0; j < getMatrizDistancias().length; j++) {
				System.out.print(getMatrizDistancias()[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	public float[][] getMatrizDistancias() {
		return matrizDistancia;
	}
}
