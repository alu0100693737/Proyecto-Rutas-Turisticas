package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase rutaTuristicaGRASPPonderado
 * Realiza un itinerario de viaje eligiendo para cada día, UNO DE LOS MEJORES LUGARES
 * de forma iterativa teniendo en cuenta la distancia, la valoración del lugar y las horas máximas permitidas
 * min(Distancia / Valoracion)    ej: 90/2 es mayor que 150/5
 * 
 * Contiene además 2 búsquedas locales distintas:
 * 
 *  Cambio en el orden de visita de la ruta (método de alteración)
 *  Busqueda Local Eliminar 2, añadir 1 vecino
 *  
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see problemaRutasTuristicas
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class rutaTuristicaGRASPPonderado extends problemaRutasTuristicas {  

	/** 
	 * Número de candidatos para realizar la elección del próximo lugar
	 */
	private final int LRC = 3;

	/**
	 * Mejores lugares candidatos, se almacena en un ArrayList de tipo Point2D con valor,posición (float,int)
	 */
	private ArrayList<Point2D.Float> lugaresCandidatos; //valor, posicion (float, int)


	/**
	 * Constructor de la clase rutaTuristicaGRASPPonderado
	 * @param ficheroLugares			Fichero con la descripcion de los lugares
	 * @param ficheroMatrizDistancias	Fichero con las distancias entre todos los lugares
	 * @param ficheroMatrizTiempos		Fichero con los tiempos para llegar de un lugar a otro
	 * @param numDias					Número de días del itinerario
	 * @param numHorasDia				Número de horas diarias del itinerario
	 * @throws FileNotFoundException	Error, fichero no valido
	 * @throws IOException				Error de entrada/salida
	 */
	public rutaTuristicaGRASPPonderado(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
	}

	@Override
	/**
	 * Metodo heredado de problemaRutaTuristica que resuelve el problema de 
	 * Gestor de Rutas Turísticas de forma GRASP.
	 * En cada iteración se eligen los LRC mejores candidatos entre los lugares que puedan visitarse. Nunca se repite un lugar
	 * ya visitado y se tiene en cuenta el tiempo máximo de itinerario por dia:
	 * Se eligirá aleatoriamente uno de los lugares candidatos.
	 * 
	 * Para el calculo de mejores candidatos, se tiene en cuenta cuánto se tarda en llegar de un sitio a otro y la duración de
	 * la actividad. Debe llegar al punto de partida antes de que se cumpla el numHoras Máximo
	 */
	public void resolverProblema(boolean MultiArranque) {

		boolean busquedalocal2a1, busquedalocal1a1;
		if(MultiArranque == false ) {
			System.out.println("¿Desea aplicar busqueda local 2 a 1?, true, false");
			Scanner n = new Scanner(System.in);
			busquedalocal2a1 = n.nextBoolean();

			System.out.println("¿Desea aplicar busqueda local 1 a 1?, true, false");
			busquedalocal1a1 = n.nextBoolean();
		} else {
			busquedalocal2a1 = false;
			busquedalocal1a1 = false;
		}

		//Introducir factor ponderado
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();
		lugaresCandidatos = new ArrayList<Point2D.Float>();

		System.out.println("Algoritmo Greedy Ponderado KM/Valoracion");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);

		//Para el conjunto de dias
		for(int k = 0; k < getNumDiasEstancia(); k++) {
			System.out.println("\nDia: " + (k + 1));
			solucionDiaria = new ArrayList<Integer>();

			getSolucionDiaria().add(0);
			int tiempoAcumulado = 0;

			//BUSCAMOS EL MEJOR CANDIDATO DESDE NUESTRA POSICION ACTUAL
			//Recorremos los lugares que podemos visitar

			boolean encontrado = true;

			while(encontrado) {
				System.out.println("\nBuscando un lugar a visitar\n");

				//Candidatos a ocupar el siguiente lugar
				getLugaresCandidatos().clear();

				//Buscamos entre todas las posibilidades
				for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
					//Si no ha sido visitado
					if(yaVisitado(i, getLugaresVisitados(), getSolucionDiaria()) == false) {
						//Si se puede introducir por tiempo
						if(((getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][i] + 
								(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getDuracion() * 60) 
								+ getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[i][0]) + 
								tiempoAcumulado) <= (getNumHorasDiarias() * 60)) {
							if(getLugaresCandidatos().size() < LRC) {
								getLugaresCandidatos().add(new Point2D.Float(getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion(), i));

								System.out.println("Valor, posicion " + getLugaresCandidatos().get(getLugaresCandidatos().size() - 1));
							} else {
								double peor = getLugaresCandidatos().get(0).getX();
								double posicionPeor = 0;

								//Buscamos el peor ya introducido y lo comparamos con el nuevo
								for(int a = 1; a < getLugaresCandidatos().size(); a++) {
									if(getLugaresCandidatos().get(a).getX() > peor) {
										peor = getLugaresCandidatos().get(a).getX();
										posicionPeor = a;
									}
								}

								//si el valor que se desea introducir es mejor que el peor ya insertado, borramos el anterior e insertamos el nuevo
								if((getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion()) < peor) {
									System.out.println("Quitando " + peor + " poniendo " + getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
											getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion());

									getLugaresCandidatos().remove((int)posicionPeor);

									getLugaresCandidatos().add(new Point2D.Float(getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
											getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion(), i));
								}
							}
						}
					} 
				}

				//No se ha encontrado ningun valor que pueda insertarse sin superar el tiempo limite diario
				if(getLugaresCandidatos().size() == 0) {
					System.out.println("Fin del dia. Tiempo: " + (tiempoAcumulado + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][0]));
					encontrado = false;
				} else { //Se ha encontrado un sitio que cumpla con las especificaciones

					System.out.println("\nAnalizadas todas las opciones, toca decidir:\n");
					for(int a = 0; a < getLugaresCandidatos().size(); a++) {
						System.out.println("Candidato num " + a + ": " + getLugaresCandidatos().get(a));
					}

					//elegimos uno de forma aleatoria
					int elegido = (int)(Math.random() * getLugaresCandidatos().size());

					System.out.println("Elegido " + elegido);

					System.out.println("\nProximo lugar: " + (int)getLugaresCandidatos().get(elegido).getY() + "\n");
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).mostrarLugar();

					System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)getLugaresCandidatos().get(elegido).getY()] + " y estaremos en la actividad " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).getDuracion() * 60);

					//A�adimos el tiempo que tarda en llegar al sitio
					tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)getLugaresCandidatos().get(elegido).getY()];
					//A�adimos el tiempo que estar� en el sitio
					tiempoAcumulado += getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).getDuracion() * 60;

					System.out.println("Tiempo acumulado " + tiempoAcumulado + " min");
					System.out.println("Valor: " + getLugaresCandidatos().get(elegido).getX());

					//A�adimos el lugar
					getSolucionDiaria().add((int)getLugaresCandidatos().get(elegido).getY());
				}
			}

			getSolucionDiaria().add(0);
			mostrarConsultaItinerarioDia(getSolucionDiaria());

			System.out.println("Aplicando Mejora basada en agitación sobre la solución ");
			if(getSolucionDiaria() != busquedaLocalCambioVisita(getSolucionDiaria())) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				for(int i = 0; i < getSolucionDiaria().size(); i++) 
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			}
			
			if(busquedalocal2a1) {
				//Aplicar busqueda local
				System.out.println("\nAplicando busqueda local 2 a 1");
				boolean mejora = true;
				while(mejora) {
					ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocal2a1(getSolucionDiaria(), getLugaresVisitados()));
					if(!busqueda.equals(getSolucionDiaria())) 
						solucionDiaria = new ArrayList<Integer>(busqueda);
					else 
						mejora = false;
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

			getLugaresVisitados().add(getSolucionDiaria());

			System.out.println("Dia " + (k + 1) + " terminado.\n----------------------------------------------------------------------------------\n");
			System.out.println("Lugares Visitados " + getSolucionDiaria());
		}
		//System.out.println("Tiempo acumulado " + calcularTiempoEmpleado(getLugaresVisitados().get(0)));
		mostrarItinerarioViaje();
		System.out.println("Valor total del viaje: " + calcularValorTotal(getLugaresVisitados()) + "\n");
	}

	/** 
	 * Método que devuelve los lugares candidatos para la visita del próximo lugar
	 * @return ArrayList de tipo Point2D Float (valor, posicion)
	 */
	public ArrayList<Point2D.Float> getLugaresCandidatos() {
		return lugaresCandidatos;
	}
}