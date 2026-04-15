package autonoma.nave_epacial.network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de cargar y centralizar la configuración de red del juego.
 * Implementa el patrón Singleton. La IP siempre viene del lobby,
 * los puertos vienen del archivo .env.
 * @version 1.0
 */
public class NetworkConfig {

    private static final String ENV_FILE = ".env";

    private final int    localPort;
    private final int    remotePort;
    private final String remoteIp;

    private static NetworkConfig instance;

    /** Constructor privado — solo lee puertos del .env, IP viene de afuera. */
    private NetworkConfig(String ipOverride) {
        Map<String, String> env = loadEnv();
        try {
            localPort  = Integer.parseInt(env.get("LOCAL_PORT"));
            remotePort = Integer.parseInt(env.get("REMOTE_PORT"));
            remoteIp   = ipOverride;
            System.out.println("[NET] LOCAL_PORT  = " + localPort);
            System.out.println("[NET] REMOTE_PORT = " + remotePort);
            System.out.println("[NET] REMOTE_IP   = " + remoteIp);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error en .env — verifica LOCAL_PORT y REMOTE_PORT.", e);
        }
    }

    private Map<String, String> loadEnv() {
        Map<String, String> map = new HashMap<>();
        System.out.println("[NET] Leyendo .env: "
                + new java.io.File(ENV_FILE).getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int idx = line.indexOf('=');
                if (idx < 0) continue;
                map.put(line.substring(0, idx).trim(),
                        line.substring(idx + 1).trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("No se encontró .env en: "
                    + new java.io.File(ENV_FILE).getAbsolutePath(), e);
        }
        return map;
    }

    /**
     * Crea la instancia con la IP ingresada en el lobby.
     * Siempre reemplaza la instancia anterior.
     */
    public static void initWithIp(String remoteIp) {
        instance = new NetworkConfig(remoteIp.trim());
        System.out.println("[NET] instancia creada con IP: " + instance.remoteIp);
    }

    /**
     * Retorna la instancia actual.
     * Lanza excepción si no se ha llamado initWithIp primero.
     */
    public static NetworkConfig getInstance() {
        if (instance == null)
            throw new RuntimeException(
                    "NetworkConfig no inicializado — llama initWithIp primero.");
        return instance;
    }

    /** Resetea la instancia al terminar la partida. */
    public static void reset() { instance = null; }

    public int    getLocalPort()  { return localPort;  }
    public int    getRemotePort() { return remotePort; }
    public String getRemoteIp()   { return remoteIp;   }
}
