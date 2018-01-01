/**
 * Clase Lugar que almacena informaci�n relevante
 * @author Ivan Garcia Campos   
 * @email alu0100693737@ull.edu.es
 * @version 1.0, 01/01/2018
 * @see asignatura "Sistemas Inteligentes e Interacci�n Persona Computador"
 * @see Master en Ingenier�a Inform�tica por la ULL
 */

package rutasTuristicas;

public class Lugar {
	/** Nombre del lugar */
	private String nombreLugar;
	/**Tem�tica del lugar: Playa, Cultura y monumentos, Parque Tem�tico, etc*/
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
	 * M�todo para mostrar los atributos de la clase Lugar
	 */
	public void mostrarLugar() {
		System.out.println("Lugar: " + getNombreLugar());
		System.out.println("Tematica: " + getTematica() + ". Puntuacion: " + getPuntuacion() + ". Duracion: " + getDuracion() + "\n");
	}
	
	/**
	 * M�todo que devuelve el Nombre del Lugar
	 * @return String
	 */
	public String getNombreLugar() {
		return nombreLugar;
	}
	
	/**
	 * M�todo que devuelve la Tematica del Lugar
	 * @return String
	 */
	public String getTematica() {
		return tematica;
	}
	
	/**
	 * M�todo que devuelve la Puntuaci�n del Lugar
	 * @return float (0-10)
	 */
	public float getPuntuacion() {
		return puntuacion;
	}
	
	/**
	 * M�todo que devuelve la Duraci�n de la visita al Lugar
	 * @return float en horas
	 */
	public float getDuracion() {
		return duracion;
	}
}
