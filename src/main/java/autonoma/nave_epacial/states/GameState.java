package autonoma.nave_epacial.states;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import autonoma.nave_epacial.gameObjects.*;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;

public class GameState {
	private Player player;
	private ArrayList<MovingObject> movingObjects = new ArrayList();

	private int meteors;

	public GameState() {
		this.player = new Player(new Vector2D((double)400, (double)300), new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);
		this.movingObjects.add(this.player);

		meteors = 2;
		startWave();
	}

	private void startWave(){

		double x, y;

		for (int i = 0; i < meteors; i++ ) {
			x=i %2==0 ? Math.random()* Constants.WIDTH : 0;
			y=i %2==0 ? Math.random()* Constants.HEIGHT : 0;

			BufferedImage texture= Assets.bigs[(int)(Math.random()*Assets.bigs.length)];

			movingObjects.add(new Meteor(
					new Vector2D(x,y),
					new Vector2D(0,1).setDirection(Math.random()*Math.PI*2),
					Constants.METEOR_VEL*Math.random()+1,
					texture,
					this,
					Size.BIG
			));
		}
		meteors++;
	}

	public void update() {
		for(int i = 0; i < this.movingObjects.size(); ++i) {
			((MovingObject)this.movingObjects.get(i)).update();
		}

		for(int i = 0; i < this.movingObjects.size(); ++i){
			if (movingObjects.get(i) instanceof Meteor)
				return;
		}
		startWave();
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
