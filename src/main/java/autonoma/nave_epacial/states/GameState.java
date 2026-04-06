package autonoma.nave_epacial.states;

import java.awt.*;
import java.util.ArrayList;

import autonoma.nave_epacial.gameObjects.MovingObject;
import autonoma.nave_epacial.gameObjects.Player;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;

public class GameState {
	private Player player;
	private ArrayList<MovingObject> movingObjects = new ArrayList();

	public GameState() {
		this.player = new Player(new Vector2D((double)400, (double)300), new Vector2D(), (double)7.0F, Assets.player, this);
		this.movingObjects.add(this.player);
	}

	public void update() {
		for(int i = 0; i < this.movingObjects.size(); ++i) {
			((MovingObject)this.movingObjects.get(i)).update();
		}

	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		for(int i = 0; i < this.movingObjects.size(); ++i) {
			((MovingObject)this.movingObjects.get(i)).draw(g);
		}

	}

	public ArrayList<MovingObject> getMovingObjects() {
		return this.movingObjects;
	}
}
