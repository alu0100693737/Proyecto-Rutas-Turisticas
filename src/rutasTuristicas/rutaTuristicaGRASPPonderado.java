package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//menor km/valoracion ej: 90/2 > 150/5


//Algoritmos Greedy, Ponderado. Menor Tiempo en llegar, sin considerar tiempo en la actividad. Distancia / (Valoracion * factorPonderado) 
public class rutaTuristicaGRASPPonderado extends problemaRutasTuristicas {  

	private ArrayList<Float> valoresDiarios;
	private ArrayList<Integer> tiemposDiarios;

	private final int LRC = 3;
	private ArrayList<Point2D.Float> lugaresCandidatos; //valor, posicion (float, int)

	public rutaTuristicaGRASPPonderado(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema();
	}

	@Override
	public void resolverProblema() {

		//Introducir factor ponderado
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();
		valoresDiarios = new ArrayList<Float>();
		tiemposDiarios = new ArrayList<Integer>();

		lugaresCandidatos = new ArrayList<Point2D.Float>();

		System.out.println("Algoritmo Greedy Ponderado KM/Valoracion");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);

		float valorAcumulado;
		int tiempoAcumulado;
		//Para el conjunto de dias
		for(int k = 0; k < getNumDiasEstancia(); k++) {
			System.out.println("\nDia: " + (k + 1));
			solucionDiaria = new ArrayList<Integer>();

			getSolucionDiaria().add(0);

			valorAcumulado = 0;
			tiempoAcumulado = 0;

			//BUSCAMOS EL MEJOR CANDIDATO DESDE NUESTRA POSICION ACTUAL
			//Recorremos los lugares que podemos visitar

			boolean encontrado = true;

			while(encontrado) {

				System.out.println("\nBuscando un lugar a visitar\n");
	
				//Candidatos a ocupar el siguiente lugar
				getLugaresCandidatos().clear();

				//Buscamos entre todas las posibilidades
				for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++) {

					boolean yaVisitado = false;

					//Si aun no ha sido visitado ese dia o los anteriores si los hubiera
					for(int l = 0; l < getLugaresVisitados().size(); l++) 
						if(getLugaresVisitados().get(l).contains(i)) 
							yaVisitado = true;


					if(getSolucionDiaria().contains(i)) 
						yaVisitado = true;

					//Si no ha sido visitado
					if(yaVisitado == false) {

						//Si el valor es mejor que el actual
						//if((getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][i] /
						//		getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).getPuntuacion()) < valor) {

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
					valorAcumulado += getLugaresCandidatos().get(elegido).getX();
					System.out.println("Valor acumulado " + valorAcumulado);

					//A�adimos el lugar
					getSolucionDiaria().add((int)getLugaresCandidatos().get(elegido).getY());
				}
			}

			//A�adimos la distancia del ultimo al lugar de inicio
			tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][0];
			
			getSolucionDiaria().add(0);

			System.out.println("Resumen del dia: ");
			for(int i = 0; i < getSolucionDiaria().size(); i++) 
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			
			System.out.println("Aplicando Busqueda Local ");
			if(getSolucionDiaria() != busquedaLocalCambioVisita(getSolucionDiaria())) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				for(int i = 0; i < getSolucionDiaria().size(); i++) 
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			}
			
			getLugaresVisitados().add(getSolucionDiaria());
			getValoresDiarios().add(valorAcumulado);
			getTiemposDiarios().add(tiempoAcumulado);

			System.out.println("Dia " + (k + 1) + " terminado.\n----------------------------------------------------------------------------------\n");
			System.out.println("Lugares Visitados " + getSolucionDiaria());
		}

		System.out.println("\n-----------------------------------------");
		System.out.println("Resumen del itinerario: ");

		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			System.out.println("\nDia " + (i + 1) + ": ");
			for(int j = 0; j < getLugaresVisitados().get(i).size(); j++) {
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getLugaresVisitados().get(i).get(j)).mostrarLugar();
			}
			System.out.println("Tiempo utilizado: " + getTiemposDiarios().get(i) + " min");
			System.out.println("Valor Acumulado diario: " + getValoresDiarios().get(i));
		}
		
		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += getValoresDiarios().get(i);
		}
		System.out.println("\n\nValor total del viaje: " + valorTotalViaje);
		
		//Aplicar busqueda local
		System.out.println("\nAplicando busqueda local");
		busquedaLocal2a1();
	}

	public ArrayList<Float> getValoresDiarios() {
		return valoresDiarios;
	}

	public ArrayList<Integer> getTiemposDiarios() {
		return tiemposDiarios;
	}

	public ArrayList<Point2D.Float> getLugaresCandidatos() {
		return lugaresCandidatos;
	}
	
	public ArrayList<Integer> busquedaLocalCambioVisita(ArrayList<Integer> visitaDiaria) { //ej: [0,6,5,22,0] -> 1.6589228     [0,5,6,22,0] -> 1.5035715  
		System.out.println("Busqueda local de reemplazo");
		//No se puede modificar el elemento 0
		ArrayList<Integer> copiaVisitaDiaria = new ArrayList<Integer>(visitaDiaria);
		
		float valorActual = calcularValor(visitaDiaria);
		for(int i = 1; i < copiaVisitaDiaria.size() - 2; i++) {
			int posAux = copiaVisitaDiaria.get(i);
			copiaVisitaDiaria.set(i, copiaVisitaDiaria.get(i + 1));
			copiaVisitaDiaria.set(i + 1, posAux);
			if(valorActual > calcularValor(copiaVisitaDiaria)) {
				System.out.println("Valor actual " + valorActual + " Valor nuevo " + calcularValor(copiaVisitaDiaria));
				System.out.println("Se ha encontrado una mejora ");
				System.out.println("Antes: " + visitaDiaria + " Ahora: " + copiaVisitaDiaria);
				visitaDiaria = new ArrayList<Integer>(copiaVisitaDiaria);
			}
		}
		return visitaDiaria;
	}
	
	public void busquedaLocal2a1() { 
		
		
	}
		/*float valorInicialTotal = 0;
		
		for(int i = 0; i < getValoresDiarios().size(); i++) {
			valorInicialTotal += getValoresDiarios().get(i);
		}
		
		System.out.println("El valor Inicial es " + valorInicialTotal);
		
		int diaElegido = 0;
		int peorElemento = 0;
		
		//Para el numero de d�as miramos que valor ofrece peor valor
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			
			System.out.println("Valor del d�a " + getLugaresVisitados().get(i));
			ArrayList<Integer> copiaDia = new ArrayList<Integer>(getLugaresVisitados().get(i));
			System.out.println(copiaDia);
			
			float copiaValor = getValoresDiarios().get(i);
			
			//Todas las combinaciones eliminando la salida y llegada, Hard Rock
			
			for(int j = 1; j < (getLugaresVisitados().get(i).size() - 1); j++) {
				for(int k = (j +1); k < (getLugaresVisitados().get(i).size() - 1); k++) {
						//ERROR EN j y k
						copiaDia.remove(j);
						System.out.println("Valor actual " + calcularValor(copiaDia));
						copiaDia.remove(k);
						System.out.println("Valor actual " + calcularValor(copiaDia));
						
						System.out.println("Valor actual solo ");
						break;
				}
				break;
			}
		}
	}*/

	//Calcula el valor de visitar un array de sitios
	public float calcularValor(ArrayList<Integer> dia) {
		float aux = 0;
		for(int i = 1; i < (dia.size() - 1); i++) {
			aux += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(i - 1)][dia.get(i)] /
			getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getPuntuacion();
		}
		return aux;
	}
	
}