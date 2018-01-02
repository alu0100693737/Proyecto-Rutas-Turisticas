package rutasTuristicas;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase rutaTuristicaGreedyPonderado
 * Realiza un itinerario de viaje eligiendo para cada día, EL MEJOR LUGAR
 * de forma iterativa teniendo en cuenta la distancia, la valoración del lugar y las horas máximas permitidas
 * min(Distancia / Valoracion)    ej: 90/2 es mayor que 150/5
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see problemaRutasTuristicas
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class rutaTuristicaGreedyPonderado extends problemaRutasTuristicas {  
	
	/**
	 * Constructor de la clase rutaTuristicaGreedyPonderado
	 * @param ficheroLugares			Fichero con la descripcion de los lugares
	 * @param ficheroMatrizDistancias	Fichero con las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero con los tiempos para llegar de un lugar a otro
	 * @param numDias					Número de días del itinerario
	 * @param numHorasDia				Número de horas diarias del itinerario
	 * @throws FileNotFoundException	Error, fichero no valido
	 * @throws IOException				Error de entrada/salida
	 */
	public rutaTuristicaGreedyPonderado(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema();
	}

	@Override
	/**
	 * Metodo heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas de forma Greedy.
	 * En cada iteración se elige el mejor candidato entre los lugares que puedan visitarse. Nunca se repite un lugar
	 * ya visitado y se tiene en cuenta el tiempo máximo de itinerario por dia:
	 * Para su calculo, se tiene en cuenta cuánto se tarda en llegar de un sitio a otro y la duración de
	 * la actividad. Debe llegar al punto de partida antes de que se cumpla el numHoras Máximo
	 */
	public void resolverProblema() {

		//Introducir factor ponderado
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Algoritmo Greedy Ponderado KM/Valoracion");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		
		//Para el conjunto de dias
		for(int k = 0; k < getNumDiasEstancia(); k++) {
			System.out.println("\nDia: " + (k + 1));
			solucionDiaria = new ArrayList<Integer>();

			getSolucionDiaria().add(0);

			int tiempoAcumulado = 0; //No sobrepasarse del limite
			int posicionEncontrada = 0;
			boolean encontrado = true;

			//BUSCAMOS EL MEJOR CANDIDATO DESDE NUESTRA POSICION ACTUAL
			//Recorremos los lugares que podemos visitar
			while(encontrado) {

				System.out.println("\nBuscando un lugar a visitar\n");
				float valor = Float.MAX_VALUE;

				//Buscamos entre todas las posibilidades
				for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {

					//Si no ha sido visitado
					if(yaVisitado(i, getLugaresVisitados(), getSolucionDiaria()) == false) {

						//Si el valor es mejor que el actual
						if((getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
								getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion()) < valor) {

							//Si se puede introducir por tiempo
							if(((getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][i] + 
									(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getDuracion() * 60) 
									+ getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[i][0]) + 
									tiempoAcumulado) <= (getNumHorasDiarias() * 60)) {
								
								System.out.println("Valor anterior " + valor);
								valor = getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion();
								posicionEncontrada = i;
								System.out.println("Candidato en " + i + " con valor " + valor);
							}
						}
					} 
				}

				//No se ha encontrado ningun valor que pueda insertarse sin superar el tiempo limite diario
				if(valor == Float.MAX_VALUE) {
					System.out.println("Fin del dia. Tiempo: " + (tiempoAcumulado + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][0]));
					encontrado = false;
				} else { //Se ha encontrado un sitio que cumpla con las especificaciones
					
					System.out.println("\nProximo lugar: " + posicionEncontrada + "\n");
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(posicionEncontrada).mostrarLugar();
					
					System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][posicionEncontrada] + " y estaremos en la actividad " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(posicionEncontrada).getDuracion() * 60);
					
					//Añadimos el tiempo que tarda en llegar al sitio y la duracion de la actividad
					tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][posicionEncontrada] + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(posicionEncontrada).getDuracion() * 60;

					System.out.println("Tiempo acumulado " + tiempoAcumulado + " min");
					System.out.println("Valor: " + valor);
					
					//Añadimos el lugar
					getSolucionDiaria().add(posicionEncontrada);
				}
			}
			
			getSolucionDiaria().add(0);
			getLugaresVisitados().add(getSolucionDiaria());

			System.out.println("Valor: " + calcularValorDiario(getSolucionDiaria()));
			System.out.println("Tiempo acumulado " + calcularTiempoEmpleado(getSolucionDiaria()));
			System.out.println("Dia " + (k + 1) + " terminado.\n----------------------------------------------------------------------------------\n");
			System.out.println("Lugares Visitados " + getSolucionDiaria());
		}
		mostrarItinerarioViaje();
	}
	
}