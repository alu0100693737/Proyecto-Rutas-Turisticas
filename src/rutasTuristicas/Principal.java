package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
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

		while(!salir){

			System.out.println("1. Algoritmo Aleatorio");
			System.out.println("2. Algoritmo Greedy");
			System.out.println("3. Algoritmo GRASP");
			System.out.println("4. Algoritmo GRASP con Busqueda Local 2 a 1");
			System.out.println("5. Algoritmo GRASP con Busqueda Local, Intercambio entre dias");
			System.out.println("6. Salir");

			System.out.println("Escribe una de las opciones");
			opcion = sn.nextInt();

			switch(opcion){
			case 1:
				System.out.println("Algoritmo Aleatorio");
				rutaTuristicaAleatoria aleatorio = new rutaTuristicaAleatoria(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				break;
			case 2:
				System.out.println("Algoritmo Greedy Ponderado");
				rutaTuristicaGreedyPonderado greedy = new rutaTuristicaGreedyPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				break;
			case 3:
				System.out.println("Algoritmo GRASP Ponderado");
				rutaTuristicaGRASPPonderado grasp = new rutaTuristicaGRASPPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				break;
			case 4:
				System.out.println("Algoritmo GRASP con Busqueda local 2 a 1");
				break;
			case 5:
				System.out.println("Algoritmo GRASP con Busqueda local intercambio entre dias");
			case 6:
				salir=true;
				System.out.println("Gracias por utilizar este Gestor de Rutas Turisticas");
				break;
			default:
				System.out.println("Solo números entre 1 y 6");
			}
		}
	}
}
