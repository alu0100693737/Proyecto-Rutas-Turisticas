package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase rutaTuristicaMultiArranque
 * Realiza un itinerario de viaje utilizando el algoritmo aleatorio y la busqueda local 2 a 1. (Algoritmo Híbrido)
 * Se tiene en cuenta el número de días y las horas máximas diarias de viaje. 
 * 
 * La generación de soluciones tienen una calidad relativa que se mejora ejecutando el algoritmo una cantidad de
 * iteraciones ITERACIONES. La solución será la de aquella ejecución cuyo resultado sea el mejor.
 * Estrategia Multiarranque
 * 
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see problemaRutasTuristicas
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class rutaTuristicaMultiArranque {
	
	/**
	 * Número de veces que se ejecuta el algoritmo aleatoria con búsqueda local 2 a 1
	 */
	public static int ITERACIONES = 10;

	/**
	 * Constructor de la clase rutaTuristicaMultiArranque
	 * @param ficheroLugares			Fichero con la descripcion de los lugares
	 * @param ficheroMatrizDistancias	Fichero con las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero con los tiempos para llegar de un lugar a otro
	 * @param numDias					Número de días del itinerario
	 * @param numHorasDia				Número de horas diarias del itinerario
	 * @throws FileNotFoundException	Error, fichero no valido
	 * @throws IOException				Error de entrada/salida
	 */
	public rutaTuristicaMultiArranque(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		resolverEstrategiaMultiArranque(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
	}

	/**
	 * Metodo que resuelve el problema de Gestor de Rutas Turísticas utilizando MultiArranque
	 * Se repite la ejecución de un algoritmo aleatorio con búsqueda local 2 a 1 (Algoritmo Hibrido) ITERACIONES veces.
	 * Se elige la mejor de las ejecuciones.
	 * Nunca se repite un lugar ya visitado y se tiene en cuenta el tiempo máximo de itinerario por dia:
	 * Para su calculo, se tiene en cuenta cuánto se tarda en llegar de un sitio a otro y la duración de
	 * la actividad. Debe llegar al punto de partida antes de que se cumpla el numHoras Máximo
	 */
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
