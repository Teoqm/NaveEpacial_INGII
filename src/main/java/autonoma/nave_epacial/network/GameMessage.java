package autonoma.nave_epacial.network;

public class GameMessage {

    public final MessageType type;
    public final String[]    data;

    public GameMessage(MessageType type, String... data) {
        this.type = type;
        this.data = data;
    }

    /** Convierte el mensaje a String para enviarlo por UDP */
    public String serialize() {
        return type.name() + "|" + String.join("|", data);
    }

    /** Reconstruye un GameMessage desde el String recibido por UDP */
    public static GameMessage parse(String raw) {
        String[] parts = raw.trim().split("\\|");
        MessageType type = MessageType.valueOf(parts[0]);
        String[] data = new String[parts.length - 1];
        System.arraycopy(parts, 1, data, 0, data.length);
        return new GameMessage(type, data);
    }
}