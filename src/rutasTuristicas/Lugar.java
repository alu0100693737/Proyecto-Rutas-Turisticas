/**
 * Clase Lugar que almacena información relevante
 * @author Ivan Garcia Campos   
 * @email alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see asignatura "Sistemas Inteligentes e Interacción Persona Computador"
 * @see Master en Ingeniería Informática por la ULL
 */

package rutasTuristicas;

public class Lugar {
	/** Nombre del lugar */
	private String nombreLugar;
	/**Temática del lugar: Playa, Cultura y monumentos, Parque Temático, etc*/
	private String tematica;
	/**Puntuacion del 1 al 10*/
	private float puntuacion;
	/**Duracion de la actividad en horas*/
	private float duracion;
	
	/**
	 * Constructor de la clase Lugar
	 * @param nombre
	 * @param tema
	 * @param puntua
	 * @param duracin
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
	
	/**
	 * Método que devuelve el Nombre del Lugar
	 * @return String
	 */
	public String getNombreLugar() {
		return nombreLugar;
	}
	
	/**
	 * Método que devuelve la Tematica del Lugar
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
