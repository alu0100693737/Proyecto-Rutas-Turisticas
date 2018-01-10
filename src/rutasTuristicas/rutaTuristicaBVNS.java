package rutasTuristicas;

import java.awt.geom.Point2D;
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

	/** 
	 * Número de candidatos para realizar la elección del próximo lugar en algoritmo GRASP
	 */
	private final int LRC = 3;

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

			System.out.println("Dia numero " + (k + 1));
			solucionAleatoria();
			solucionGRASP();

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
			int maximoComparaciones = 0;
			while(K <= 4 || (maximoComparaciones < 50)) {
				ArrayList<Integer> solucionCandidata = new ArrayList<Integer>(aplicarBVNS(getSolucionDiaria(), getLugaresVisitados(), rondaActual));
				if(calcularValorDiario(getSolucionDiaria()) > calcularValorDiario(solucionCandidata)) {
					System.out.println("\n\nSE CAMBIA LA SOLUCION Y SE VUELVE A EMPEZAR\n");
					solucionDiaria = new ArrayList<Integer>(solucionCandidata);
					rondaActual = 1;
					System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
					System.out.println("Agitacion hecha " + solucionCandidata);
					System.out.println("Valor actual " + calcularValorDiario(solucionCandidata));
					System.out.println("Ronda num: " + rondaActual);
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
		System.out.println("Valor total del viaje: " + valorTotalViaje + "\n");
	}

	public void solucionAleatoria() {
		solucionDiaria = new ArrayList<Integer>();
		int minutosAcumulados = 0;
		//Maximo de comparaciones para decidir que no se puede introducir ningun sitio mas sin sobrepasar la restriccion de tiempo
		int maximoComparaciones = 0; 			
		//Añadimos el primer elemento, de donde partimos
		getSolucionDiaria().add(0);

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
	}

	public void solucionGRASP() {

		ArrayList<Point2D.Float> lugaresCandidatos = new ArrayList<Point2D.Float>();

		solucionDiaria = new ArrayList<Integer>();

		getSolucionDiaria().add(0);
		int tiempoAcumulado = 0;

		//BUSCAMOS EL MEJOR CANDIDATO DESDE NUESTRA POSICION ACTUAL
		//Recorremos los lugares que podemos visitar

		boolean encontrado = true;

		while(encontrado) {
			System.out.println("\nBuscando un lugar a visitar\n");

			//Candidatos a ocupar el siguiente lugar
			lugaresCandidatos.clear();

			//Buscamos entre todas las posibilidades
			for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {
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

							System.out.println("Valor, posicion " + lugaresCandidatos.get(lugaresCandidatos.size() - 1));
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
								System.out.println("Quitando " + peor + " poniendo " + getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
										getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion());

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
				System.out.println("Fin del dia. Tiempo: " + (tiempoAcumulado + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][0]));
				encontrado = false;
			} else { //Se ha encontrado un sitio que cumpla con las especificaciones

				System.out.println("\nAnalizadas todas las opciones, toca decidir:\n");
				for(int a = 0; a < lugaresCandidatos.size(); a++) {
					System.out.println("Candidato num " + a + ": " + lugaresCandidatos.get(a));
				}

				//elegimos uno de forma aleatoria
				int elegido = (int)(Math.random() * lugaresCandidatos.size());

				System.out.println("Elegido " + elegido);

				System.out.println("\nProximo lugar: " + (int)lugaresCandidatos.get(elegido).getY() + "\n");
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)lugaresCandidatos.get(elegido).getY()).mostrarLugar();

				System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)lugaresCandidatos.get(elegido).getY()] + " y estaremos en la actividad " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)lugaresCandidatos.get(elegido).getY()).getDuracion() * 60);

				//A�adimos el tiempo que tarda en llegar al sitio
				tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)lugaresCandidatos.get(elegido).getY()];
				//A�adimos el tiempo que estar� en el sitio
				tiempoAcumulado += getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)lugaresCandidatos.get(elegido).getY()).getDuracion() * 60;

				System.out.println("Tiempo acumulado " + tiempoAcumulado + " min");
				System.out.println("Valor: " + lugaresCandidatos.get(elegido).getX());

				//A�adimos el lugar
				getSolucionDiaria().add((int)lugaresCandidatos.get(elegido).getY());
			}
		}

		getSolucionDiaria().add(0);
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

		//Si se puede eliminar K elementos en la ronda
		if((solucionDiariaInicial.size() - 2) > rondaActual) {
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

			System.out.println("\nAplicando Mejora basada en agitación sobre la solución ");
			ArrayList<Integer> busqueda = new ArrayList<Integer>(busquedaLocalCambioVisita(copiaDia));
			if(!copiaDia.equals(busqueda)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				System.out.println("Agitacion hecha " + copiaDia);
				System.out.println("Valor actual " + calcularValorDiario(busqueda));
				copiaDia = new ArrayList<Integer>(busqueda);
			}
			return copiaDia;

		} else {
			System.out.println("No se puede seguir a la siguiente ronda de K");
			System.out.println("K = " + K);
			return getSolucionDiaria();
		}
	}
}