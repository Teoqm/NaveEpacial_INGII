package autonoma.nave_epacial.network;

/**
 * La clase GameMessage representa el paquete de datos estandarizado utilizado para la
 * comunicación en red mediante el protocolo UDP.
 * Permite encapsular un tipo de acción y sus datos asociados en una estructura
 * que puede ser fácilmente enviada y reconstruida entre diferentes instancias del juego.
 * * @version 1.0
 */
public class GameMessage {

    /** El tipo de mensaje que define la naturaleza de la acción enviada. */
    public final MessageType type;
    /** Los datos o parámetros específicos que acompañan al mensaje. */
    public final String[]    data;

    /**
     * Construye un nuevo mensaje de juego.
     * * @param type El {@link MessageType} que identifica el propósito del mensaje.
     * @param data Lista variable de argumentos de tipo String que contienen la información.
     */
    public GameMessage(MessageType type, String... data) {
        this.type = type;
        this.data = data;
    }

    /** * Convierte el objeto GameMessage en una cadena de texto plana para su transmisión.
     * Utiliza el carácter "|" como delimitador entre el nombre del tipo y cada dato.
     * * @return Una cadena de texto serializada lista para ser enviada por un socket UDP.
     */
    public String serialize() {
        return type.name() + "|" + String.join("|", data);
    }

    /** * Reconstruye un objeto GameMessage a partir de una cadena de texto recibida.
     * * @param raw La cadena de texto cruda (delimitada por "|") recibida desde la red.
     * @return Una nueva instancia de {@link GameMessage} con los datos procesados.
     * @throws IllegalArgumentException Si el formato de la cadena o el tipo de mensaje no son válidos.
     */
    public static GameMessage parse(String raw) {
        String[] parts = raw.trim().split("\\|");
        MessageType type = MessageType.valueOf(parts[0]);
        String[] data = new String[parts.length - 1];
        System.arraycopy(parts, 1, data, 0, data.length);
        return new GameMessage(type, data);
    }
}