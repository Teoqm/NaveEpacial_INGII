package autonoma.nave_epacial.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * La clase Loader es una utilidad de soporte encargada de la carga física de recursos
 * desde el sistema de archivos o el classpath.
 * Proporciona métodos estáticos para procesar imágenes, fuentes y archivos de audio,
 * encapsulando el manejo de excepciones común en la lectura de recursos.
 * @version 1.0
 */
public class Loader {

	/**
	 * Carga una imagen desde una ruta de recurso dada.
	 * * @param path La ruta relativa al recurso de la imagen.
	 * @return Un objeto {@link BufferedImage} si la carga es exitosa; {@code null} en caso de error.
	 */
	public static BufferedImage ImageLoader(String path)
	{
		try {
			return ImageIO.read(Loader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Carga una fuente TrueType (TTF) desde el flujo de recursos especificado.
	 * * @param path La ruta relativa al archivo .ttf.
	 * @param size El tamaño de punto de la fuente a generar.
	 * @return Un objeto {@link Font} derivado en estilo plano; {@code null} si ocurre un error de formato o lectura.
	 */
	public static Font loadFont(String path, int size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Loader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Carga un archivo de audio y lo prepara para su reproducción como un Clip.
	 * * @param path La ruta relativa al archivo de sonido (generalmente .wav).
	 * @return Un objeto {@link Clip} abierto y listo para sonar; {@code null} si el formato no es compatible
	 * o la línea de audio no está disponible.
	 */
	public static Clip loadSound(String path) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Loader.class.getResource(path)));
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return null;
	}

}