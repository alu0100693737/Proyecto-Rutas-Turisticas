package rutasTuristicas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class matrizDeDistancias {
	private ArrayList<ArrayList<Float>> matrizDistancias;

	public matrizDeDistancias(String ficheroMatrizDistancias) {
		matrizDistancias = new ArrayList<ArrayList<Float>>();
		leerMatrizDistancias(ficheroMatrizDistancias);
		mostrarMatrizDistancias();
	}

	public void leerMatrizDistancias(String ficheroMatrizDistancias) {
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(ficheroMatrizDistancias);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] auxiliar = sCurrentLine.split(" ");
				ArrayList<Float> distanciaLugar = new ArrayList<Float>();
				for(int i = 0; i < auxiliar.length; i++) 
					distanciaLugar.add(Float.parseFloat(auxiliar[i]));
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
	public void mostrarMatrizDistancias() {
		for(int i = 0; i < getMatrizDistancias().size(); ++i)
			for(int j = getMatrizDistancias().get(0).size(); j > 0; --j) {
				System.out.print(getMatrizDistancias().get(i).get(j) + " ");
			}	
	}
	public ArrayList<ArrayList<Float>> getMatrizDistancias() {
	return matrizDistancias;
}
}
