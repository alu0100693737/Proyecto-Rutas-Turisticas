package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public abstract class problemaRutasTuristicas {
	private int numDiasEstancia_;
	private int numHorasDiarias_;

	private puntosTuristicos lugaresTuristicosDisponibles_;

	//Resolucion de Problemas
	protected ArrayList<Integer> solucionDiaria;
	protected ArrayList<ArrayList<Integer>> lugaresVisitados;
	protected ArrayList<Integer> kilometrosViaje;

	public problemaRutasTuristicas(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		lugaresTuristicosDisponibles_ = new puntosTuristicos(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos);
		numDiasEstancia_ = numDias;
		numHorasDiarias_ = numHorasDia;
	}

	public abstract void resolverProblema();

	//Calcula el valor de visitar un array de sitios
	public float calcularValorDiario(ArrayList<Integer> dia) {
		float aux = 0;
		for(int i = 1; i < (dia.size() - 1); i++) {
			aux += getLugaresTuristicosDisponibles().getMatrizDistancias().getMatrizDistancias()[dia.get(i - 1)][dia.get(i)] /
					getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getPuntuacion();
		}
		return aux;
	}

	public int calcularTiempoEmpleado(ArrayList<Integer> dia) {
		//dia = new ArrayList<Integer>();
		//dia.add(0); dia.add(1); dia.add(2); dia.add(3); dia.add(0);
		int tiempoDiario = 0; 
		System.out.println("DIA : " + dia);
		for(int i = 1; i < (dia.size() - 1); i++) {
			System.out.println("PEPE "+  dia.get(i - 1) + " areg " + dia.get(i));
			System.out.println(" A " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(i - 1)][dia.get(i)] + " B " + getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getDuracion() * 60);
			tiempoDiario += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(i - 1)][dia.get(i)];
			tiempoDiario += getLugaresTuristicosDisponibles().getLugaresTuristicos().get(dia.get(i)).getDuracion() * 60;
		}
		System.out.println("ULTIMA SUMA " + getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(dia.size() - 2)][0]);
		tiempoDiario += getLugaresTuristicosDisponibles().getMatrizTiempos().getMatrizTiempos()[dia.get(dia.size() - 2)][0];
		return tiempoDiario;
	}

	public boolean yaVisitado(int lugar, ArrayList<ArrayList<Integer>> diasAnteriores, ArrayList<Integer> diaActual) {
		boolean yaVisitado = false;

		//Si aun no ha sido visitado ese dia o los anteriores si los hubiera
		for(int l = 0; l < diasAnteriores.size(); l++) 
			if(diasAnteriores.get(l).contains(lugar)) 
				yaVisitado = true;

		if(getSolucionDiaria().contains(lugar)) 
			yaVisitado = true;

		return yaVisitado;
	}

	public void mostrarItinerarioViaje() {
		System.out.println("\nResumen del itinerario: ");

		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			System.out.println("\nDia " + (i + 1) + ": ");

			for(int j = 0; j < getLugaresVisitados().get(i).size(); j++) {
				getLugaresTuristicosDisponibles().getLugaresTuristicos().get(getLugaresVisitados().get(i).get(j)).mostrarLugar();
			}
			System.out.println("Tiempo utilizado: " + calcularTiempoEmpleado(getLugaresVisitados().get(i)) + " min");
			System.out.println("Valor Acumulado diario: " + calcularValorDiario(getLugaresVisitados().get(i)));
		}

		float valorTotalViaje = 0;
		for(int i = 0; i < getLugaresVisitados().size(); i++) {
			valorTotalViaje += calcularValorDiario(getLugaresVisitados().get(i));
		}
		System.out.println("\nValor total del viaje: " + (valorTotalViaje));

	}

	public void mostrarConsultaItinerarioDia(ArrayList<Integer> itinerario) {
		for(int i = 0; i < itinerario.size(); i++) {
			getLugaresTuristicosDisponibles().getLugaresTuristicos().get(itinerario.get(i)).mostrarLugar();
		}
	}

	public int getNumDiasEstancia() {
		return numDiasEstancia_;
	}

	public int getNumHorasDiarias() {
		return numHorasDiarias_;
	}

	public puntosTuristicos getLugaresTuristicosDisponibles() {
		return lugaresTuristicosDisponibles_;
	}

	public ArrayList<Integer> getSolucionDiaria() {
		return solucionDiaria;
	}

	public ArrayList<ArrayList<Integer>> getLugaresVisitados() {
		return lugaresVisitados;
	}

	public ArrayList<Integer> getKilometrosViaje() {
		return kilometrosViaje;
	}

}
