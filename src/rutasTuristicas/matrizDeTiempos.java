package rutasTuristicas;

import java.util.ArrayList;

public class matrizDeTiempos {
private ArrayList<ArrayList<Integer>> matrizDeTiempos;
	
	public matrizDeTiempos(String ficheroMatrizDeTiempos) {
		matrizDeTiempos = new ArrayList<ArrayList<Integer>>();
	}
	
	public ArrayList<ArrayList<Integer>> getMatrizDistancias() {
		return matrizDeTiempos;
	}

}
