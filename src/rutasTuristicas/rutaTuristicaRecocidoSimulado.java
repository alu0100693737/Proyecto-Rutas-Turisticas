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
	private final int LRC = 3;

	public rutaTuristicaRecocidoSimulado(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema(true);		

	}

	@Override
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
			//solucionGRASP();

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

			System.out.println("\nAplicando Recocido simulado ");

			float temperatura = 25;
			while(temperatura > 0) {
				System.out.println("\nTEMPERATURA  " + temperatura);
				ArrayList<Integer> recocidoSimulado = new ArrayList<Integer>(aplicarRecocidoSimulado(getSolucionDiaria(), getLugaresVisitados(), temperatura));
				if(!getSolucionDiaria().equals(recocidoSimulado)) {
					System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
					solucionDiaria = new ArrayList<Integer>(recocidoSimulado);
					System.out.println("Visita actual " + getSolucionDiaria());
					System.out.println("Valor actual " + calcularValorDiario(getSolucionDiaria()));
					System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
					System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
					System.out.println("Visita actual " + getSolucionDiaria());
				} 
				temperatura -= 0.5;
			}
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

							//System.out.println("Valor, posicion " + lugaresCandidatos.get(lugaresCandidatos.size() - 1));
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

				//A�adimos el lugar
				getSolucionDiaria().add((int)lugaresCandidatos.get(elegido).getY());
			}
		}

		getSolucionDiaria().add(0);
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
				if((2 / (1 + Math.pow(Math.E, (diferenciaValores/temperatura)))) > 0.90) {
					System.out.println((2 / (1 + Math.pow(Math.E, (diferenciaValores/temperatura)))));
					System.out.println("CAMBIANDO AUNQUE ES PEOR");
					return copiaDia;
				} else 
					return visitaDiaria;
			}
		}
	}

}