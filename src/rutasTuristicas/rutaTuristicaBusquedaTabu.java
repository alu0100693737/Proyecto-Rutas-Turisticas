package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;

public class rutaTuristicaBusquedaTabu extends problemaRutasTuristicas {
	//Heuristica por probabilidad,
		//Si mejora se acepta, si no se puede aceptar con una cierta probabilidad. La probabilidad disminuye cada vez más siguiendo un criterio
		
		public rutaTuristicaBusquedaTabu(String ficheroLugares, String ficheroMatrizDistancias, String ficheroMatrizTiempos, int numDias, int numHorasDia) throws FileNotFoundException, IOException {
			super(ficheroLugares, ficheroMatrizDistancias, ficheroMatrizTiempos, numDias, numHorasDia);

			resolverProblema(true);		

		}

		@Override
		public void resolverProblema(boolean Estrategia) {
			System.out.println("ERROR");
		}

}
