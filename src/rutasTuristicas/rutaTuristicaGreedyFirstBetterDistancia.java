package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
//
//Algoritmos Greedy, Distancias Firs Better. Menor Tiempo en llegar, sin considerar tiempo en la actividad. Solo Distancia
public class rutaTuristicaGreedyFirstBetterDistancia extends problemaRutasTuristicas {

	public rutaTuristicaGreedyFirstBetterDistancia(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema();
	}

	@Override
	public void resolverProblema() {

		solucionDiaria = new ArrayList<Integer>();
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		for(int k = 0; k < getNumDiasEstancia(); k++) {
		}
			
	}
}
