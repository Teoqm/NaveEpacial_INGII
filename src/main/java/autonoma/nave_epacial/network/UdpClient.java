package autonoma.nave_epacial.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements Runnable {

    private static final int    BUFFER_SIZE = 512;

    private final DatagramSocket socket;
    private final InetAddress    remoteAddress;
    private final int            remotePort;
    private final NetworkObserver observer;
    private volatile boolean     running = true;

    /**
     * @param localPort     puerto en el que ESTA PC escucha
     * @param remoteIp      IP de la otra PC
     * @param remotePort    puerto en el que la OTRA PC escucha
     * @param observer      quien procesará los mensajes recibidos
     */
    public UdpClient(int localPort, String remoteIp, int remotePort,
                     NetworkObserver observer) throws Exception {
        this.socket        = new DatagramSocket(localPort);
        this.remoteAddress = InetAddress.getByName(remoteIp);
        this.remotePort    = remotePort;
        this.observer      = observer;
    }

    /** Envía un mensaje al otro jugador. Falla silenciosamente. */
    public void send(GameMessage message) {
        try {
            byte[] data   = message.serialize().getBytes();
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, remoteAddress, remotePort);
            socket.send(packet);
        } catch (Exception ignored) {
            // UDP: tolerancia a fallos silenciosa
        }
    }

    /** Hilo de escucha — se ejecuta en segundo plano. */
    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String raw = new String(packet.getData(), 0, packet.getLength());
                observer.onMessageReceived(GameMessage.parse(raw));
            } catch (Exception ignored) {
                // Paquete corrupto o error de red: ignorar, no crashear el juego
            }
        }
    }

    public void stop() {
        running = false;
        socket.close();
    }
}