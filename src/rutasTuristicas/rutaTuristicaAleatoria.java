package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;

public class rutaTuristicaAleatoria extends problemaRutasTuristicas {

	public rutaTuristicaAleatoria(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		resolverProblema();
	}

	@Override
	public void resolverProblema() {
		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		// TODO Auto-generated method stub	
	}
	
}
