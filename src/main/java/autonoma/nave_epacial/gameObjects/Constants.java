package autonoma.nave_epacial.gameObjects;

public class Constants {

    //frame dimensions

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    //player propieties

    public static final int FIRERATE=300;
    public static final double DELTAANGLE=0.1;
    public static final double ACC= 0.2;
    public static final double PLAYER_MAX_VEL=5.0;

    //Laser propieties

    public static final double LASER_VEL=15.0;

    //Meteor propieties

    public static final double METEOR_VEL = (double)2.0F;
    public static final int METEOR_SCORE = 20;
    public static final int NODE_RADIUS = 160;

    //Ufo propieties

    public static final double UFO_MASS = (double)60.0F;
    public static final int UFO_MAX_VEL = 3;
    public static long UFO_FIRE_RATE = 1000L;
    public static double UFO_ANGLE_RANGE = (Math.PI / 2D);
    public static final int UFO_SCORE = 40;
}
