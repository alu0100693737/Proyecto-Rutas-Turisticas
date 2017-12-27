package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//Algoritmos aleatorio, calculo de tiempo máximo
public class rutaTuristicaAleatoria extends problemaRutasTuristicas {

	public rutaTuristicaAleatoria(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);
		
		/*for(int i = 0; i < getLugaresTuristicosDisponibles().getNumLugares(); i++)
			getLugaresTuristicosDisponibles().getLugaresTuristicos().get(i).mostrarLugar();*/
			
		resolverProblema();
	}

	@Override
	public void resolverProblema() {

		solucionDiaria = new ArrayList<Integer>();
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();

		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		for(int k = 0; k < getNumDiasEstancia(); k++) {
			int minutosTotales = getNumHorasDiarias() * 60;
			int minutosAcumulados = 0;

			System.out.println("Dia numero " + (k + 1));
			//Cada dia
			//Añadimos el primer elemento, de donde partimos
			getSolucionDiaria().add(0);
			int maximoComparaciones = 0;

			//Maximo de comparaciones posibles
			while (maximoComparaciones < 56) {

				int elegido = (int)(Math.random() * getLugaresTuristicosDisponibles().getNumLugares());
				
				//Si aun no se ha visitado el lugar
				if(!getSolucionDiaria().contains(elegido)) {
					//Si no ha sido visitado los dias anteriores
					boolean visitadoDiasAnteriores = false;
					for(int l = 0; l < getLugaresVisitados().size(); l++)
						if(getLugaresVisitados().get(l).contains(elegido)) 
							visitadoDiasAnteriores = true;
					if(visitadoDiasAnteriores == false) {
						//Con el tiempo que nos queda, si sumamos el tiempo en llegar alli, la duracion de la actividad y cuanto tardamos en volver a 0 si lo elegimos, es menor que la hora maxima
						if((minutosAcumulados + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60) +  getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[elegido][0]) < minutosTotales) {
							System.out.println("\nSe añade: " + elegido);
							getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).mostrarLugar();
							System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + " minutos");
							System.out.println("La actividad tiene una duracion de " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60);

							//Reseteamos las comparaciones
							maximoComparaciones = 0;
							minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60);

							//System.out.println("Se ha añadido " + elegido + " con " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[solucionDiaria.get(solucionDiaria.size() - 1)][elegido] + " Min distancia");
							getSolucionDiaria().add(elegido);
							System.out.println("Acumulado " + minutosAcumulados + " minutos");
						}
						maximoComparaciones++;
					}
				}
			}

			//Añadimos distancia del ultimo al primer elemento
			minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][0];
			System.out.println("Añadiendo la distancia hasta el lugar de inicio : " + minutosAcumulados + " \n\n");

			System.out.println("\n\n\n.---------------------------------------------------------------------------------");
			System.out.println("Resumen dia " + (k + 1) + " :");
			for(int i = 0; i < getSolucionDiaria().size(); i++) {
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
			}
			System.out.println("Minutos acumulados " + minutosAcumulados);

			//Añadimos 
			getLugaresVisitados().add(getSolucionDiaria());
			getSolucionDiaria().clear();
			System.out.println("\n\n\n");
		}
	}
}
