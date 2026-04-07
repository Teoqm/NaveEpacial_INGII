package autonoma.nave_epacial.graphics;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

	public static boolean loaded = false;
	public static float count = 0;
	public static float MAX_COUNT = 46;


	public static BufferedImage player;
	/// effects

	public static BufferedImage speed;

	//laserts

	public static BufferedImage blueLaser,greenLaser,redLaser;

	//explosion

	public static BufferedImage[] exp= new BufferedImage[9];

	//enemies

	public static BufferedImage ufo;

	// fonts

	public static Font fontBig;
	public static Font fontMed;

	public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot;


	//meteors

	public static BufferedImage[] bigs = new BufferedImage[4];
	public static BufferedImage[] meds = new BufferedImage[2];
	public static BufferedImage[] smalls = new BufferedImage[2];
	public static BufferedImage[] tinies = new BufferedImage[2];

	public static BufferedImage[] numbers = new BufferedImage[11];
	public static BufferedImage life;

	// ui

	public static BufferedImage blueBtn;
	public static BufferedImage greyBtn;


	public static void init()
	{
		player = loadImage("/ships/player_1.png");

		speed = loadImage("/effects/fire08.png");

		fontBig = loadFont("/fonts/futureFont.ttf", 42);

		fontMed = loadFont("/fonts/futureFont.ttf", 20);

		blueLaser = loadImage("/lasers/laserBlue01.png");

		greenLaser  = loadImage("/lasers/laserGreen11.png");

		redLaser  = loadImage("/lasers/laserRed01.png");

		for (int i = 0; i < bigs.length; i++)
			bigs[i] = loadImage("/meteors/meteorGrey_big"+(i+1)+".png");

		for (int i = 0; i < meds.length; i++)
			meds[i] = loadImage("/meteors/meteorGrey_med"+(i+1)+".png");

		for (int i = 0; i < smalls.length; i++)
			smalls[i] = loadImage("/meteors/meteorGrey_small"+(i+1)+".png");

		for (int i = 0; i < tinies.length; i++)
			tinies[i] = loadImage("/meteors/meteorGrey_tiny"+(i+1)+".png");

		for (int i = 0; i < exp.length; i++)
			exp[i]=loadImage("/explosion/"+i+".png");

		ufo = loadImage("/ships/ufoRed.png");

		for(int i = 0; i < numbers.length; ++i) {
			numbers[i] = loadImage("/numbers/" + i + ".png");
		}

		life = loadImage("/others/life.png");

		backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
		explosion = loadSound("/sounds/explosion.wav");
		playerLoose = loadSound("/sounds/playerLoose.wav");
		playerShoot = loadSound("/sounds/playerShoot.wav");
		ufoShoot = loadSound("/sounds/ufoShoot.wav");

		greyBtn = loadImage("/ui/grey_button.png");
		blueBtn = loadImage("/ui/blue_button.png");

		loaded = true;
	}


	public static BufferedImage loadImage(String path) {
		count ++;
		return Loader.ImageLoader(path);
	}
	public static Font loadFont(String path, int size) {
		count ++;
		return Loader.loadFont(path, size);
	}
	public static Clip loadSound(String path) {
		count ++;
		return Loader.loadSound(path);
	}

}
