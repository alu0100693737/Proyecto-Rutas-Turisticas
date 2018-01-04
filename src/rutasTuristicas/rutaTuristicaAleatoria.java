package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase rutaTuristicaAleatoria
 * Realiza un itinerario de viaje teniendo en cuenta el número de días y las horas máximas
 * diarias de viaje. 
 * Elige aleatoriamente rutas de forma iterativa
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see problemaRutasTuristicas
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class rutaTuristicaAleatoria extends problemaRutasTuristicas {

	/**
	 * Constructor de la clase rutaTuristica Aleatoria
	 * @param ficheroLugares			Fichero con la descripcion de los lugares
	 * @param ficheroMatrizDistancias	Fichero con las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero con los tiempos para llegar de un lugar a otro
	 * @param numDias					Número de días del itinerario
	 * @param numHorasDia				Número de horas diarias del itinerario
	 * @throws FileNotFoundException	Error, fichero no valido
	 * @throws IOException				Error de entrada/salida
	 */
	public rutaTuristicaAleatoria(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
	}

	@Override
	/**
	 * Metodo heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas de forma aleatoria teniendo en cuenta las restricciones
	 * En cada iteración se elige uno de forma aleatoria si no supera el tiempo máximo diario teniendo en cuenta
	 * las inserciones anteriores
	 */
	public void resolverProblema(boolean MultiArranque) {

		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		boolean busquedalocal2a1, busquedalocal1a1;
		if(MultiArranque == false ) {
			System.out.println("¿Desea aplicar busqueda local 2 a 1?, true, false");
			Scanner n = new Scanner(System.in);
			busquedalocal2a1 = n.nextBoolean();

			System.out.println("¿Desea aplicar busqueda local 1 a 1?, true, false");
			busquedalocal1a1 = n.nextBoolean();
		} else {
			//Estrategia MultiArranque
			busquedalocal2a1 = true;
			busquedalocal1a1 = false;
		}

		for(int k = 0; k < getNumDiasEstancia(); k++) {
			solucionDiaria = new ArrayList<Integer>();
			int minutosAcumulados = 0;
			//Maximo de comparaciones para decidir que no se puede introducir ningun sitio mas sin sobrepasar la restriccion de tiempo
			int maximoComparaciones = 0; 			
			//Añadimos el primer elemento, de donde partimos
			getSolucionDiaria().add(0);
			System.out.println("Dia numero " + (k + 1));

			//Maximo de comparaciones posibles
			while (maximoComparaciones < 56) {
				int elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());

				//Si aun no se ha visitado el lugar
				if(yaVisitado(elegido, getLugaresVisitados(), getSolucionDiaria()) == false) {
					//Con el tiempo que nos queda, si sumamos el tiempo en llegar alli, la duracion de la actividad y cuanto tardamos en volver a 0 si lo elegimos, es menor que la hora maxima
					if((minutosAcumulados + 
							getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + 
							(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60) +  
							getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[elegido][0]) 
							< (getNumHorasDiarias() * 60)) {

						System.out.println("\nSe añade: " + elegido + " " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getNombreLugar());
						System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + " minutos");
						System.out.println("La actividad tiene una duracion de " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60 + " minutos");
						System.out.println("Kilometros hechos para llegar : " + getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + " km");

						//Reseteamos las comparaciones
						maximoComparaciones = 0;
						minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60);

						getSolucionDiaria().add(elegido);
						//System.out.println("Acumulado " + minutosAcumulados + " minutos");
					}
					maximoComparaciones++;
				}
			}

			getSolucionDiaria().add(0);

			System.out.println("Solución aleatoria: " + getSolucionDiaria());
			System.out.println("Valor actual " + calcularValorDiario(getSolucionDiaria()));
			System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
			System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
			System.out.println("Visita actual " + getSolucionDiaria());
			
			//Busqueda local, Mejora?
			System.out.println("\nAplicando Mejora basada en agitación sobre la solución ");
			ArrayList<Integer> busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));
			if(!getSolucionDiaria().equals(busquedaCambio)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				solucionDiaria = new ArrayList<Integer>(busquedaCambio);
				for(int i = 0; i < getSolucionDiaria().size(); i++) 
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();

			}
			if(busquedalocal2a1) {
				//Aplicar busqueda local
				System.out.println("\nAplicando busqueda local 2 a 1");
				boolean mejora = true;
				while(mejora) {
					ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocal2a1(getSolucionDiaria(), getLugaresVisitados()));
					if(!busqueda.equals(getSolucionDiaria())) {
						solucionDiaria = new ArrayList<Integer>(busqueda);
					} else {
						mejora = false;
					}
				}
				System.out.println("\nTerminada la busqueda 2 a 1");
			}

			if(busquedalocal1a1) {
				System.out.println("\nAplicando busqueda local 1 a 1");

				boolean mejora = true;
				while(mejora) {
					ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocal1a1(getSolucionDiaria(), getLugaresVisitados()));
					if(!busqueda.equals(getSolucionDiaria())) {
						solucionDiaria = new ArrayList<Integer>(busqueda);
					} else {
						mejora = false;
					}
				}
			}


			System.out.println("\n\nResumen dia " + (k + 1) + " :");
			mostrarConsultaItinerarioDia(getSolucionDiaria());

			//Añadimos 
			getLugaresVisitados().add(getSolucionDiaria());
		}

		System.out.println("\n-----------------------------------------");

		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += calcularValorDiario(getLugaresVisitados().get(i));
			System.out.println(getLugaresVisitados().get(i));
		}
		System.out.println("\nValor total del viaje: " + valorTotalViaje);
	}
}
