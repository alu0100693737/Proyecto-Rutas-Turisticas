package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//https://www.sciencedirect.com/science/article/pii/S0213131514000029
//funcion de probabilidad utilizada

public class rutaTuristicaRecocidoSimulado extends problemaRutasTuristicas {

	//Heuristica por probabilidad,
	//Si mejora se acepta, si no se puede aceptar con una cierta probabilidad. La probabilidad disminuye cada vez más siguiendo un criterio
	
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

			solucionDiaria = new ArrayList<Integer>();
			int minutosAcumulados = 0;
			//Maximo de comparaciones para decidir que no se puede introducir ningun sitio mas sin sobrepasar la restriccion de tiempo
			int maximoComparaciones = 0; 			
			//Añadimos el primer elemento, de donde partimos
			getSolucionDiaria().add(0);
			System.out.println("Dia numero " + (k + 1));

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
			float temperatura = 35;
			
			ArrayList<Integer> recocidoSimulado = new ArrayList<Integer>(aplicarRecocidoSimulado(getSolucionDiaria(), temperatura));
			if(!getSolucionDiaria().equals(recocidoSimulado)) {
				System.out.println("Cambio en la solucion, imprimimos de nuevo el itinerario: ");
				solucionDiaria = new ArrayList<Integer>(recocidoSimulado);
				System.out.println("Visita actual " + getSolucionDiaria());
				System.out.println("Valor actual " + calcularValorDiario(getSolucionDiaria()));
				System.out.println("Tiempo actual " + calcularTiempoEmpleado(getSolucionDiaria()));
				System.out.println("Kilometros actual " + calcularKilometrosEmpleado(getSolucionDiaria()));
				System.out.println("Visita actual " + getSolucionDiaria());
			} else {
				System.out.println("Opcion peor");
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
	
	ArrayList<Integer> aplicarRecocidoSimulado(ArrayList<Integer> lugares, float temperatura) {
		return new ArrayList<Integer>();
	}

}