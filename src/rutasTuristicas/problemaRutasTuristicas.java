package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class problemaRutasTuristicas {
	private int numDiasEstancia_;
	private int numHorasDiarias_;
	
	private puntosTuristicos lugaresTuristicosDisponibles_;
	
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
}