package rutasTuristicas;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Principal {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		puntosTuristicos prueba = new puntosTuristicos(args[0],args[1], args[2]);
	}
}
