package autonoma.nave_epacial.states;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import autonoma.nave_epacial.gameObjects.*;
import autonoma.nave_epacial.graphics.Animation;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.math.Vector2D;

public class GameState {
	private Player player;
	private ArrayList<MovingObject> movingObjects = new ArrayList();
	private ArrayList<Animation> explosions = new ArrayList();

	private int meteors;

	public GameState() {
		this.player = new Player(new Vector2D((double)400, (double)300), new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);
		this.movingObjects.add(this.player);

		meteors = 2;
		startWave();
	}

	public void divideMeteor(Meteor meteor) {
		Size size = meteor.getSize();

		BufferedImage[] textures=size.textures;

		Size newSize = null;

		switch (size){
			case BIG:
				newSize = Size.MED;
				break;
			case MED:
				newSize=Size.SMALL;
				break;
			case SMALL:
				newSize=Size.TINY;
				break;
			default:
				return;
		}
		for (int i = 0; i < size.quantity; i++) {
			movingObjects.add(new Meteor(
					meteor.getPosition(),
					new Vector2D(0,1).setDirection(Math.random()*Math.PI*2),
					Constants.METEOR_VEL*Math.random()+1,
					textures[(int)(Math.random()*textures.length)],
					this,
					newSize
			));
		}
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

	public void playExplosion(Vector2D position){
		explosions.add(new Animation(
				Assets.exp,
				50,
				position.subtract(new Vector2D((double) Assets.exp[0].getWidth() /2, (double) Assets.exp[0].getHeight() /2))
		));
	}

	public void update() {
		for(int i = 0; i < this.movingObjects.size(); ++i) {
			((MovingObject)this.movingObjects.get(i)).update();
		}

		for(int i = 0; i < this.explosions.size(); ++i) {
			Animation anim= explosions.get(i);
			anim.update();

			if (!anim.isRunning()) {
				explosions.remove(i);
			}
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

		for(int i = 0; i < this.explosions.size(); ++i) {
			Animation anim = explosions.get(i);
			g2d.drawImage(
					anim.getCurrentFrame(),
					(int) anim.getPosition().getX(),
					(int) anim.getPosition().getY(),
					null
			);
		}

	}

	public ArrayList<MovingObject> getMovingObjects() {
		return this.movingObjects;
	}
}
