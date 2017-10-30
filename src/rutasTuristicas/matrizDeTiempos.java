package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class matrizDeTiempos {
	private int[][] matrizTiempos;

	public matrizDeTiempos(String ficheroMatrizTiempos, int numLugares)
			throws FileNotFoundException, IOException {

		matrizTiempos = new int[numLugares][numLugares];

		leerMatrizTiempos(ficheroMatrizTiempos, numLugares);
		// mostrarMatrizTiempos();
	}

	public void leerMatrizTiempos(String ficheroMatrizTiempos, int numLugares)
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
			//mostrarMatrizTiempos();
		}
	}

	public void mostrarMatrizTiempos() {
		for (int i = 0; i < getMatrizTiempos().length; ++i) {
			for (int j = 0; j < getMatrizTiempos().length; j++) {
				System.out.print(getMatrizTiempos()[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

	public int[][] getMatrizTiempos() {
		return matrizTiempos;
	}
}