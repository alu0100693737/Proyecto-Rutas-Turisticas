package rutasTuristicas;

/**
 * 
 * @author Ivan Garcia
 * Clase para la definicion de Lugares 
 */
public class Lugar {
	private String nombreLugar;
	private String tematica;
	private float puntuacion;
	private float duracion;
	
	public Lugar(String nombre, String tema, float puntua, float duracin) {
		nombreLugar = nombre;
		tematica = tema;
		puntuacion = puntua;
		duracion = duracin;
	}
	
	public String getNombreLugar() {
		return nombreLugar;
	}
	
	public String getTematica() {
		return tematica;
	}
	
	public float getPuntuacion() {
		return puntuacion;
	}
	
	public float getDuracion() {
		return duracion;
	}
}
