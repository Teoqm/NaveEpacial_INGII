package autonoma.nave_epacial.Io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase ScoreData actúa como un contenedor de datos para representar una entrada
 * en el historial de puntuaciones del juego.
 * Almacena el equipo, puntaje y fecha del registro.
 * @version 1.0
 */
public class ScoreData {

    /** Nombre del equipo que registró el puntaje. */
    private String team;
    /** Representación textual de la fecha (formato yyyy-MM-dd HH:mm). */
    private String date;
    /** El valor numérico de la puntuación alcanzada. */
    private int score;

    /**
     * Constructor principal con equipo y puntaje.
     * La fecha se genera automáticamente.
     *
     * @param team  nombre del equipo
     * @param score puntaje total
     */
    public ScoreData(String team, int score) {
        this.team  = team;
        this.score = score;
        this.date  = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(new Date(System.currentTimeMillis()));
    }

    /**
     * Constructor vacío para deserialización desde JSON.
     */
    public ScoreData() {
        this.team  = "Desconocido";
        this.date  = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(new Date(System.currentTimeMillis()));
        this.score = 0;
    }

    public String getTeam()             { return team;  }
    public void   setTeam(String team)  { this.team  = team; }
    public String getDate()             { return date;  }
    public void   setDate(String date)  { this.date  = date; }
    public int    getScore()            { return score; }
    public void   setScore(int score)   { this.score = score; }
}