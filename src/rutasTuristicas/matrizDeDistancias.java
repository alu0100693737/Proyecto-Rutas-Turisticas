package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class matrizDeDistancias {
	private ArrayList<ArrayList<Float>> matrizDistancias;

	public matrizDeDistancias(String ficheroMatrizDistancias) throws FileNotFoundException, IOException {
		matrizDistancias = new ArrayList<ArrayList<Float>>();
		leerMatrizDistancias(ficheroMatrizDistancias);
		mostrarMatrizDistancias();
	}

	public void leerMatrizDistancias(String ficheroMatrizDistancias) throws FileNotFoundException, IOException {
		

		
		try(BufferedReader br = new BufferedReader(new FileReader(ficheroMatrizDistancias))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	ArrayList<Float> distanciaLugar = new ArrayList<Float>();
		    	String[] auxiliar = line.split("\\s+");
		    	for(int i = 0; i < auxiliar.length; i++) {
					//System.out.print(auxiliar[i] + " ") ;
					distanciaLugar.add(Float.parseFloat(auxiliar[i]));
				}
		    	getMatrizDistancias().add(distanciaLugar);
		    	line = br.readLine();
		    }
		}
}
	public void mostrarMatrizDistancias() {
		for(int i = 0; i < getMatrizDistancias().size(); ++i) {
			for(int j = 0; j < getMatrizDistancias().get(i).size(); j++) {
				System.out.print(getMatrizDistancias().get(i).get(j) + " ");
			}	
		System.out.print("\n");
		}
	}
	public ArrayList<ArrayList<Float>> getMatrizDistancias() {
	return matrizDistancias;
}
}
