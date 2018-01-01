package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//Algoritmos aleatorio, calculo de tiempo máximo
public class rutaTuristicaAleatoria extends problemaRutasTuristicas {
	
	public rutaTuristicaAleatoria(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		resolverProblema();
	}

	@Override
	public void resolverProblema() {
		
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();
		
		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		for(int k = 0; k < getNumDiasEstancia(); k++) {

			solucionDiaria = new ArrayList<Integer>();
			int minutosAcumulados = 0;
			int maximoComparaciones = 0; //Maximo de comparaciones para decidir que no se puede introducir ningun sitio mas sin sobrepasar la restriccion de tiempo
			
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
						System.out.println("Acumulado " + minutosAcumulados + " minutos");
					}
					maximoComparaciones++;
				}
			}
			
			getSolucionDiaria().add(0);
			System.out.println("Resumen dia " + (k + 1) + " :");
			mostrarConsultaItinerarioDia(getSolucionDiaria());

			//Añadimos 
			getLugaresVisitados().add(getSolucionDiaria());
		}

		System.out.println("\n-----------------------------------------");

		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += calcularValorDiario(getLugaresVisitados().get(i));
			System.out.println(getLugaresVisitados().get(i));
		}
		System.out.println("\nValor total del viaje: " + valorTotalViaje);
	}
}
