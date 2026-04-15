package autonoma.nave_epacial.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UdpClient implements Runnable {

    private static final int BUFFER_SIZE = 512;

    private DatagramSocket   socket;
    private final InetAddress    remoteAddress;
    private final int            remotePort;
    private final NetworkObserver observer;
    private volatile boolean     running = true;

    /**
     * @param localPort  puerto en el que ESTA PC escucha
     * @param remoteIp   IP de la otra PC
     * @param remotePort puerto en el que la OTRA PC escucha
     * @param observer   quien procesará los mensajes recibidos
     */
    public UdpClient(int localPort, String remoteIp, int remotePort,
                     NetworkObserver observer) throws Exception {
        // SO_REUSEADDR permite reutilizar el puerto inmediatamente
        // después de cerrar la conexión anterior
        DatagramSocket sock = new DatagramSocket(null);
        sock.setReuseAddress(true);
        sock.bind(new InetSocketAddress(localPort));
        this.socket        = sock;
        this.remoteAddress = InetAddress.getByName(remoteIp);
        this.remotePort    = remotePort;
        this.observer      = observer;
        System.out.println("[UDP] Socket abierto en puerto " + localPort);
    }

    /** Envía un mensaje al otro jugador. Falla silenciosamente. */
    public void send(GameMessage message) {
        try {
            if (socket == null || socket.isClosed()) return;
            byte[] data = message.serialize().getBytes();
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, remoteAddress, remotePort);
            socket.send(packet);
        } catch (Exception ignored) {}
    }

    /** Hilo de escucha — se ejecuta en segundo plano. */
    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (running) {
            try {
                if (socket == null || socket.isClosed()) break;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String raw = new String(packet.getData(), 0, packet.getLength());
                observer.onMessageReceived(GameMessage.parse(raw));
            } catch (Exception ignored) {}
        }
    }

    /** Cierra el socket limpiamente. */
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;
        System.out.println("[UDP] Socket cerrado.");
    }
}