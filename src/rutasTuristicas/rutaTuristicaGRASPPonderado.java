package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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

		resolverProblema();
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
	public void resolverProblema() {

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

			/*
			System.out.println("Aplicando Busqueda Local ");
			if(getSolucionDiaria() != busquedaLocalCambioVisita(getSolucionDiaria())) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				for(int i = 0; i < getSolucionDiaria().size(); i++) 
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			}*/

			//Aplicar busqueda local
			System.out.println("\nAplicando busqueda local");
			busquedaLocal2a1(getSolucionDiaria(), getLugaresVisitados());

			getLugaresVisitados().add(getSolucionDiaria());

			System.out.println("Dia " + (k + 1) + " terminado.\n----------------------------------------------------------------------------------\n");
			System.out.println("Lugares Visitados " + getSolucionDiaria());
		}
		System.out.println("Tiempo acumulado " + calcularTiempoEmpleado(getLugaresVisitados().get(0)));
		mostrarItinerarioViaje();
	}

	public ArrayList<Point2D.Float> getLugaresCandidatos() {
		return lugaresCandidatos;
	}

	public ArrayList<Integer> busquedaLocalCambioVisita(ArrayList<Integer> visitaDiaria) { //ej: [0,6,5,22,0] -> 1.6589228     [0,5,6,22,0] -> 1.5035715  
		System.out.println("Busqueda local de reemplazo");
		//No se puede modificar el elemento 0
		ArrayList<Integer> copiaVisitaDiaria = new ArrayList<Integer>(visitaDiaria);

		float valorActual = calcularValorDiario(visitaDiaria);
		for(int i = 1; i < copiaVisitaDiaria.size() - 2; i++) {
			int posAux = copiaVisitaDiaria.get(i);
			copiaVisitaDiaria.set(i, copiaVisitaDiaria.get(i + 1));
			copiaVisitaDiaria.set(i + 1, posAux);
			if(valorActual > calcularValorDiario(copiaVisitaDiaria)) {
				System.out.println("Valor actual " + valorActual + " Valor nuevo " + calcularValorDiario(copiaVisitaDiaria));
				System.out.println("Se ha encontrado una mejora ");
				System.out.println("Antes: " + visitaDiaria + " Ahora: " + copiaVisitaDiaria);
				visitaDiaria = new ArrayList<Integer>(copiaVisitaDiaria);
			}
		}
		return visitaDiaria;
	}

	public void busquedaLocal2a1(ArrayList<Integer> visitaDiaria, ArrayList<ArrayList<Integer>> yaVisitados) { 
		visitaDiaria = new ArrayList<Integer>();
		visitaDiaria.add(0); visitaDiaria.add(6); visitaDiaria.add(5); visitaDiaria.add(22); visitaDiaria.add(0);
		System.out.println("Busqueda local 2 Lugares a 1");

		System.out.println("Valor actual " + calcularValorDiario(visitaDiaria));
		System.out.println("Visita actual " + visitaDiaria);

		ArrayList<Integer> copiaDia = new ArrayList<Integer>(visitaDiaria);
		float valorAMejorar = calcularValorDiario(visitaDiaria);
		
		ArrayList<Integer> candidato = new ArrayList<Integer>();
		float valorCandidato = valorAMejorar;

		//Todas las combinaciones eliminando la salida y llegada, Hard Rock
		for(int j = 1; j < (visitaDiaria.size() - 1); j++) {
			for(int k = (j + 1); k < (visitaDiaria.size() - 1); k++) {
				//ERROR EN j y k
				copiaDia = new ArrayList<Integer>(visitaDiaria);

				copiaDia.remove(copiaDia.indexOf(visitaDiaria.get(j)));
				System.out.println("Valor actual " + calcularValorDiario(copiaDia));
				copiaDia.remove(copiaDia.indexOf(visitaDiaria.get(k)));
				System.out.println("Valor actual " + calcularValorDiario(copiaDia));
				System.out.println("Hemos borrado " + visitaDiaria.get(j) + " y " + visitaDiaria.get(k));
				System.out.println(copiaDia);
				for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {

					if(yaVisitado(i, getLugaresVisitados(), visitaDiaria) == false) {
						for(int l = 1; l < (copiaDia.size() - 1); l++) {
							copiaDia.add(l, i);
							if(calcularValorDiario(copiaDia) < valorAMejorar) {
								if(valorCandidato > calcularValorDiario(copiaDia)) {
									candidato = new ArrayList<Integer>(copiaDia);
									valorCandidato = calcularValorDiario(copiaDia);
									System.out.println("Posibilidad de cambio en " + i);
								}
							}
							copiaDia.remove(l);
						}
					}
				}
				
				
			}
		}
		if(valorCandidato != valorAMejorar) {
			copiaDia = candidato;
			System.out.println("Solucion " + copiaDia);
			System.out.println("Calculando valor " + calcularValorDiario(copiaDia));
			
		}
	}
}