package autonoma.nave_epacial.graphics;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * La clase Sound proporciona una interfaz simplificada para la gestión de audio.
 * Permite controlar la reproducción, detención, bucles y ajuste de volumen
 * de los recursos sonoros cargados en el juego.
 * * @author Gemini
 * @version 1.0
 */
public class Sound {

    /** El recurso de audio subyacente. */
    private Clip clip;
    /** Control para ajustar el volumen (ganancia) del sonido. */
    private FloatControl volume;

    /**
     * Construye un nuevo objeto Sound a partir de un Clip de audio.
     * * @param clip El clip de audio previamente cargado.
     */
    public Sound(Clip clip) {
        this.clip = clip;
        volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    /**
     * Inicia la reproducción del sonido desde el principio (frame 0).
     */
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Inicia la reproducción del sonido en un bucle infinito.
     */
    public void loop() {
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Detiene la reproducción actual del sonido.
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Obtiene la posición actual de reproducción en frames.
     * * @return La posición actual del frame dentro del clip.
     */
    public int getFramePosition() {
        return clip.getFramePosition();
    }

    /**
     * Ajusta el volumen del sonido basándose en un valor de ganancia.
     * * @param value El nuevo valor de ganancia (normalmente en decibelios).
     */
    public void changeVolume(float value) {
        volume.setValue(value);
    }

}