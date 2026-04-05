package autonoma.nave_epacial.states;

import java.awt.Graphics;

import autonoma.nave_epacial.gameObjects.Player;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;

public class GameState {
	
	private Player player;
	
	public GameState()
	{
		player = new Player(new Vector2D(400, 300), new Vector2D(), 5, Assets.player);
	}
	
	public void update()
	{
		player.update();
	}
	
	public void draw(Graphics g)
	{
		player.draw(g);
	}
}
