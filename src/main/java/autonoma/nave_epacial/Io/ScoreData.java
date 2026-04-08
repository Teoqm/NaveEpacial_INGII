package autonoma.nave_epacial.Io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase ScoreData actúa como un contenedor de datos para representar una entrada
 * en el historial de puntuaciones del juego.
 * Almacena el puntaje obtenido y la fecha en que se registró, permitiendo la
 * serialización y visualización del desempeño del jugador a lo largo del tiempo.
 * * @version 1.0
 */
public class ScoreData {

    /** Representación textual de la fecha en que se alcanzó el puntaje (formato yyyy-MM-dd). */
    private String date;
    /** El valor numérico de la puntuación alcanzada. */
    private int score;

    /**
     * Construye una nueva instancia de ScoreData con un puntaje específico.
     * La fecha se genera automáticamente capturando el tiempo actual del sistema
     * y formateándolo como una cadena "año-mes-día".
     * * @param score El puntaje total a registrar.
     */
    public ScoreData(int score) {
        this.score = score;

        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(today);
    }

    /**
     * Constructor vacío necesario para procesos de persistencia y
     * deserialización manual (ej. carga desde JSON).
     */
    public ScoreData() {
    }

    /**
     * Obtiene la fecha del registro.
     * * @return Cadena de texto con la fecha en formato yyyy-MM-dd.
     */
    public String getDate() {
        return date;
    }

    /**
     * Establece manualmente la fecha del registro.
     * * @param date Nueva fecha en formato de cadena.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Obtiene el puntaje almacenado.
     * * @return Valor entero del puntaje.
     */
    public int getScore() {
        return score;
    }

    /**
     * Establece el valor del puntaje.
     * * @param score Nuevo puntaje numérico.
     */
    public void setScore(int score) {
        this.score = score;
    }
}