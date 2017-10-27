package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class matrizDeTiempos {
	private ArrayList<ArrayList<Integer>> matrizTiempos;

	public matrizDeTiempos(String ficheroMatrizTiempos) throws FileNotFoundException, IOException {
		matrizTiempos = new ArrayList<ArrayList<Integer>>();
		leerMatrizTiempos(ficheroMatrizTiempos);
		mostrarMatrizTiempos();
	}

	public void leerMatrizTiempos(String ficheroMatrizTiempos) throws FileNotFoundException, IOException {
		

		
		try(BufferedReader br = new BufferedReader(new FileReader(ficheroMatrizTiempos))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	ArrayList<Integer> distanciaLugar = new ArrayList<Integer>();
		    	String[] auxiliar = line.split("\\s+");
		    	for(int i = 0; i < auxiliar.length; i++) {
					//System.out.print(auxiliar[i] + " ") ;
					distanciaLugar.add(Integer.parseInt(auxiliar[i]));
				}
		    	getMatrizTiempos().add(distanciaLugar);
		    	line = br.readLine();
		    }
		}
}
	public void mostrarMatrizTiempos() {
		for(int i = 0; i < getMatrizTiempos().size(); ++i) {
			for(int j = 0; j < getMatrizTiempos().get(i).size(); j++) {
				System.out.print(getMatrizTiempos().get(i).get(j) + " ");
			}	
		System.out.print("\n");
		}
	}
	public ArrayList<ArrayList<Integer>> getMatrizTiempos() {
	return matrizTiempos;
}

}
