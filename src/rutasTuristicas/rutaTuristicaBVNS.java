package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase rutaTuristicaBVNS (Basic VNS)
 * Realiza un itinerario de viaje teniendo en cuenta el número de días y las horas máximas
 * diarias de viaje. 
 * Se parte de una solución aleatoria o grasp y se intenta mejorar realizando una busqueda local con cambio en la estructura
 * de vecindad.
 * 
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see problemaRutasTuristicas
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class rutaTuristicaBVNS extends problemaRutasTuristicas {

	//Cambio de estructura hasta 4 elementos, en el entorno.
	/**
	 * Máximo cambio en el entorno permitido, se empezará por k = 1. 
	 */
	private final static int K = 3;

	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int LRC = 3;

	/**
	 * false para aleatorio 1 para grasp
	 */
	private boolean algoritmo; 

	//algoritmo false para aleatorio y true para grasp
	public rutaTuristicaBVNS(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia, boolean algor) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		algoritmo = algor;
		resolverProblema(algor);		
	}

	@Override
	/**
	 * Metodo heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas de forma aleatoria teniendo en cuenta las restricciones
	 * En cada iteración se elige uno de forma aleatoria si no supera el tiempo máximo diario teniendo en cuenta
	 * las inserciones anteriores.
	 * 
	 * Hecho esto, se realiza una estrategia BVNS para optimizar la ruta elegida. Para ello se permitirán elementos de cambio
	 * en la solución con elementos de la vecindad de la forma, eliminar un elemento e introducir uno de la vecindad.
	 * 
	 * Si se genera una solución mejor, se repite el proceso, en el caso de que no se encuentre una solución válida, 
	 * se aumenta la estructura de analisis hasta un máximo de K = 4.
	 */
	public void resolverProblema(boolean Estrategia) {

		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		//Para el conjunto de días
		for(int k = 0; k < getNumDiasEstancia(); k++) {

			System.out.println("\nDia numero " + (k + 1));
			if(getAlgoritmoInicial() == false) {
				solucionAleatoria();
				System.out.println("Solución aleatoria con agitación: ");
			} else {
				solucionGRASP();
				System.out.println("Solución GRASP con agitación: ");
			}

			//Busqueda local agitacion, Mejora?
			ArrayList<Integer> busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));

			if(!getSolucionDiaria().equals(busquedaCambio)) {
				solucionDiaria = new ArrayList<Integer>(busquedaCambio);
			}
			System.out.println("Solucion " + getSolucionDiaria() + " con valor " + calcularValorDiario(getSolucionDiaria()));
			System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
			System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
			
			//Aplicamos BVNS
			int rondaActual = 1;
			int maximoComparaciones = 0;
			while(K <= 4 || (maximoComparaciones < 50)) {
				ArrayList<Integer> solucionCandidata = new ArrayList<Integer>(aplicarBVNS(getSolucionDiaria(), getLugaresVisitados(), rondaActual));
				if(calcularValorDiario(getSolucionDiaria()) > calcularValorDiario(solucionCandidata)) {
					System.out.println("\n\nSE CAMBIA LA SOLUCION Y SE VUELVE A EMPEZAR\n");
					solucionDiaria = new ArrayList<Integer>(solucionCandidata);
					rondaActual = 1;
					System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
					System.out.println("Agitacion hecha " + solucionCandidata);
					System.out.println("Valor actual " + calcularValorDiario(solucionCandidata));
					System.out.println("Ronda num: " + rondaActual);
				} else {
					rondaActual++;
					maximoComparaciones++;
					if(rondaActual <= K) {
						//System.out.println("Ronda num: " + rondaActual);
						//System.out.println("DSFGFDSSD");
						//aplicarBVNS(getSolucionDiaria(), getLugaresVisitados(), rondaActual);
					} else {
						break;
					}
				}
			}
			System.out.println("Terminado el VNS ");
			getLugaresVisitados().add(getSolucionDiaria());
		}
		System.out.println("\n-----------------------------------------");
		mostrarItinerarioViaje();
		System.out.println(getLugaresVisitados());
		System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");
	}

	/**
	 * Aplica una busqueda vns teniendo
	 * @param solucionDiariaInicial Solucion actual
	 * @param diasAnteriores Solucion de los dias anteriores si los hubiera
	 * @param ronda	K actual
	 * @return solucion encontrada
	 */
	public ArrayList<Integer> aplicarBVNS(ArrayList<Integer> solucionDiariaInicial, ArrayList<ArrayList<Integer>> diasAnteriores, int ronda) {

		int rondaActual = ronda;
		System.out.println("\nRonda actual: " + rondaActual);
		System.out.println("BVNS básico: Máximo de cambio de entorno = " + K);

		System.out.println("Valor actual " + calcularValorDiario(solucionDiariaInicial));
		System.out.println("Tiempo actual " + calcularTiempoEmpleado(solucionDiariaInicial));
		System.out.println("Kilometros actual " + calcularKilometrosEmpleado(solucionDiariaInicial));
		System.out.println("Visita actual " + solucionDiariaInicial);

		ArrayList<Integer> copiaDia = new ArrayList<Integer>(solucionDiariaInicial);

		//Si se puede eliminar K elementos en la ronda
		if((solucionDiariaInicial.size() - 2) > rondaActual) {
			copiaDia = new ArrayList<Integer>(solucionDiariaInicial);

			for(int i = 0; i < rondaActual; i++) {
				int elegido = (int)(Math.random() * (copiaDia.size() - 2)) + 1;
				System.out.println("Elegido " + elegido + " " + copiaDia.get(elegido));
				copiaDia.remove(elegido);
			}

			System.out.println("Iteracion ya borrados " + copiaDia);
			boolean encontrado = false;
			ArrayList<Integer> copiaEliminados = new ArrayList<Integer>(copiaDia);
			int numComparaciones = 0;
			while((encontrado == false) && (numComparaciones < 10)) {

				//Introducimos rondaActual elementos aleatorios y agitamos para ordenarlos de forma optima
				while(copiaDia.size() < solucionDiariaInicial.size()) {
					int elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());
					//Si no ha sido visitado ni los dias anteriores ni el actual
					if(!yaVisitado(elegido, diasAnteriores, solucionDiariaInicial) ) {
						copiaDia.add(1, elegido);
						//System.out.println("Se añadio: " + elegido);
					} else {
						System.out.println("Problema en " + elegido);
						System.out.println(Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(solucionDiariaInicial)));
					}

				}
				if((calcularTiempoEmpleado(copiaDia) < getNumHorasDiarias() * 60) && (Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(solucionDiariaInicial))) < 30) {
					encontrado = true;
					System.out.println("Encontrado " + calcularTiempoEmpleado(copiaDia));

				} else {
					//System.out.println("Se borro");
					copiaDia = new ArrayList<Integer>(copiaEliminados);
					numComparaciones++;
				}
			}

			//Si no se ha encontrado, se devuelve el original
			if(numComparaciones == 10) {
				System.out.println("No encontrada una alternativa");
				return getSolucionDiaria();
			}
			System.out.println("Visita actual " + getSolucionDiaria());
			System.out.println("Visita probable " + copiaDia);
			System.out.println("Valor probable " + calcularValorDiario(copiaDia));

			System.out.println("\nAplicando Mejora basada en agitación sobre la solución ");
			ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocalCambioVisita(copiaDia));
			if(!copiaDia.equals(busqueda)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				System.out.println("Agitacion hecha " + copiaDia);
				System.out.println("Valor actual " + calcularValorDiario(busqueda));
				copiaDia = new ArrayList<Integer>(busqueda);
			}
			return copiaDia;

		} else {
			System.out.println("No se puede seguir a la siguiente ronda de K");
			System.out.println("K = " + K);
			return getSolucionDiaria();
		}
	}

	/**
	 * Método que devuelve que algoritmo inicial se aplica, aleatorio y grasp
	 * @return boolean
	 */
	public boolean getAlgoritmoInicial() {
		return algoritmo;
	}
}