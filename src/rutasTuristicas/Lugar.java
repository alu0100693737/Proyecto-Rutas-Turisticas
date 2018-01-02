package rutasTuristicas;

/**
 * Clase Lugar que almacena información relevante
 * @author Ivan Garcia Campos   alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * Asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * Master en Ingeniería Informática por la ULL
 */
public class Lugar {
	
	/** 
	 * Nombre del lugar 
	 * */
	private String nombreLugar;
	/**
	 * Temática del lugar: Playa, Cultura y monumentos, Parque Temático, etc
	 * */
	private String tematica;
	/**
	 * Puntuación del 1 al 10
	 * */
	private float puntuacion;
	/**
	 * Duración de la actividad en horas
	 * */
	private float duracion;
	
	/**
	 * Constructor de la clase Lugar
	 * @param nombre 		Nombre del lugar
	 * @param tema			Temática del lugar
	 * @param puntua		Puntuación del lugar
	 * @param duracin		Duración de visitar el lugar
	 */
	public Lugar(String nombre, String tema, float puntua, float duracin) {
		nombreLugar = nombre;
		tematica = tema;
		puntuacion = puntua;
		duracion = duracin;
	}
	
	/**
	 * Método para mostrar los atributos de la clase Lugar
	 */
	public void mostrarLugar() {
		System.out.println("Lugar: " + getNombreLugar());
		System.out.println("Tematica: " + getTematica() + ". Puntuacion: " + getPuntuacion() + ". Duracion: " + getDuracion() + "\n");
	}
	
	///GETS
	
	/**
	 * Método que devuelve el Nombre del Lugar
	 * @return String
	 */
	public String getNombreLugar() {
		return nombreLugar;
	}
	
	/**
	 * Método que devuelve la Temática del Lugar
	 * @return String
	 */
	public String getTematica() {
		return tematica;
	}
	
	/**
	 * Método que devuelve la Puntuación del Lugar
	 * @return float (0-10)
	 */
	public float getPuntuacion() {
		return puntuacion;
	}
	
	/**
	 * Método que devuelve la Duración de la visita al Lugar
	 * @return float en horas
	 */
	public float getDuracion() {
		return duracion;
	}
}
