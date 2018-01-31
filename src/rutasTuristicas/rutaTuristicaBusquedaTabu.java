package rutasTuristicas;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creamos un asolucion inicial y vamos aplicando busquedas locales 1 a 1 bloqueando las ciudades que permanezcan mas de LIMITE TABU veces
 * @author ivan
 *
 */
public class rutaTuristicaBusquedaTabu extends problemaRutasTuristicas {
	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int NUMITERACIONES = 30;

	private final int LIMITETABU = 3;
	private final int TIEMPOTABU = 2;
	//false para aleatorio 1 para grasp
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
	public rutaTuristicaBusquedaTabu(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia, boolean algor) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		algoritmo = algor;
		listaTabu = new ArrayList<Point>();
		resolverProblema(algor);		
	}

	@Override
	/**
	 * Método heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas utilizando una heurística de búsqueda Tabú
	 * Existe un cambio del entorno forzado. 
	 * Se genera una solucion de inicio que puede ser aleatoria o Grasp.
	 * Se realiza una búsqueda local intentando mejorar la solución. Si se mejora, se cambia.
	 * Cuando en LIMITETABU iteraciones de solución, se repite una ciudad, esta se inserta en la lista Tabú 
	 * durante 2 - 4 tiempos.
	 * De esta manera se busca un agitamiento de la solucion
	 * El problema acaba cuando se realicen NUMSOLUCIONES iteraciones
	 */
	public void resolverProblema(boolean Estrategia) {

		ArrayList<Integer> mejorSolucion = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> solucionesIteracionesDiarias = new ArrayList<ArrayList<Integer>>();

		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		//Para el conjunto de días
		for(int k = 0; k < getNumDiasEstancia(); k++) {
			mejorSolucion = new ArrayList<Integer>();
			System.out.println("Dia numero " + (k + 1));
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
			System.out.println(solucionDiaria + " con valor " + calcularValorDiario(solucionDiaria));
			System.out.println("Aplicando busqueda tabu");
			for(int i = 0; i < NUMITERACIONES; i++) {

				System.out.println("\nITERACION " + i);

				if(!getListaTabu().isEmpty()) {

					for(int p = 0; p < getListaTabu().size(); p++) {
						if(getListaTabu().get(p).y == 0) {
							//System.out.println("SACANDO DE LA LISTA DE BLOQUEADOS EL ELEMENTO " + getListaTabu().get(p).x);
							getListaTabu().remove(p);
						} else {
							getListaTabu().get(p).y -= 1;
						}
					}
					//System.out.println("LISTA NO VACIA");
					//System.out.println(getListaTabu());


					for(int j = 0; j < getListaTabu().size(); j++) {
						if(getSolucionDiaria().contains(getListaTabu().get(j).x)) {
							getSolucionDiaria().remove(getSolucionDiaria().indexOf(getListaTabu().get(j).x));
						}
					}
					//Añadimos uno mas aleatoriamente
					if(getAlgoritmoInicial() == false) {
						solucionParcialAleatoria();
						//System.out.println("Ahora la solucion es " + getSolucionDiaria() + " con " + calcularValorDiario(getSolucionDiaria()));
						//Busqueda local agitacion, Mejora?
						busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));

						if(!getSolucionDiaria().equals(busquedaCambio)) {
							solucionDiaria = new ArrayList<Integer>(busquedaCambio);
						}
					} else { //Elegimos por grasp
						solucionGRASPParcial();
						//Busqueda local agitacion, Mejora?
						busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));

						if(!getSolucionDiaria().equals(busquedaCambio)) {
							solucionDiaria = new ArrayList<Integer>(busquedaCambio);
						}
					}
					//System.out.println("Candidato " + solucionDiaria);
				}



				ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocal1a1(getSolucionDiaria(), getLugaresVisitados()));
				if(!busqueda.equals(getSolucionDiaria())) {
					if(calcularValorDiario(busqueda) < calcularValorDiario(getSolucionDiaria())) {
						//System.out.println("SE HA MEJORADO");
						solucionDiaria = new ArrayList<Integer>(busqueda);
					}
				} 

				if(i >= (LIMITETABU - 1)) {
					//System.out.println("SOLUCIONES ANTERIORES " + solucionesIteracionesDiarias);
					//System.out.println("ITERACION " + i);
					//Los tamaños en las soluciones son el mismo
					for(int j = 1; j < (getSolucionDiaria().size() - 1); j++) {
						if(solucionesIteracionesDiarias.get(solucionesIteracionesDiarias.size() - 1).contains(getSolucionDiaria().get(j))) {
							if(solucionesIteracionesDiarias.get(solucionesIteracionesDiarias.size() - 2).contains(getSolucionDiaria().get(j))) {									
								//System.out.println("\nELEMENTO BLOQUEADO: " + getSolucionDiaria().get(j));
								int tiempoEspera =(int)(Math.random() * ((TIEMPOTABU + 3) - TIEMPOTABU) + TIEMPOTABU);
								//System.out.println("Tiempo de espera " + tiempoEspera);
								getListaTabu().add(new Point(getSolucionDiaria().get(j), tiempoEspera));
							}
						}

					}
				}
				if(mejorSolucion.size() > 0) {
					if(calcularValorDiario(mejorSolucion) > calcularValorDiario(solucionDiaria)) {


						mejorSolucion = new ArrayList<Integer>(solucionDiaria);
						System.out.println("MEJOR SOLUCION ACTUAL: " + solucionDiaria + " con valor " + calcularValorDiario(solucionDiaria));
					}
				} else {
					mejorSolucion = new ArrayList<Integer>(solucionDiaria);
					System.out.println("MEJOR SOLUCION ACTUAL: " + solucionDiaria + " con valor " + calcularValorDiario(solucionDiaria));

				}
				solucionesIteracionesDiarias.add(new ArrayList<Integer>(solucionDiaria));
			}

			getLugaresVisitados().add(mejorSolucion);
		}

		 System.out.println("\n-----------------------------------------");
		 System.out.println(getLugaresVisitados());
		 System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");
	}

	/**
	 * Método que devuelve que algoritmo inicial se aplica, aleatorio y grasp
	 * @return boolean
	 */
	public boolean getAlgoritmoInicial() {
		return algoritmo;
	}
}
