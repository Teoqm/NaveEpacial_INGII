package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.NetworkConfig;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Estado que representa la pantalla de lobby del juego.
 *
 * Permite a los dos jugadores locales ingresar sus nombres y la IP
 * de la otra PC antes de iniciar la partida.
 *
 * El flujo es:
 * J1 escribe nombre → ENTER → J2 escribe nombre → ENTER
 * → escribe IP remota → ENTER → inicia GameState.
 *
 * @version 1.0
 */
public class LobbyState extends State {

    /** Nombre ingresado por el jugador 1. */
    private String nombre1 = "";

    /** Nombre ingresado por el jugador 2. */
    private String nombre2 = "";

    /** IP de la otra PC ingresada por el usuario. */
    private String remoteIp = "";

    /**
     * Campo activo:
     * 1 = nombre jugador 1
     * 2 = nombre jugador 2
     * 3 = IP remota
     */
    private int campoActivo = 1;

    /** Límite máximo de caracteres para nombres. */
    private static final int MAX_CHARS = 12;

    /** Límite máximo de caracteres para la IP. */
    private static final int MAX_IP_CHARS = 15;

    public LobbyState() {}

    @Override
    public void update() throws IOException {}

    /**
     * Procesa los eventos de teclado para capturar nombres e IP.
     * BACKSPACE elimina el último carácter del campo activo.
     * ENTER confirma el campo actual y avanza al siguiente.
     * TAB alterna entre campos.
     *
     * @param e evento de teclado recibido desde Window
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_BACK_SPACE) {
            if (campoActivo == 1 && nombre1.length() > 0)
                nombre1 = nombre1.substring(0, nombre1.length() - 1);
            else if (campoActivo == 2 && nombre2.length() > 0)
                nombre2 = nombre2.substring(0, nombre2.length() - 1);
            else if (campoActivo == 3 && remoteIp.length() > 0)
                remoteIp = remoteIp.substring(0, remoteIp.length() - 1);
            return;
        }

        if (key == KeyEvent.VK_ENTER) {
            if (campoActivo == 1 && !nombre1.isEmpty()) {
                campoActivo = 2;
            } else if (campoActivo == 2 && !nombre2.isEmpty()) {
                campoActivo = 3;
            } else if (campoActivo == 3 && !remoteIp.isEmpty()) {
                String ipLimpia = remoteIp.trim();
                System.out.println("[LOBBY] IP ingresada: '" + ipLimpia + "'");
                NetworkConfig.initWithIp(ipLimpia);
                System.out.println("[LOBBY] IP configurada: "
                        + NetworkConfig.getInstance().getRemoteIp()
                        + " | LOCAL: " + NetworkConfig.getInstance().getLocalPort()
                        + " | REMOTO: " + NetworkConfig.getInstance().getRemotePort());
                State.changeState(new GameState(nombre1, nombre2));
            }
            return;
        }

        if (key == KeyEvent.VK_TAB) {
            if      (campoActivo == 1 && !nombre1.isEmpty()) campoActivo = 2;
            else if (campoActivo == 2 && !nombre2.isEmpty()) campoActivo = 3;
            else if (campoActivo == 3)                        campoActivo = 1;
            return;
        }

        char c = e.getKeyChar();

        if (campoActivo == 3) {
            // IP: solo números y puntos
            if ((Character.isDigit(c) || c == '.') && remoteIp.length() < MAX_IP_CHARS)
                remoteIp += c;
        } else {
            // Nombres: letras, números y espacios
            if (Character.isLetterOrDigit(c) || c == ' ') {
                if (campoActivo == 1 && nombre1.length() < MAX_CHARS)
                    nombre1 += c;
                else if (campoActivo == 2 && nombre2.length() < MAX_CHARS)
                    nombre2 += c;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        Text.drawText(g2d, "INGRESA LOS NOMBRES Y IP",
                new Vector2D(Constants.WIDTH / 2, 120),
                true, Color.YELLOW, Assets.fontBig);

        // Campo jugador 1
        Color c1 = campoActivo == 1 ? Color.CYAN : Color.WHITE;
        Text.drawText(g2d, "Jugador 1 (Flechas):",
                new Vector2D(Constants.WIDTH / 2, 220),
                true, Color.GRAY, Assets.fontMed);
        String d1 = nombre1 + (campoActivo == 1 ? "|" : "");
        Text.drawText(g2d, d1.isEmpty() ? "_" : d1,
                new Vector2D(Constants.WIDTH / 2, 255),
                true, c1, Assets.fontMed);

        // Campo jugador 2
        Color c2 = campoActivo == 2 ? Color.CYAN : Color.WHITE;
        Text.drawText(g2d, "Jugador 2 (WASD):",
                new Vector2D(Constants.WIDTH / 2, 315),
                true, Color.GRAY, Assets.fontMed);
        String d2 = nombre2 + (campoActivo == 2 ? "|" : "");
        Text.drawText(g2d, d2.isEmpty() ? "_" : d2,
                new Vector2D(Constants.WIDTH / 2, 350),
                true, c2, Assets.fontMed);

        // Campo IP
        Color c3 = campoActivo == 3 ? Color.CYAN : Color.WHITE;
        Text.drawText(g2d, "IP de la otra PC:",
                new Vector2D(Constants.WIDTH / 2, 410),
                true, Color.GRAY, Assets.fontMed);
        String d3 = remoteIp + (campoActivo == 3 ? "|" : "");
        Text.drawText(g2d, d3.isEmpty() ? "_" : d3,
                new Vector2D(Constants.WIDTH / 2, 445),
                true, c3, Assets.fontMed);

        // Instrucción
        Text.drawText(g2d, "ENTER para confirmar cada campo",
                new Vector2D(Constants.WIDTH / 2, 530),
                true, Color.DARK_GRAY, Assets.fontMed);

        // Mostrar campo activo como guía
        String guia = campoActivo == 1 ? "Escribe el nombre del Jugador 1"
                : campoActivo == 2 ? "Escribe el nombre del Jugador 2"
                : "Escribe la IP de la otra PC (ej: 192.168.1.5)";
        Text.drawText(g2d, guia,
                new Vector2D(Constants.WIDTH / 2, 580),
                true, Color.YELLOW, Assets.fontMed);
    }
}