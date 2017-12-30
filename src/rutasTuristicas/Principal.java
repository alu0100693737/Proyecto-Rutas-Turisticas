package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Principal {
	public static void main(String[] args) throws NumberFormatException, FileNotFoundException, IOException{
		
			//rutaTuristicaAleatoria prueba = new rutaTuristicaAleatoria(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

			//rutaTuristicaGreedyPonderado prueba = new rutaTuristicaGreedyPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

			rutaTuristicaGRASPPonderado prueba = new rutaTuristicaGRASPPonderado(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

	}
}
