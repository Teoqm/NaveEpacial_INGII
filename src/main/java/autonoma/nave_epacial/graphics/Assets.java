package autonoma.nave_epacial.graphics;

import java.awt.image.BufferedImage;

public class Assets {
	
	public static BufferedImage player;
	/// effects

	public static BufferedImage speed;

	//laserts

	public static BufferedImage blueLaser,greenLaser,redLaser;

	//meteors

	public static BufferedImage[] bigs = new BufferedImage[4];
	public static BufferedImage[] meds = new BufferedImage[2];
	public static BufferedImage[] smalls = new BufferedImage[2];
	public static BufferedImage[] tinies = new BufferedImage[2];

	public static void init()
	{
		player = Loader.ImageLoader("/ships/player_1.png");

		speed = Loader.ImageLoader("/effects/fire08.png");

		blueLaser = Loader.ImageLoader("/lasers/laserBlue01.png");

		greenLaser  = Loader.ImageLoader("/lasers/laserGreen11.png");

		redLaser  = Loader.ImageLoader("/lasers/laserRed01.png");

		for (int i = 0; i < bigs.length; i++)
			bigs[i] = Loader.ImageLoader("/meteors/meteorGrey_big"+(i+1)+".png");

		for (int i = 0; i < meds.length; i++)
			meds[i] = Loader.ImageLoader("/meteors/meteorGrey_med"+(i+1)+".png");

		for (int i = 0; i < smalls.length; i++)
			smalls[i] = Loader.ImageLoader("/meteors/meteorGrey_small"+(i+1)+".png");

		for (int i = 0; i < tinies.length; i++)
			tinies[i] = Loader.ImageLoader("/meteors/meteorGrey_tiny"+(i+1)+".png");
	}
	
}
