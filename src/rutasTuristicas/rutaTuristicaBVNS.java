package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase rutaTuristicaBVNS (Basic VNS)
 * Realiza un itinerario de viaje teniendo en cuenta el número de días y las horas máximas
 * diarias de viaje. 
 * Se parte de una solución aleatoria y se intenta mejorar realizando una busqueda local con cambio en la estructura
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
	private final static int K = 4;

	public rutaTuristicaBVNS(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema(true);		

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

		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		//Para el conjunto de días
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


			//Busqueda local, Mejora?
			System.out.println("\nAplicando Mejora basada en agitación sobre la solución ");
			ArrayList<Integer> busquedaCambio = new ArrayList<Integer>(busquedaLocalCambioVisita(getSolucionDiaria()));
			if(!getSolucionDiaria().equals(busquedaCambio)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				solucionDiaria = new ArrayList<Integer>(busquedaCambio);
				System.out.println("Visita actual " + getSolucionDiaria());
				System.out.println("Valor actual " + calcularValorDiario(getSolucionDiaria()));
				System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
				System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
				System.out.println("Visita actual " + getSolucionDiaria());
			}
			//Aplicamos BVNS

			int rondaActual = 1;
			maximoComparaciones = 0;
			while(K <= 4 || maximoComparaciones < 50) {
				ArrayList<Integer> solucionCandidata = new ArrayList<Integer>(aplicarBVNS(getSolucionDiaria(), getLugaresVisitados(), rondaActual));
				if(calcularValorDiario(getSolucionDiaria()) > calcularValorDiario(solucionCandidata)) {
					System.out.println("\n\nSE CAMBIA LA SOLUCION Y SE VUELVE A EMPEZAR\n");
					solucionDiaria = new ArrayList<Integer>(solucionCandidata);
					rondaActual = 1;
					System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
					System.out.println("Agitacion hecha " + solucionCandidata);
					System.out.println("Valor actual " + calcularValorDiario(solucionCandidata));
					System.out.println("Ronda num: " + rondaActual);
					//aplicarBVNS(getSolucionDiaria(), getLugaresVisitados(), rondaActual);
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

		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += calcularValorDiario(getLugaresVisitados().get(i));
			System.out.println(getLugaresVisitados().get(i));
			System.out.println("Tiempo viaje " + calcularTiempoEmpleado(getLugaresVisitados().get(i)));
		}
		System.out.println("\nValor total del viaje: " + valorTotalViaje);
	}

	//Tiene random cuando se elige cual eliminar
	public ArrayList<Integer> aplicarBVNS(ArrayList<Integer> solucionDiariaInicial, ArrayList<ArrayList<Integer>> diasAnteriores, int ronda) {

		int rondaActual = ronda;
		System.out.println("\nRonda actual: " + rondaActual);
		System.out.println("BVNS básico: Máximo de cambio de entorno = " + K);

		System.out.println("Valor actual " + calcularValorDiario(solucionDiariaInicial));
		System.out.println("Tiempo actual " + calcularTiempoEmpleado(solucionDiariaInicial));
		System.out.println("Kilometros actual " + calcularKilometrosEmpleado(solucionDiariaInicial));
		System.out.println("Visita actual " + solucionDiariaInicial);

		ArrayList<Integer> copiaDia = new ArrayList<Integer>(solucionDiariaInicial);
		float valorAMejorar = calcularValorDiario(solucionDiariaInicial);

		ArrayList<Integer> candidato = new ArrayList<Integer>();
		float valorCandidato = valorAMejorar;

		//int prueba = 0;
		//Si se puede eliminar K elementos en la ronda
		if((solucionDiariaInicial.size() - 2) > rondaActual) {
			//while(prueba < 20) {
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
						System.out.println("Se añadio: " + elegido);
					} else {
						System.out.println("Problema en " + elegido);
						System.out.println(Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(solucionDiariaInicial)));
					}

				}
				if((calcularTiempoEmpleado(copiaDia) < getNumHorasDiarias() * 60) && (Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(solucionDiariaInicial))) < 30) {
					encontrado = true;
					System.out.println("Encontrado " + calcularTiempoEmpleado(copiaDia));
					
				} else {
					System.out.println("Se borro");
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
			//System.out.println("Tiempo actual " + calcularTiempoEmpleado(copiaDia));
			//System.out.println("Kilometros actual " + calcularKilometrosEmpleado(copiaDia));


			System.out.println("\nAplicando Mejora basada en agitación sobre la solución ");
			ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocalCambioVisita(copiaDia));
			if(!copiaDia.equals(busqueda)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				System.out.println("Agitacion hecha " + copiaDia);
				System.out.println("Valor actual " + calcularValorDiario(busqueda));
				copiaDia = new ArrayList<Integer>(busqueda);
				//for(int i = 0; i < getSolucionDiaria().size(); i++) 
				//	getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			}



			return copiaDia;
		} else {
			System.out.println("No se puede seguir a la siguiente ronda de K");
			System.out.println("K = " + K);
			return getSolucionDiaria();
		}
	}
}