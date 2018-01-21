package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//https://www.sciencedirect.com/science/article/pii/S0213131514000029
//funcion de probabilidad utilizada

public class rutaTuristicaRecocidoSimulado extends problemaRutasTuristicas {

	//Heuristica por probabilidad,
	//Si mejora se acepta, si no se puede aceptar con una cierta probabilidad. La probabilidad disminuye cada vez más siguiendo un criterio

	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int TEMPERATURA = 25;

	//false para aleatorio 1 para grasp
	private boolean algoritmo; 

	//algoritmo false para aleatorio y true para grasp
	public rutaTuristicaRecocidoSimulado(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia, boolean algor) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		algoritmo = algor;
		resolverProblema(true);		
	}

	@Override
	public void resolverProblema(boolean Estrategia) {

		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

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
			
			System.out.println("\nAplicando Recocido simulado ");

			float enfriamiento = 100;
			while(((TEMPERATURA * enfriamiento) / 100) > 0) {
				System.out.println("\nTEMPERATURA  " + enfriamiento + " " + ((TEMPERATURA * enfriamiento) / 100));
				ArrayList<Integer> recocidoSimulado = new ArrayList<Integer>(aplicarRecocidoSimulado(getSolucionDiaria(), getLugaresVisitados(), ((TEMPERATURA * enfriamiento) / 100)));
				if(!getSolucionDiaria().equals(recocidoSimulado)) {
					System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
					solucionDiaria = new ArrayList<Integer>(recocidoSimulado);
					System.out.println("Visita actual " + getSolucionDiaria());
					System.out.println("Valor actual " + calcularValorDiario(getSolucionDiaria()));
					System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
					System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
					System.out.println("Visita actual " + getSolucionDiaria());
				} 
				enfriamiento -= 5;
			}
			getLugaresVisitados().add(getSolucionDiaria());
		}
		System.out.println("\n-------------------------------------------------------");
		
		mostrarItinerarioViaje();
		System.out.println(getLugaresVisitados());
		System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");
	}

	
	//Elegimos uno aleatoriamente, calculamos la diferencia de valor, si es mejor se asigna, si es peor se asigna con una determinada probabilidad
	//e^Af/T
	//Recocido modificado 1/(a + eAf/T)
	public ArrayList<Integer> aplicarRecocidoSimulado(ArrayList<Integer> visitaDiaria, ArrayList<ArrayList<Integer>> diasAnteriores, float temperatura) {
		System.out.println("Actual " + visitaDiaria + " con valor " + calcularValorDiario(visitaDiaria) + "\n");
		ArrayList<Integer> copiaDia = new ArrayList<Integer>(visitaDiaria);

		int elegidoEliminar = (int)(Math.random() * (copiaDia.size() - 2)) + 1;
		//System.out.println("Elegido " + elegidoEliminar + " " + copiaDia.get(elegidoEliminar));
		copiaDia.remove(elegidoEliminar);

		//Tener en cuenta a que se mantenga razonablemente el mismo tiempo en realizar el itinerario
		ArrayList<Integer> copiaEliminados = new ArrayList<Integer>(copiaDia);
		boolean encontrado = false;
		int numComparaciones = 0;
		while((encontrado == false) && (numComparaciones < 30)) {
			//Introducimos rondaActual elementos aleatorios y agitamos para ordenarlos de forma optima
			while(copiaDia.size() < visitaDiaria.size()) {
				int elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());
				//Si no ha sido visitado ni los dias anteriores ni el actual
				if(!yaVisitado(elegido, diasAnteriores, visitaDiaria) ) {
					copiaDia.add(1, elegido);
					//System.out.println("Se añadio: " + elegido);
				} else {
					//System.out.println("Problema en " + elegido);
					//System.out.println(Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(visitaDiaria)));
				}

			}
			if((calcularTiempoEmpleado(copiaDia) < getNumHorasDiarias() * 60) && (Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(visitaDiaria))) < 30) {
				encontrado = true;
				System.out.println("Encontrado " + calcularTiempoEmpleado(copiaDia));
			} else {
				//System.out.println("Se borro");
				copiaDia = new ArrayList<Integer>(copiaEliminados);
				numComparaciones++;
			}
		}

		//Si no se ha encontrado, se devuelve el original
		if(numComparaciones == 30) {
			System.out.println("No encontrada una alternativa\n");
			return getSolucionDiaria();
		} else {

			System.out.println("\nAplicando Mejora basada en agitación sobre la solución encontrada");
			ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocalCambioVisita(copiaDia));
			if(!copiaDia.equals(busqueda)) {
				System.out.println("Agitacion hecha " + busqueda + " valor " + calcularValorDiario(busqueda));
				//System.out.println("Valor actual " + calcularValorDiario(busqueda));
				copiaDia = new ArrayList<Integer>(busqueda);
			}
			System.out.println("VALOR ORIGINAL " + calcularValorDiario(visitaDiaria));
			if(calcularValorDiario(copiaDia) < calcularValorDiario(visitaDiaria)) {
				System.out.println("Se mejora la solucion de " + calcularValorDiario(visitaDiaria) + " a " + calcularValorDiario(copiaDia));
				System.out.println(copiaDia);
				return copiaDia;
			} else {
				System.out.println("No se mejora");
				//System.out.println("Aplicamos probabilidad");
				float diferenciaValores = Math.abs(calcularValorDiario(copiaDia) - calcularValorDiario(visitaDiaria));
				//System.out.println("La diferencia es de " + diferenciaValores);
				//System.out.println("A " + (diferenciaValores/temperatura) + " E " + Math.E);
				//System.out.println("Probabilidad de " + (2 / (1 + Math.pow(Math.E, (diferenciaValores/temperatura)))));
				System.out.println("PROBABILIDAD " + (Math.pow(Math.E, -(diferenciaValores/temperatura))));
				if((Math.pow(Math.E, -(diferenciaValores/temperatura))) > 0.90) {
					
					System.out.println("CAMBIANDO AUNQUE ES PEOR");
					return copiaDia;
				} else 
					return visitaDiaria;
			}
		}
	}

	public boolean getAlgoritmoInicial() {
		return algoritmo;
	}
}