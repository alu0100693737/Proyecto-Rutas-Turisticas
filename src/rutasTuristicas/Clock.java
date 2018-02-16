package rutasTuristicas;
/**
 * Clase Lugar que almacena información relevante
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class Clock {
	
	/**Atributo que indica el tiempo de inicio de ejecución**/
	private long startTime;
	/**Atributo que indica el tiempo de finalización de ejecución**/
	private long endTime;

	/**Constructor de la clase Clock*/
	public Clock() {
		startTime = 0;
		endTime = 0;
	}

	/**
	 * Método que inicia el contador de tiempo
	 */
	public void start() {
		setStartTime();
	}
	/**
	 * Método que para el contador de tiempo
	 */
	public void stop() {
		setEndTime();
	}
	
	/**
	 * Método get que devuelve startTime 
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Método set que inicializa startTime al tiempo actual del sistema
	 */
	public void setStartTime() {
		startTime = System.nanoTime();
	}
	
	/**
	 * Método que inicializa endTime al tiempo actual del sistema
	 */
	public void setEndTime() {
		endTime = System.nanoTime();
	}
	
	/**
	 * Método que devuelve endTime
	 * @return endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	
	/**
	 * Método que calcula la diferencia entre endTime y startTime
	 * @return diferencia
	 */
	public long elapseTime() {
		System.out.println(getEndTime() - getStartTime());
		return getEndTime() - getStartTime();
	}
}