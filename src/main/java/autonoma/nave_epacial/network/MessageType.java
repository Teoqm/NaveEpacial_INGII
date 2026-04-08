package autonoma.nave_epacial.network;
public enum MessageType {
    WINNER,
    ENEMY_DIED,
    // Jugador 1
    PLAYER_STATE,
    PLAYER_CROSS,
    SPAWN_LASER,
    LASER_CROSS,
    METEOR_CROSS,
    YOU_DIED,
    // Jugador 2
    PLAYER2_STATE,
    PLAYER2_CROSS,
    SPAWN_LASER2,
    LASER2_CROSS,
    YOU_DIED2
}