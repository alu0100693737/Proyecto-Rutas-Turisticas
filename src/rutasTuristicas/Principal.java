package rutasTuristicas;

public class Principal {
	public static void main(String[] args) throws WrongParameterException {
		try {
			rutaTuristicaAleatoria prueba = new rutaTuristicaAleatoria(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

		} catch (Exception e) {
			throw new WrongParameterException("Debe introducir los parametros adecuados para ejecutar este programa. \n"
					+ "1.Lugares. \n2.Matriz de Distancias. \n3.Matriz de Tiempos.\n4.Num de Dias de la Estancia.\n5.Num de Horas Diarias");
		}
	}
}
