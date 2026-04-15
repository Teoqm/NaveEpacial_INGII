package autonoma.nave_epacial.graphics;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La clase Assets es la encargada de centralizar, cargar y proporcionar acceso
 * a todos los recursos multimedia del juego, incluyendo imágenes, sonidos y fuentes.
 * Utiliza métodos estáticos para permitir que cualquier componente del juego
 * acceda a los recursos cargados tras la inicialización.
 * @version 1.0
 */
public class Assets {

	/** Indica si todos los recursos han sido cargados exitosamente. */
	public static boolean loaded = false;
	/** Contador actual de recursos cargados (útil para barras de progreso). */
	public static float count = 0;
	/** Cantidad total de recursos que deben ser cargados. */
	public static float MAX_COUNT = 46;

	/** Imagen de la nave del jugador 1 local (verde). */
	public static BufferedImage player;
	/** Imagen de la nave del jugador 2 local (naranja). */
	public static BufferedImage player2;
	/** Imagen de la nave del enemigo 1 remoto (azul). */
	public static BufferedImage playerEnemy1;
	/** Imagen de la nave del enemigo 2 remoto (rojo). */
	public static BufferedImage playerEnemy2;

	/** Imagen del efecto de propulsión (fuego). */
	public static BufferedImage speed;

	/** Imágenes para los diferentes tipos de láseres (azul, verde y rojo). */
	public static BufferedImage blueLaser, greenLaser, redLaser;

	/** Arreglo de imágenes que componen la animación de explosión. */
	public static BufferedImage[] exp = new BufferedImage[9];

	/** Imagen de la nave enemiga OVNI. */
	public static BufferedImage ufo;

	/** Fuente principal en tamaño grande. */
	public static Font fontBig;
	/** Fuente principal en tamaño mediano. */
	public static Font fontMed;

	/** Clips de audio para música de fondo, explosiones y disparos. */
	public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot;

	/** Arreglos de imágenes para meteoros de distintos tamaños. */
	public static BufferedImage[] bigs   = new BufferedImage[4];
	public static BufferedImage[] meds   = new BufferedImage[2];
	public static BufferedImage[] smalls = new BufferedImage[2];
	public static BufferedImage[] tinies = new BufferedImage[2];

	/** Imágenes para la representación visual de números y vidas. */
	public static BufferedImage[] numbers = new BufferedImage[11];
	public static BufferedImage life;

	/** Imágenes para los componentes de la interfaz de usuario (botones). */
	public static BufferedImage blueBtn;
	public static BufferedImage greyBtn;

	/**
	 * Inicializa la carga de todos los recursos del juego.
	 * Este método debe invocarse al inicio para asegurar que los objetos
	 * tengan acceso a sus texturas y sonidos.
	 */
	public static void init() {

		// Tamaño original de la nave: 69x53 píxeles
		int shipW = 69;
		int shipH = 53;

		player       = loadImage("/ships/player_1.png",           shipW, shipH);
		player2      = loadImage("/ships/playerShip1_orange.png", shipW, shipH);
		playerEnemy1 = loadImage("/ships/playerShip2_blue.png",   shipW, shipH);
		playerEnemy2 = loadImage("/ships/playerShip2_red.png",    shipW, shipH);

		speed = loadImage("/effects/fire08.png");

		fontBig = loadFont("/fonts/futureFont.ttf", 42);
		fontMed = loadFont("/fonts/futureFont.ttf", 20);

		blueLaser  = loadImage("/lasers/laserBlue01.png");
		greenLaser = loadImage("/lasers/laserGreen11.png");
		redLaser   = loadImage("/lasers/laserRed01.png");

		for (int i = 0; i < bigs.length; i++)
			bigs[i] = loadImage("/meteors/meteorGrey_big" + (i + 1) + ".png");

		for (int i = 0; i < meds.length; i++)
			meds[i] = loadImage("/meteors/meteorGrey_med" + (i + 1) + ".png");

		for (int i = 0; i < smalls.length; i++)
			smalls[i] = loadImage("/meteors/meteorGrey_small" + (i + 1) + ".png");

		for (int i = 0; i < tinies.length; i++)
			tinies[i] = loadImage("/meteors/meteorGrey_tiny" + (i + 1) + ".png");

		for (int i = 0; i < exp.length; i++)
			exp[i] = loadImage("/explosion/" + i + ".png");

		ufo = loadImage("/ships/ufoRed.png");

		for (int i = 0; i < numbers.length; i++)
			numbers[i] = loadImage("/numbers/" + i + ".png");

		life = loadImage("/others/life.png");

		backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
		explosion       = loadSound("/sounds/explosion.wav");
		playerLoose     = loadSound("/sounds/playerLoose.wav");
		playerShoot     = loadSound("/sounds/playerShoot.wav");
		ufoShoot        = loadSound("/sounds/ufoShoot.wav");

		greyBtn = loadImage("/ui/grey_button.png");
		blueBtn = loadImage("/ui/blue_button.png");

		loaded = true;
	}

	/**
	 * Carga una imagen desde la ruta especificada e incrementa el contador de recursos.
	 *
	 * @param path Ruta del archivo de imagen.
	 * @return {@link BufferedImage} con los datos de la imagen.
	 */
	public static BufferedImage loadImage(String path) {
		count++;
		return Loader.ImageLoader(path);
	}

	/**
	 * Carga una imagen y la escala al tamaño especificado.
	 * Usado para normalizar el tamaño de todas las naves jugadoras.
	 *
	 * @param path   Ruta del archivo de imagen.
	 * @param width  Ancho deseado en píxeles.
	 * @param height Alto deseado en píxeles.
	 * @return {@link BufferedImage} escalada al tamaño indicado.
	 */
	public static BufferedImage loadImage(String path, int width, int height) {
		count++;
		BufferedImage original = Loader.ImageLoader(path);
		BufferedImage scaled   = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		scaled.getGraphics().drawImage(
				original.getScaledInstance(width, height, Image.SCALE_SMOOTH),
				0, 0, null);
		return scaled;
	}

	/**
	 * Carga una fuente TrueType (.ttf) y le asigna un tamaño.
	 *
	 * @param path Ruta del archivo de fuente.
	 * @param size Tamaño de la fuente a cargar.
	 * @return Objeto {@link Font} configurado.
	 */
	public static Font loadFont(String path, int size) {
		count++;
		return Loader.loadFont(path, size);
	}

	/**
	 * Carga un archivo de audio en formato Clip.
	 *
	 * @param path Ruta del archivo de sonido (.wav).
	 * @return Objeto {@link Clip} listo para ser reproducido.
	 */
	public static Clip loadSound(String path) {
		count++;
		return Loader.loadSound(path);
	}
}