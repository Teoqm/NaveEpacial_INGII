package autonoma.nave_epacial.states;

import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.models.Player;
import autonoma.nave_epacial.graphics.Assets;

import java.awt.*;

public class GameState {

    private Player player;

    public GameState() {

        player = new Player(new Vector2D(100,500), Assets.player);

    }


    public void update() {
        player.update();
    }

    public void draw(Graphics g) {
        player.draw(g);
    }
}
