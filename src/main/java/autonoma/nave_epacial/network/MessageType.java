package autonoma.nave_epacial.network;

/**
 * El enumerador MessageType define los tipos de eventos y comandos que pueden ser
 * transmitidos a través de la red en una sesión multijugador.
 * Estos identificadores permiten al sistema de red discernir cómo procesar los
 * datos recibidos, separando las acciones del Jugador 1 de las del Jugador 2,
 * así como los eventos globales de juego.
 * * @version 1.0
 */
public enum MessageType {
    /** Indica que un jugador ha ganado la partida. */
    WINNER,

    /** Notifica la destrucción de un enemigo (OVNI) en la red. */
    ENEMY_DIED,

    // --- Jugador 1 ---

    /** Sincroniza la posición, ángulo y visibilidad del Jugador 1. */
    PLAYER_STATE,

    /** Notifica que el Jugador 1 ha cruzado el límite lateral hacia la otra pantalla. */
    PLAYER_CROSS,

    /** Indica la creación de un proyectil láser por parte del Jugador 1. */
    SPAWN_LASER,

    /** Gestiona el paso de un láser del Jugador 1 entre pantallas. */
    LASER_CROSS,

    /** Gestiona el paso de un meteoro entre pantallas relacionado con el Jugador 1. */
    METEOR_CROSS,

    /** Notifica que el Jugador 1 ha perdido una vida o ha muerto. */
    YOU_DIED,

    // --- Jugador 2 ---

    /** Sincroniza la posición, ángulo y visibilidad del Jugador 2. */
    PLAYER2_STATE,

    /** Notifica que el Jugador 2 ha cruzado el límite lateral hacia la otra pantalla. */
    PLAYER2_CROSS,

    /** Indica la creación de un proyectil láser por parte del Jugador 2. */
    SPAWN_LASER2,

    /** Gestiona el paso de un láser del Jugador 2 entre pantallas. */
    LASER2_CROSS,

    /** Notifica que el Jugador 2 ha perdido una vida o ha muerto. */
    YOU_DIED2,

    /** Sincronizar puntaje en tiempo real */
    SCORE_UPDATE
}