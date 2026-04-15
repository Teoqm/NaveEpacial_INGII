package autonoma.nave_epacial.network;

/**
 * La interfaz NetworkObserver define el contrato para los objetos que desean
 * ser notificados cuando se recibe un nuevo mensaje a través de la red.
 * Implementa el patrón de diseño Observer, permitiendo que diferentes componentes
 * del juego (como el estado de la partida) reaccionen a los datos entrantes
 * de manera asíncrona sin estar acoplados directamente al socket UDP.
 * * @version 1.0
 */
public interface NetworkObserver {

    /**
     * Método invocado automáticamente cuando el cliente de red procesa
     * un paquete entrante y lo convierte en un mensaje de juego válido.
     * * @param message El objeto {@link GameMessage} que contiene el tipo
     * de evento y los datos recibidos desde el par remoto.
     */
    void onMessageReceived(GameMessage message);
}