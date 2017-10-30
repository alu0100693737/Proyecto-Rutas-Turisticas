package rutasTuristicas;

/**
 * Clase heredada de Excepcion para recoger excepciones y lanzar mensaje
 * Usado en Excepciones2 y Excepciones3
 * 
 * @author Ivan Garcia Campos   
 * @email alu0100693737@ull.edu.es
 * @version 1.0, 30/10/2017
 * @see asignatura "Sistemas Inteligentes"
 */

public class WrongParameterException extends Exception {
	public WrongParameterException ( String msg ) {
		super( msg ) ;
	}
}
