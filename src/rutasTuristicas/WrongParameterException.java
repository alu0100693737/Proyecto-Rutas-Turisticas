package rutasTuristicas;
/**
 * Clase heredada de Excepcion para recoger excepciones y lanzar mensaje
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class WrongParameterException extends Exception {
	public WrongParameterException ( String msg ) {
		super( msg ) ;
	}
}
