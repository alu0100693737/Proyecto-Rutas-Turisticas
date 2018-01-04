package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class rutaTuristicaMultiArranque {
	
	public static int ITERACIONES = 10;
	//Aplicamos MultiArranque con busqueda local 5 veces y nos quedamos con el mejor. Algoritmo Hibrido
	
	public rutaTuristicaMultiArranque(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		resolverEstrategiaMultiArranque(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
	}
	
	public void resolverEstrategiaMultiArranque(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		
		ArrayList<ArrayList<Integer>> solucionFinal = new ArrayList<ArrayList<Integer>>();
		float mejorValor = Float.MAX_VALUE;
		int iteracionElegida = -1;
		
		for(int i = 0; i < ITERACIONES; i++) {
			System.out.println("------------------------------------------");
			System.out.println("ITERACION NUM: " + i);
			rutaTuristicaAleatoria aleatorio = new rutaTuristicaAleatoria(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
			
			//Resolvemos sin aplicar Busqueda Local ninguna
			aleatorio.resolverProblema(true);
			
			float valorAcumulado = 0;
			for(int j = 0; j < aleatorio.getLugaresVisitados().size(); j++) {
				valorAcumulado += aleatorio.calcularValorDiario(aleatorio.getLugaresVisitados().get(j));
			}
			
			if(mejorValor > valorAcumulado) {
				System.out.println("Mejor solución en: " + i);
				iteracionElegida = i;
				mejorValor = valorAcumulado;
				solucionFinal = new ArrayList<ArrayList<Integer>>(aleatorio.getLugaresVisitados());
			}
		}
		
		System.out.println("La mejor solucion es: ");
		for(int i = 0; i < solucionFinal.size(); i++) {
			System.out.println(solucionFinal.get(i));
		}
		System.out.println("Encontrado en la Iteración " + iteracionElegida);
		System.out.println("Valor acumulado " + mejorValor);
	}
}
