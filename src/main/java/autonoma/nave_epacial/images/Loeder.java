package autonoma.nave_epacial.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Loeder {

    /*
    * clase con la funcio en ayudarno es cargar imagenes y musica y otrso recursos extrenos
    * */


    public static BufferedImage imageLoder(String path){

        BufferedImage image = null;
        try {
           return ImageIO.read(Loeder.class.getResource(path));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
}
