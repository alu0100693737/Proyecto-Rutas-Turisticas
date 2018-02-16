package rutasTuristicas;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase Principal para la prueba del Proyecto
 * Contiene un menu para probar cada una de las heurísticas implementadas:
 * 		Algoritmo Aleatorio,
 *   	Algoritmo Greedy,
 * 		Algoritmo GRASP,
 * 		Búsqueda Local,
 * 		Recocido Simulado,
 * 		Busqueda Tabú,	
 * 		MultiArranque y 
 * 		BVNS
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class Principal {

	public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException{
		Scanner sn = new Scanner(System.in);
		boolean salir = false;
		int opcion; //Guardaremos la opcion del usuario
		int eleccion;
		while(!salir){

			System.out.println("\n1. Algoritmo Aleatorio, posibilidad de Busqueda Local (2 a 1) y (1 a 1)\n");
			System.out.println("2. Algoritmo Greedy, posibilidad de Búsqueda Local (2 a 1) y (1 a 1)\n");
			System.out.println("3. Algoritmo GRASP, posibilidad de Búsqueda Local (2 a 1) y (1 a 1)\n");
			System.out.println("4. MultiArranque con algoritmo Aleatorio o GRASP y búsqueda local (2 a 1)\n");
			System.out.println("5. Recocido Simulado con algoritmo Aleatorio o GRASP\n");
			System.out.println("6. Búsqueda Tabú con algoritmo Aleatorio o GRASP\n");
			System.out.println("7. VNS Básico con algoritmo Aleatorio o GRASP\n");
			System.out.println("8. Busqueda Memoria a Largo Plazo con algoritmo Aleatorio o GRASP\n");
			System.out.println("9. Salir");

			System.out.println("\n\tEscribe una de las opciones");
			opcion = sn.nextInt();

			//Tiempo para el análisis de heuristicas
			Clock tiempo = new Clock();
			ArrayList<Point2D.Float> tiemposEjecucion = new ArrayList<Point2D.Float>();
			boolean analisis = true;

			switch(opcion){
			case 1:
				System.out.println("Algoritmo Aleatorio, posibilidad de Busqueda Local (2 a 1) y (1 a 1)");
				rutaTuristicaAleatoria aleatorio = new rutaTuristicaAleatoria(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				aleatorio.resolverProblema(false);
				break;
			case 2:
				System.out.println("Algoritmo Greedy Ponderado, posibilidad de Búsqueda Local (2 a 1) y (1 a 1)");
				rutaTuristicaGreedyPonderado greedy = new rutaTuristicaGreedyPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				greedy.resolverProblema(false);
				break;
			case 3:
				System.out.println("Algoritmo GRASP, posibilidad de Búsqueda Local (2 a 1) y (1 a 1)");
				rutaTuristicaGRASPPonderado grasp = new rutaTuristicaGRASPPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				grasp.resolverProblema(false);
				break;
			case 4:
				System.out.println("Estrategia MultiArranque con algoritmo Aleatorio o GRASP");
				System.out.println("Pulse 1 para utilizar un algoritmo aleatorio, pulse 2 para estrategia GRASP");
				eleccion = sn.nextInt();
				if(eleccion == 1) {
					rutaTuristicaMultiArranque multiArranque = new rutaTuristicaMultiArranque(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), false);
				} else if (eleccion == 2) {
					rutaTuristicaMultiArranque multiArranque = new rutaTuristicaMultiArranque(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
				}
				break;
			case 5:


				System.out.println("Recocido Simulado con algoritmo Aleatorio o GRASP");
				System.out.println("Pulse 1 para utilizar un algoritmo aleatorio, pulse 2 para estrategia GRASP");
				eleccion = sn.nextInt();
				if(eleccion == 1) {
					rutaTuristicaRecocidoSimulado recocido = new rutaTuristicaRecocidoSimulado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), false);
				} else if (eleccion == 2)  {
					rutaTuristicaRecocidoSimulado recocido = new rutaTuristicaRecocidoSimulado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
				}


				break;
			case 6:
				
					System.out.println("Búsqueda Tabú");
					System.out.println("Búsqueda Tabú con Algoritmo Aleatorio o GRASP");
					System.out.println("Pulse 1 para utilizar un algoritmo aleatorio, pulse 2 para estrategia GRASP");
					eleccion = sn.nextInt();
					if(eleccion == 1) {
					rutaTuristicaBusquedaTabu busquedaTabu = new rutaTuristicaBusquedaTabu(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), false);
					} else if (eleccion == 2)  {
					rutaTuristicaBusquedaTabu busquedaTabu = new rutaTuristicaBusquedaTabu(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
				}
					

				break;
			case 7: 
				System.out.println("VNS Básico con Algoritmo Aleatorio o GRASP");
				System.out.println("Pulse 1 para utilizar un algoritmo aleatorio, pulse 2 para estrategia GRASP");
				eleccion = sn.nextInt();
				if(eleccion == 1) {
					rutaTuristicaBVNS bvns = new rutaTuristicaBVNS(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), false);
				} else if (eleccion == 2)  {
					rutaTuristicaBVNS bvns = new rutaTuristicaBVNS(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
				}
				break;
			case 8: 

				System.out.println("Búsqueda Memoria Largo Plazo");
				System.out.println("Pulse 1 para utilizar un algoritmo aleatorio, pulse 2 para estrategia GRASP");
				eleccion = sn.nextInt();
				if(eleccion == 1) {
					rutaTuristicaBusquedaMemoriaLargoPlazo busquedaMemoriaLargoPlazo = new rutaTuristicaBusquedaMemoriaLargoPlazo(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), false);
				} else if (eleccion == 2)  {
					rutaTuristicaBusquedaMemoriaLargoPlazo busquedaMemoriaLargoPlazo = new rutaTuristicaBusquedaMemoriaLargoPlazo(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), true);
				}
				break;
			case 9:
				salir=true;
				System.out.println("Gracias por utilizar este Gestor de Rutas Turisticas");
				break;
			default:
				System.out.println("Solo números entre 1 y 8");
			}
		}
	}
}
