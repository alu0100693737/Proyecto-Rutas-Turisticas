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
	
	protected ArrayList<Float> valoresDiarios;
	protected ArrayList<Integer> tiemposDiarios;
	
	public problemaRutasTuristicas(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
		lugaresTuristicosDisponibles_ = new puntosTuristicos(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos);
		numDiasEstancia_ = numDias;
		numHorasDiarias_ = numHorasDia;
	}
	
	public abstract void resolverProblema();
	
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
		System.out.println("\nValor total del viaje: " + (valorTotalViaje));
		
	}
	
	public void mostrarConsultaItinerarioDia(ArrayList<Integer> itinerario) {
		for(int i = 0; i < itinerario.size(); i++) {
			getLugaresTuristicosDisponibles().getLugaresTuristicos().get(itinerario.get(i)).mostrarLugar();;
		}
	}
	//Utilizado en la resolucion de problemas
	public ArrayList<Float> getValoresDiarios() {
		return valoresDiarios;
	}
	
	public ArrayList<Integer> getTiemposDiarios() {
		return tiemposDiarios;
	}
}
