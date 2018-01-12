package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class rutaTuristicaBusquedaTabu extends problemaRutasTuristicas {
	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int LRC = 3;
	private final int NUMSOLUCIONES = 30;
	private ArrayList<Integer> listaTabu; //Mayor que 4 se bloquea 3 tiempos

	//false para aleatorio 1 para grasp
	private boolean algoritmo; 
	//Heuristica por cambio de entorno forzado. Solucion buena y cuando se repita mucho una ciudad, marcamos y estamos 4 turnos sin poder elegirla

	public rutaTuristicaBusquedaTabu(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia, boolean algor) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		algoritmo = algor;
		listaTabu = new ArrayList<Integer>();
		for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
			getListaTabu().add(0);
		}
		resolverProblema(true);		

	}

	@Override
	public void resolverProblema(boolean Estrategia) {
		
		ArrayList<ArrayList<Integer>> solucionFinal = new ArrayList<ArrayList<Integer>>();
		float mejorValor = Float.MAX_VALUE;
		int iteracionElegida = -1;

		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		for(int i = 0; i < NUMSOLUCIONES; i++) {
			lugaresVisitados =  new ArrayList<ArrayList<Integer>>();
			solucionDiaria = new ArrayList<Integer>();

			//Para el conjunto de días
			for(int k = 0; k < getNumDiasEstancia(); k++) {

				System.out.println("Dia numero " + (k + 1));
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

			}
			System.out.println("\n-----------------------------------------");
			mostrarItinerarioViaje();
			System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");
		}
	}

	public ArrayList<Integer> getListaTabu() {
		return listaTabu;
	}

	public boolean getAlgoritmoInicial() {
		return algoritmo;
	}
}
