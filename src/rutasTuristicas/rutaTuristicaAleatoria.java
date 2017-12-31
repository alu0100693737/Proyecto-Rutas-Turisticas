package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//Algoritmos aleatorio, calculo de tiempo máximo
public class rutaTuristicaAleatoria extends problemaRutasTuristicas {

	private ArrayList<Float> valoresDiarios;
	public rutaTuristicaAleatoria(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

		resolverProblema();
	}

	@Override
	public void resolverProblema() {

		
		lugaresVisitados =  new ArrayList<ArrayList<Integer>>();
		kilometrosViaje = new ArrayList<Integer>();
		valoresDiarios = new ArrayList<Float>();

		System.out.println("Algoritmo Aleatorio");
		System.out.println("Tenemos " + getNumDiasEstancia() + " dias de estancia con " + getNumHorasDiarias() + " horas de visita a la isla.");
		System.out.println("minutos totales diarios " + getNumHorasDiarias() * 60);
		System.out.println("\n");

		
		float valorAcumulado;
		for(int k = 0; k < getNumDiasEstancia(); k++) {
			
			solucionDiaria = new ArrayList<Integer>();
			int minutosTotales = getNumHorasDiarias() * 60;
			int minutosAcumulados = 0;
			
			//Kilometros diarios hechos en la ruta elegida
			int kilometrosDiarios = 0;
			
			valorAcumulado = 0;

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
						if((minutosAcumulados + 
								getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + 
								(getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60) +  
								getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[elegido][0]) 
																							< minutosTotales) {
							
							System.out.println("\nSe añade: " + elegido);
							
							valorAcumulado += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] /
									getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getPuntuacion();
							
							getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).mostrarLugar();
							System.out.println("Se tarda en llegar " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + " minutos");
							System.out.println("La actividad tiene una duracion de " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60 + " minutos");
							System.out.println("Kilometros hechos para llegar : " + getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + " km");
							//Reseteamos las comparaciones
							maximoComparaciones = 0;
							minutosAcumulados += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido] + (getLugaresTuristicosDisponibles().getLugaresTuristicos().get(elegido).getDuracion() * 60);
							kilometrosDiarios += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][elegido];
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
			kilometrosDiarios += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[getSolucionDiaria().get(getSolucionDiaria().size() - 1)][0];
			
			System.out.println("\nAñadiendo la distancia hasta el lugar de inicio : " + minutosAcumulados + " min");
			System.out.println("Añadiendo los kilometros hasta el lugar de inicio: " + kilometrosDiarios + " km\n\n");

			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("Resumen dia " + (k + 1) + " :");
			for(int i = 0; i < getSolucionDiaria().size(); i++) {
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getSolucionDiaria().get(i)).mostrarLugar();
				
			}
			System.out.println("Minutos acumulados " + minutosAcumulados + " min");
			System.out.println("Kilometros diarios " + kilometrosDiarios + " km");
			System.out.println("Valor Acumulado diario: " + valorAcumulado);

			//Añadimos 
			getLugaresVisitados().add(getSolucionDiaria());
			getKilometrosViaje().add(kilometrosDiarios);
			
			getValoresDiarios().add(valorAcumulado);
		}
		
		System.out.println("\n-----------------------------------------");
		
		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += getValoresDiarios().get(i);
			System.out.println(getLugaresVisitados().get(i));
		}
		
		System.out.println("\nValor total del viaje: " + valorTotalViaje);
		
	}
	
	public ArrayList<Float> getValoresDiarios() {
		return valoresDiarios;
	}
}
