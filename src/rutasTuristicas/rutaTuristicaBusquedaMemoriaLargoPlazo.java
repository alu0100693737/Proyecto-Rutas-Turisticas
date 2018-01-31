package rutasTuristicas;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class rutaTuristicaBusquedaMemoriaLargoPlazo extends problemaRutasTuristicas {
	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int NUMSOLUCIONES = 30;
	
	/**
	 * Número máximo de soluciones continuadas que contengan el mismo elemento
	 */
	private final int LIMITEMEMORIALARGOPLAZO = 3;
	/**
	 * Tiempo de espera en la lista Tabu, de 2 a 4
	 */
	private final int TIEMPOMEMORIALARGOPLAZO = 2;
	
	/**
	 * Algoritmo que se ejecutará para la solución inicial, false para aleatorio 1 para grasp
	 */
	private boolean algoritmo; 
	//Heuristica por cambio de entorno forzado. Solucion buena y cuando se repita mucho una ciudad, marcamos y estamos 4 turnos sin poder elegirla

	/**
	 * Constructor de la clase rutaTuristicaBusquedaMemoriaLargoPlazo
	 * @param ficheroLugares			Fichero con la descripcion de los lugares
	 * @param ficheroMatrizDistancias	Fichero con las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero con los tiempos para llegar de un lugar a otro
	 * @param numDias					Número de días del itinerario
	 * @param numHorasDia				Número de horas diarias del itinerario
	 * @param algor						Algoritmo que se ejecutará inicialmente, 0 para aleatorio 1 para grasp
	 * @throws FileNotFoundException	Error, fichero no valido
	 * @throws IOException				Error de entrada/salida
	 */
	public rutaTuristicaBusquedaMemoriaLargoPlazo(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia, boolean algor) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		algoritmo = algor;
		listaTabu = new ArrayList<Point>();
		resolverProblema(true);		
	}

	@Override
	/**
	 * Método heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas utilizando una heurística de búsqueda memoria a largo plazo
	 * Existe un cambio del entorno forzado. Se generan soluciones utilizando algoritmo aleatorio o grasp. Si la solución vecina es mejor
	 * que la actual, actualizamos la solucion.
	 * Cuando se haya repetido LIMITEMEMORIALARGOPLAZO iteraciones un mismo lugar, se introduce en la lista tabú y se
	 * calcula una solución que cumpla los requisitos y mantenga los demás lugares.
	 * El problema acaba cuando se realicen NUMSOLUCIONES iteraciones
	 */
	public void resolverProblema(boolean Estrategia) {
		ArrayList<ArrayList<ArrayList<Integer>>> conjuntosSoluciones = new ArrayList<ArrayList<ArrayList<Integer>>>();

		ArrayList<ArrayList<Integer>> solucionFinal = new ArrayList<ArrayList<Integer>>();
		float mejorValor = Float.MAX_VALUE;
		int iteracionElegida = -1;

		listaTabu = new ArrayList<Point>();

		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		for(int i = 0; i < NUMSOLUCIONES; i++) {
			lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

			System.out.println("\nITERACION NUM " + (i + 1));

			if(!getListaTabu().isEmpty()) {
				System.out.println("LISTA NO VACIA");
				System.out.println(getListaTabu());
				for(int p = 0; p < getListaTabu().size(); p++) {
					if(getListaTabu().get(p).y == 0) {
						System.out.println("SACANDO DE LA LISTA DE BLOQUEADOS EL ELEMENTO " + getListaTabu().get(p).x);
						getListaTabu().remove(p);
						p--;
					} else {
						getListaTabu().get(p).y -= 1;
					}
				}
			}

			//Para el conjunto de días
			for(int k = 0; k < getNumDiasEstancia(); k++) {
				solucionDiaria = new ArrayList<Integer>();
				//System.out.println("Dia numero " + (k + 1));
				if(getAlgoritmoInicial() == false) {
					solucionAleatoria();
					//System.out.println("Solución aleatoria con agitación: ");
				} else {
					solucionGRASP();
					//System.out.println("Solución GRASP con agitación: ");
				}

				//Busqueda local agitacion, Mejora?
				ArrayList<Integer> busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));

				if(!getSolucionDiaria().equals(busquedaCambio)) {
					solucionDiaria = new ArrayList<Integer>(busquedaCambio);
				}
				System.out.println("Solucion " + getSolucionDiaria() + " con valor " + calcularValorDiario(getSolucionDiaria()));
				/*System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
				System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));*/
				getLugaresVisitados().add(getSolucionDiaria());
			}
			
			System.out.println("\n-----------------------------------------");
			//mostrarItinerarioViaje();
			System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");

			float valorAcumulado = calcularValorTotal(getLugaresVisitados());
			if(mejorValor > valorAcumulado) {
				System.out.println("\nMejorando solución en iteracion: " + i + " de " + mejorValor + " a " + valorAcumulado);
				iteracionElegida = i;
				mejorValor = valorAcumulado;
				solucionFinal = new ArrayList<ArrayList<Integer>>(getLugaresVisitados());
				System.out.println(solucionFinal);
			}
			
			conjuntosSoluciones.add(new ArrayList<ArrayList<Integer>>(getLugaresVisitados()));

			if(i >= (LIMITEMEMORIALARGOPLAZO - 1)) {
				System.out.println("\nBusqueda Memoria a Largo Plazo, bloqueo de lugares");
				//System.out.println("Iteracion num " + i);
				System.out.println(conjuntosSoluciones.get(i));
				boolean encontrado = false;
				//Si se repite la ciudad el mismo dia en los ultimos 3 itinerarios, bloqueamos
				for(int k = 1; k < getLugaresTuristicosDisponibles().getNumLugares(); k++) {
					for(int l = 0; l < getNumDiasEstancia(); l++) {
						if(conjuntosSoluciones.get(i).get(l).contains(k)) {
							for(int m = 0; m < getNumDiasEstancia(); m++) {
								if(conjuntosSoluciones.get(i - 1).get(m).contains(k)) {
									for(int n = 0; n < getNumDiasEstancia(); n++) {
										if(conjuntosSoluciones.get(i - 2).get(n).contains(k)) {
											encontrado = true;
											System.out.println("ENCONTRADO " + k + " EN "); 
											System.out.println(conjuntosSoluciones.get(i));
											System.out.println(conjuntosSoluciones.get(i - 1));
											System.out.println(conjuntosSoluciones.get(i - 2));
											//Limite tabu tiempo aleatorio limitado?
											int tiempoEspera =(int)(Math.random() * ((TIEMPOMEMORIALARGOPLAZO + 3) - TIEMPOMEMORIALARGOPLAZO) + TIEMPOMEMORIALARGOPLAZO);
											System.out.println("Tiempo de espera " + tiempoEspera);
											getListaTabu().add(new Point(k, tiempoEspera));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		System.out.println("LA MEJOR SOLUCION ES: ");
		for(int i = 0; i < solucionFinal.size(); i++) {
			System.out.println(solucionFinal.get(i));
			System.out.println("Dia: " + i);
			mostrarConsultaItinerarioDia(solucionFinal.get(i));
		}

		float valorTotalViaje = 0;
		for(int i = 0; i < solucionFinal.size(); i++) {
			valorTotalViaje += calcularValorDiario(solucionFinal.get(i));
			System.out.println(solucionFinal.get(i) + " con valor " + calcularValorDiario(solucionFinal.get(i)));
		}
		
		System.out.println("Mejor iteracion: " + iteracionElegida);
		System.out.println("\nValor total del viaje: " + valorTotalViaje);
		System.out.println(getLugaresVisitados());
		System.out.println("Valor acumulado " + mejorValor);
	}

	/**
	 * Método que devuelve que algoritmo inicial se aplica, aleatorio y grasp
	 * @return boolean
	 */
	public boolean getAlgoritmoInicial() {
		return algoritmo;
	}
}
