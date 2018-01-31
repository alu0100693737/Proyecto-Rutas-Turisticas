package rutasTuristicas;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase ProblemaRutasTuristicas para la resolución de problemas
 * de asignación de rutas turísticas especificando el 
 * número de días y las horas diarias máximas de la ruta
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public abstract class problemaRutasTuristicas {

	/** 
	 * Número de candidatos para realizar la elección del próximo lugar
	 */
	private final int LRC = 3;

	/** 
	 * Número de Días de la estancia
	 * */
	private int numDiasEstancia_;
	/** 
	 * Número de Horas diarias máximo para la realización de rutas
	 * */
	private int numHorasDiarias_;
	/**
	 * Atributo de tipo puntosTuristicos que almacena todos los lugares disponibles,
	 * matriz de distancias y matriz de tiempos
	 */
	private puntosTuristicos lugaresTuristicosDisponibles_;

	/**
	 * Atributo utilizado para la resolución del problema, solución diaria encontrada
	 */
	protected ArrayList<Integer> solucionDiaria;
	/**
	 * Atributo utilizado para la utilización del problema, itinerario de visita
	 * de todos los dias permitidos
	 */
	protected ArrayList<ArrayList<Integer>> lugaresVisitados;

	protected ArrayList<Point> listaTabu; //Mayor que 4 se bloquea 3 tiempos

	/**
	 * Constructor de la Clase problemaRutasTuristicas
	 * @param ficheroLugares			Fichero que especifica los Lugares permitidos para su visita
	 * @param ficheroMatrizDistancias 	Fichero que especifica la Matriz de Distancias entre todos los Lugares, Bidireccional
	 * @param ficheroMatrizTiempos		Fichero que especifica la Matriz de Tiempos, cuanto se tarda en llegar de un sitio a otro
	 * @param numDias					Número de dias del itinerario de viaje
	 * @param numHorasDia				Número de horas permitidas de viaje diarias
	 * @throws FileNotFoundException	Si los ficheros no han sido especificados
	 * @throws IOException				Error entrada salida
	 */
	public problemaRutasTuristicas(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		lugaresTuristicosDisponibles_ = new puntosTuristicos(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos);
		numDiasEstancia_ = numDias;
		numHorasDiarias_ = numHorasDia;
		listaTabu = new ArrayList<Point>();
	}

	/** 
	 * Método abstracto para la resolución de problemas
	 * Se implementarán:
	 * 		Algoritmo Aleatorio,
	 * 		Algoritmo Greedy,	
	 * 		Algoritmo GRASP,
	 * 		Busqueda Local,
	 * 		Multiarranque,	
	 * 		Recocido Simulado,
	 * 		Busqueda Tabú y 
	 * 		BVNS
	 */
	public abstract void resolverProblema(boolean MultiArranque); //Se usa parametro por si se desea multiarranque, sin usar busquedas locales

	/**
	 * Método de agitación basado en buscar una mejora cambiando el orden de los lugares que se visitan.
	 * Ej: [0, 3, 22, 0] es peor que [0, 22, 3, 0]
	 * @param visitaDiaria			ArrayList de lugares a visitar
	 * @return						Solución en forma ArrayList de Integer
	 */
	public ArrayList<Integer> busquedaLocalCambioVisita(ArrayList<Integer> visitaDiaria) { //ej: [0,6,5,22,0] -> 1.6589228     [0,5,6,22,0] -> 1.5035715  
		//System.out.println("Busqueda local de reemplazo");
		//No se puede modificar el elemento 0
		ArrayList<Integer> copiaVisitaDiaria = new ArrayList<Integer>(visitaDiaria);

		float valorActual = calcularValorDiario(visitaDiaria);
		for(int i = 1; i < copiaVisitaDiaria.size() - 2; i++) {
			int posAux = copiaVisitaDiaria.get(i);
			copiaVisitaDiaria.set(i, copiaVisitaDiaria.get(i + 1));
			copiaVisitaDiaria.set(i + 1, posAux);
			if(valorActual > calcularValorDiario(copiaVisitaDiaria)) {
				System.out.println("Antes: " + visitaDiaria + " Ahora: " + copiaVisitaDiaria);
				System.out.println("Valor actual " + valorActual + " Valor nuevo " + calcularValorDiario(copiaVisitaDiaria));
				System.out.println("Tiempo actual " + calcularTiempoEmpleado(visitaDiaria) + " Tiempo actual " + calcularTiempoEmpleado(copiaVisitaDiaria));
				System.out.println("Kilometros actual " + calcularKilometrosEmpleado(visitaDiaria) + " Kilometros nuevo " + calcularKilometrosEmpleado(copiaVisitaDiaria));
				System.out.println("Se ha encontrado una mejora ");
				visitaDiaria = new ArrayList<Integer>(copiaVisitaDiaria);
				valorActual = calcularValorDiario(copiaVisitaDiaria); //Actualizamos el valor, mejora
			}
		}
		return visitaDiaria;
	}

	/**
	 * Método Búsqueda local que busca una mejora de la solución eliminando dos elementos
	 * e introduciendo uno entre los vecinos que mejore el valor pero que ni supere el tiempo 
	 * limite diario ni se diferencie en más de 30min
	 * @param visitaDiaria		Visita realizada los días anteriores
	 * @param yaVisitados		Visitas realizadas ya durante el presente día
	 * @return					Solución
	 */
	public ArrayList<Integer> busquedaLocal2a1(ArrayList<Integer> visitaDiaria, ArrayList<ArrayList<Integer>> yaVisitados) { 
		//visitaDiaria = new ArrayList<Integer>();
		//visitaDiaria.add(0); visitaDiaria.add(6); visitaDiaria.add(5); visitaDiaria.add(22); visitaDiaria.add(0);
		System.out.println("\nBusqueda local 2 Lugares a 1");

		System.out.println("Valor actual " + calcularValorDiario(visitaDiaria));
		System.out.println("Tiempo actual " + calcularTiempoEmpleado(visitaDiaria));
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

				//Eliminamos dos elementos
				copiaDia.remove(copiaDia.indexOf(visitaDiaria.get(j)));
				copiaDia.remove(copiaDia.indexOf(visitaDiaria.get(k)));

				//System.out.println("Hemos borrado " + visitaDiaria.get(j) + " y " + visitaDiaria.get(k) + " : " + copiaDia);

				//Buscamos aquel vecino que no ha sido visitado y se comprueba si mejora el valor con respecto al actual o no
				for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
					if(yaVisitado(i, getLugaresVisitados(), visitaDiaria) == false) {

						for(int l = 1; l < (copiaDia.size()); l++) {
							copiaDia.add(l, i);
							//System.out.println("Probando " + copiaDia);
							if(calcularValorDiario(copiaDia) < valorAMejorar) {
								//Si encontramos un mejor valor
								if(valorCandidato > calcularValorDiario(copiaDia)) {
									//Si el tiempo empleado no supera las horas diarias o es menor en 30min que la anterior solucion
									if(calcularTiempoEmpleado(copiaDia) < (getNumHorasDiarias() * 60)  && (Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(visitaDiaria)) < 30)) {
										candidato = new ArrayList<Integer>(copiaDia);
										valorCandidato = calcularValorDiario(copiaDia);
										//System.out.println("Posibilidad de cambio en " + candidato + " con valor " + valorCandidato);
									}
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
			System.out.println("Solucion Mejorada de " + visitaDiaria + " a " + copiaDia);
			System.out.println("Valor " + calcularValorDiario(copiaDia));
			System.out.println("Tiempo empleado " + calcularTiempoEmpleado(copiaDia));
			System.out.println("Kilometros empleados " + calcularKilometrosEmpleado(copiaDia));
			return copiaDia;
		} else 
			return visitaDiaria;
	}

	/**
	 * Método Búsqueda local que busca una mejora de la solución eliminando dos elementos
	 * e introduciendo uno entre los vecinos que mejore el valor pero que ni supere el tiempo 
	 * limite diario ni se diferencie en más de 30min
	 * @param visitaDiaria		Visita realizada los días anteriores
	 * @param yaVisitados		Visitas realizadas ya durante el presente día
	 * @return					Solución
	 */
	public ArrayList<Integer> busquedaLocal1a1(ArrayList<Integer> visitaDiaria, ArrayList<ArrayList<Integer>> yaVisitados) { 

		System.out.println("\nBusqueda local 1 Lugares a 1");

		System.out.println("Valor actual " + calcularValorDiario(visitaDiaria));
		System.out.println("Tiempo actual " + calcularTiempoEmpleado(visitaDiaria));
		System.out.println("Kilometros actual " + calcularKilometrosEmpleado(visitaDiaria));
		System.out.println("Visita actual " + visitaDiaria);

		ArrayList<Integer> copiaDia = new ArrayList<Integer>(visitaDiaria);
		float valorAMejorar = calcularValorDiario(visitaDiaria);

		ArrayList<Integer> candidato = new ArrayList<Integer>();
		float valorCandidato = valorAMejorar;

		//Todas las combinaciones eliminando la salida y llegada, Hard Rock
		for(int j = 1; j < (visitaDiaria.size() - 1); j++) {
			copiaDia = new ArrayList<Integer>(visitaDiaria);

			//Eliminamos un elemento
			copiaDia.remove(copiaDia.indexOf(visitaDiaria.get(j)));
			//System.out.println("Hemos borrado " + visitaDiaria.get(j) + " : " + copiaDia);

			//Buscamos aquel vecino que no ha sido visitado y se comprueba si mejora el valor con respecto al actual o no
			for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
				if(yaVisitado(i, getLugaresVisitados(), visitaDiaria) == false) {
					boolean noTabu = false;
					for(int b = 0; b < getListaTabu().size(); b++) {
						if(getListaTabu().get(b).x == i) {
							noTabu = true;
						}
					}
					if(noTabu == false) {
						for(int l = 1; l < (copiaDia.size()); l++) {
							copiaDia.add(l, i);
							//System.out.println("Probando " + copiaDia);
							if(calcularValorDiario(copiaDia) < valorAMejorar) {
								//Si encontramos un mejor valor
								if(valorCandidato > calcularValorDiario(copiaDia)) {
									//Si el tiempo empleado no supera las horas diarias o es menor en 30min que la anterior solucion
									if(calcularTiempoEmpleado(copiaDia) < (getNumHorasDiarias() * 60)  && (Math.abs(calcularTiempoEmpleado(copiaDia) - calcularTiempoEmpleado(visitaDiaria)) < 30)) {
										candidato = new ArrayList<Integer>(copiaDia);
										valorCandidato = calcularValorDiario(copiaDia);
										//System.out.println("Posibilidad de cambio en " + candidato + " con valor " + valorCandidato);
									}
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
			System.out.println("Solucion Mejorada de " + visitaDiaria + " a " + copiaDia);
			System.out.println("Valor " + calcularValorDiario(copiaDia));
			System.out.println("Tiempo empleado " + calcularTiempoEmpleado(copiaDia));
			System.out.println("Kilometros empleados " + calcularKilometrosEmpleado(copiaDia));
			return copiaDia;
		} else 
			return visitaDiaria;
	}

	/**
	 * Método que calcula el valor de visitar un Array de Sitios. 
	 * Se considera Distancia/Valoración 
	 * @param dia		Array de Lugares Visitados
	 * @return			float valor de la visita de los lugares especificados en dia
	 */
	public float calcularValorDiario(ArrayList<Integer> dia) {
		float aux = 0;
		for(int i = 1; i < (dia.size() - 1); i++) {
			//System.out.println("A " + getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(i - 1)][dia.get(i)] + " B " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getPuntuacion());
			aux += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(i - 1)][dia.get(i)] /
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getPuntuacion();
		}
		return aux;
	}

	public float calcularValorTotal(ArrayList<ArrayList<Integer>> itinerario) {
		float valorTotalViaje = 0;
		for(int i = 0; i < itinerario.size(); i++) {
			valorTotalViaje += calcularValorDiario(itinerario.get(i));
			//System.out.println(itinerario.get(i) + " con valor " + calcularValorDiario(itinerario.get(i)));
			//System.out.println("Tiempo viaje " + calcularTiempoEmpleado(itinerario.get(i)));
		}
		return valorTotalViaje;
	}
	/**
	 * Método que calcula el tiempo empleado en visitar un Array de Sitios.
	 * @param dia 		Array de Lugares Visitados
	 * @return			int tiempo empleado recorrer los lugares especificados en dia
	 * teniendo en cuenta el tiempo en llegar al lugar y la duración de la actividad.
	 */
	public int calcularTiempoEmpleado(ArrayList<Integer> dia) {
		int tiempoDiario = 0; 
		//System.out.println("Lugares : " + dia);
		for(int i = 1; i < (dia.size() - 1); i++) {
			tiempoDiario += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(i - 1)][dia.get(i)];
			tiempoDiario += getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getDuracion() * 60;
		}
		tiempoDiario += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(dia.size() - 2)][0];
		return tiempoDiario;
	}

	/**
	 * Método que calcula los kilómetros empleados para visitar el conjunto de lugares un Array de Sitios
	 * @param dia		Array de Lugares Visitados
	 * @return			float kilometros empleados
	 */
	public float calcularKilometrosEmpleado(ArrayList<Integer> dia) {
		float kilometrosDiarios = 0;
		for(int i = 1; i < (dia.size() - 1); i++) {
			kilometrosDiarios += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(i - 1)][dia.get(i)];
		}
		kilometrosDiarios += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(dia.size() - 2)][0];
		return kilometrosDiarios;
	}

	/**
	 * Método que evalua si un lugar ha sido visitado los días anteriores o el actual.
	 * @param lugar				Lugar a evaluar
	 * @param diasAnteriores	Array que contiene las visitas a lugares de dias anteriores
	 * @param diaActual			Array que contiene las visitas que ya se han hecho el día actual
	 * @return					Boolean devuelve si se ha visitado o no el lugar
	 */
	public boolean yaVisitado(int lugar, ArrayList<ArrayList<Integer>> diasAnteriores, ArrayList<Integer> diaActual) {
		boolean yaVisitado = false;
		/**Si aun no ha sido visitado ese dia o los anteriores si los hubiera*/
		for(int l = 0; l < diasAnteriores.size(); l++) 
			if(diasAnteriores.get(l).contains(lugar)) 
				yaVisitado = true;
		if(diaActual.contains(lugar)) 
			yaVisitado = true;
		return yaVisitado;
	}


	/**
	 * Método que realiza un algoritmo aleatorio para el dia actual. Utilizado para heuristicas comunes como Busqueda Tabu, Memoria Largo Plazo, 
	 * Recocido Simulado y MultiArranque.
	 */
	public void solucionAleatoria() {
		solucionDiaria = new ArrayList<Integer>();

		//Añadimos el primer elemento, de donde partimos
		getSolucionDiaria().add(0);

		solucionParcialAleatoria();
	}

	/**
	 * Método utilizado en solucionAleatoria utilizado para añadir aleatoriamente elementos en el día actual
	 * siempre que se cumplan las restricciones. La solucion se almacena en getSolucionDiaria().
	 */
	public void solucionParcialAleatoria() {
		//Maximo de comparaciones para decidir que no se puede introducir ningun sitio mas sin sobrepasar la restriccion de tiempo
		int maximoComparaciones = 0; 
		int minutosAcumulados = 0;

		if(solucionDiaria.size() == 1) {

		} else {
			//Eliminamos el elemento 0 final
			solucionDiaria.remove(solucionDiaria.size() - 1);
			for(int i = 1; i < solucionDiaria.size(); i++) {
				minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(i - 1)][getSolucionDiaria().get(i)] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).getDuracion() * 60);
			}
		}

		//Maximo de comparaciones posibles
		while (maximoComparaciones < 56) {
			int elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());
			while (contieneListaTabu(elegido)) {
				elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());
			}
			//Si aun no se ha visitado el lugar
			if(yaVisitado(elegido, getLugaresVisitados(), getSolucionDiaria()) == false) {
				//Con el tiempo que nos queda, si sumamos el tiempo en llegar alli, la duracion de la actividad y cuanto tardamos en volver a 0 si lo elegimos, es menor que la hora maxima
				if((minutosAcumulados + 
						getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + 
						(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60) +  
						getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[elegido][0]) 
						< (getNumHorasDiarias() * 60)) {
					//Reseteamos las comparaciones
					maximoComparaciones = 0;
					minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60);
					getSolucionDiaria().add(elegido);
				}
				maximoComparaciones++;
			}}
		getSolucionDiaria().add(0);
	}
	
	/**
	 * Método que realiza un algoritmo GRASP para el dia actual. Utilizado para heuristicas comunes como Busqueda Tabu, Memoria Largo Plazo, 
	 * Recocido Simulado y MultiArranque. Utilizado el LRC
	 */

	public void solucionGRASP() {
		solucionDiaria = new ArrayList<Integer>();

		getSolucionDiaria().add(0);
		solucionGRASPParcial();	
	}

	/**
	 * Método utilizado en solucionGRASP utilizado para añadir aleatoriamente elementos en el día actual
	 * siempre que se cumplan las restricciones. La solucion se almacena en getSolucionDiaria().
	 */
	public void solucionGRASPParcial() {
		ArrayList<Point2D.Float> lugaresCandidatos = new ArrayList<Point2D.Float>();
		int tiempoAcumulado = 0;
		
		if(solucionDiaria.size() == 1) {
			
		} else {
			//Eliminamos el elemento 0 final
			solucionDiaria.remove(solucionDiaria.size() - 1);
			for(int i = 1; i < solucionDiaria.size(); i++) {
				tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(i - 1)][getSolucionDiaria().get(i)] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).getDuracion() * 60);
			}
		}

		//BUSCAMOS EL MEJOR CANDIDATO DESDE NUESTRA POSICION ACTUAL
		//Recorremos los lugares que podemos visitar

		boolean encontrado = true;

		while(encontrado) {

			//Candidatos a ocupar el siguiente lugar
			lugaresCandidatos.clear();

			//Buscamos entre todas las posibilidades
			for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
				if(!contieneListaTabu(i))
					//Si no ha sido visitado
					if(yaVisitado(i, getLugaresVisitados(), getSolucionDiaria()) == false) {
						//Si se puede introducir por tiempo
						if(((getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][i] + 
								(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getDuracion() * 60) 
								+ getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[i][0]) + 
								tiempoAcumulado) <= (getNumHorasDiarias() * 60)) {
							if(lugaresCandidatos.size() < LRC) {
								lugaresCandidatos.add(new Point2D.Float(getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion(), i));
							} else {
								double peor = lugaresCandidatos.get(0).getX();
								double posicionPeor = 0;

								//Buscamos el peor ya introducido y lo comparamos con el nuevo
								for(int a = 1; a < lugaresCandidatos.size(); a++) {
									if(lugaresCandidatos.get(a).getX() > peor) {
										peor = lugaresCandidatos.get(a).getX();
										posicionPeor = a;
									}
								}

								//si el valor que se desea introducir es mejor que el peor ya insertado, borramos el anterior e insertamos el nuevo
								if((getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion()) < peor) {
									lugaresCandidatos.remove((int)posicionPeor);
									lugaresCandidatos.add(new Point2D.Float(getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
											getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion(), i));
								}
							}
						}
					} 
			}

			//No se ha encontrado ningun valor que pueda insertarse sin superar el tiempo limite diario
			if(lugaresCandidatos.size() == 0) {
				encontrado = false;
			} else { //Se ha encontrado un sitio que cumpla con las especificaciones
				//elegimos uno de forma aleatoria
				int elegido = (int)(Math.random() * lugaresCandidatos.size());
				//Añadimos el tiempo que tarda en llegar al sitio
				tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)lugaresCandidatos.get(elegido).getY()];
				//A�adimos el tiempo que estar� en el sitio
				tiempoAcumulado += getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)lugaresCandidatos.get(elegido).getY()).getDuracion() * 60;
				//Añadimos el lugar
				getSolucionDiaria().add((int)lugaresCandidatos.get(elegido).getY());
			}
		}
		getSolucionDiaria().add(0);
	}

	/**
	 * Muestra todo el itinerario del viaje, resultado de la resolución del problema
	 * y el valor total de la solución.
	 * Incluye el tiempo utilizado, kilometros y valor acumulado
	 */
	public void mostrarItinerarioViaje() {
		System.out.println("\nResumen del itinerario: ");

		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			System.out.println("\nDia " + (i + 1) + ": ");
			mostrarConsultaItinerarioDia(getLugaresVisitados().get(i));
		}

		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += calcularValorDiario(getLugaresVisitados().get(i));
		}
		System.out.println("\nValor total del viaje: " + (valorTotalViaje));
	}

	/**
	 * Muestra el Itinerario de viaje de los lugares introducidos por parámetro
	 * Incluye el tiempo utilizado, kilometros y valor acumulado
	 * @param itinerario 		Lugares del itinerario
	 */
	public void mostrarConsultaItinerarioDia(ArrayList<Integer> itinerario) {

		for(int i = 0; i < itinerario.size(); i++) {
			getLugaresTuristicosDisponibles().getLugaresTuristicos().get(itinerario.get(i)).mostrarLugar();
		}

		System.out.println("Lugares: " + itinerario);
		System.out.println("Tiempo acumulado " + calcularTiempoEmpleado(getSolucionDiaria()) + "min");
		System.out.println("Kilometros diarios " + calcularKilometrosEmpleado(getSolucionDiaria()) + " km");
		System.out.println("Valor Acumulado diario: " + calcularValorDiario(getSolucionDiaria()));
	}

	public boolean contieneListaTabu(int lugar) {
		for(int i = 0; i < getListaTabu().size(); i++) {
			if(getListaTabu().get(i).x == lugar) {
				return true;
			}
		}
		return false;
	}
	///GETS

	/**
	 * Método que devuelve el numero de días del itinerario de viaje
	 * @return int numero de días
	 */
	public int getNumDiasEstancia() {
		return numDiasEstancia_;
	}

	/**
	 * Método que devuelve el número de horas diarias del itinerario de viaje
	 * @return int número de horas
	 */
	public int getNumHorasDiarias() {
		return numHorasDiarias_;
	}

	/**
	 * Método que devuelve la información de todos los Lugares existentes para la resolución de problemas
	 * @return	puntosTuristicos
	 */
	public puntosTuristicos getLugaresTuristicosDisponibles() {
		return lugaresTuristicosDisponibles_;
	}

	/**
	 * Método utilizado en la resolución del problema que devuelve la solución Diaria que se este calculando 
	 * @return ArrayList de Integer
	 */
	public ArrayList<Integer> getSolucionDiaria() {
		return solucionDiaria;
	}

	/**
	 * Método utilizado en la resolución del problema que devuelve la solución del problema
	 * @return ArrayList de Integer
	 */
	public ArrayList<ArrayList<Integer>> getLugaresVisitados() {
		return lugaresVisitados;
	}

	/**
	 * Método que devuelve una lista tabu de elementos que no pueden visitarse. Utilizado en alguna heurística
	 * @return ArrayList de Point, (lugar, tiempoDeEspera)
	 */
	public ArrayList<Point> getListaTabu() {
		return listaTabu;
	}
}
