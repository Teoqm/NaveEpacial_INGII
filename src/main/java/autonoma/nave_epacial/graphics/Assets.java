package autonoma.nave_epacial.graphics;

import java.awt.image.BufferedImage;

public class Assets {

    //clase que sirve para almasenar recursos que vamso a utlizar

    public static BufferedImage player;

    public  static void init(){

        player = Loeder.imageLoder("/ships/player_1.png");


    }
}
