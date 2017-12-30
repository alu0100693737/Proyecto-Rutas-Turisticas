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
										System.out.println("Quitando un elemento y poniendo otro");
										getLugaresCandidatos().remove(posicionPeor);
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

					for(int a = 0; a < getLugaresCandidatos().size(); a++) {
						System.out.println("Candidato num " + a + ": " + getLugaresCandidatos().get(a));
					}
					
					int elegido = (int)(Math.random() * LRC);
					
					System.out.println("Elegido " + elegido);
					
					System.out.println("\nProximo lugar: " + (int)getLugaresCandidatos().get(elegido).getY() + "\n");
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).mostrarLugar();

					System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)getLugaresCandidatos().get(elegido).getY()] + " y estaremos en la actividad " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).getDuracion() * 60);

					//Añadimos el tiempo que tarda en llegar al sitio
					tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size()-1)][(int)getLugaresCandidatos().get(elegido).getY()];
					//Añadimos el tiempo que estará en el sitio
					tiempoAcumulado += getLugaresTuristicosDisponibles().getLugaresTuristicos().get((int)getLugaresCandidatos().get(elegido).getY()).getDuracion() * 60;

					System.out.println("Tiempo acumulado " + tiempoAcumulado + " min");
					System.out.println("Valor: " + getLugaresCandidatos().get(elegido).getX());
					valorAcumulado += getLugaresCandidatos().get(elegido).getX();
					System.out.println("Valor acumulado " + valorAcumulado);

					//Añadimos el lugar
					getSolucionDiaria().add((int)getLugaresCandidatos().get(elegido).getY());
				}

			}

			//Añadimos la distancia del ultimo al lugar de inicio
			tiempoAcumulado += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][0];
			getSolucionDiaria().add(0);

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
}