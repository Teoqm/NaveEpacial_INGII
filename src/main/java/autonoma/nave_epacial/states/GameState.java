package autonoma.nave_epacial.states;

import autonoma.nave_epacial.Io.JSONParser;
import autonoma.nave_epacial.Io.ScoreData;
import autonoma.nave_epacial.gameObjects.*;
import autonoma.nave_epacial.graphics.Animation;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Sound;
import autonoma.nave_epacial.math.Vector2D;
import autonoma.nave_epacial.network.GameMessage;
import autonoma.nave_epacial.network.MessageType;
import autonoma.nave_epacial.network.NetworkObserver;
import autonoma.nave_epacial.network.UdpClient;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GameState extends State implements NetworkObserver {

	public static final Vector2D PLAYER_START_POSITION = new Vector2D(
			Constants.WIDTH / 2 - Assets.player.getWidth() / 2,
			Constants.HEIGHT / 2 - Assets.player.getHeight() / 2);

	public static final Vector2D PLAYER2_START_POSITION = new Vector2D(
			Constants.WIDTH / 2 - Assets.player.getWidth() / 2 + 60,
			Constants.HEIGHT / 2 - Assets.player.getHeight() / 2);

	// Jugadores locales
	private Player player;
	private Player player2;

	// Jugadores remotos
	private Player enemy;
	private Player enemy2;

	private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
	private final ArrayList<MovingObject> objectsToAdd  = new ArrayList<>();
	private final ArrayList<Animation>    explosions    = new ArrayList<>();
	private final ArrayList<Message>      messages      = new ArrayList<>();

	private int score = 0;
	private int lives1 = 3;
	private int lives2 = 3;
	private int livesEnemy1 = 3; // vidas del jugador remoto 1
	private int livesEnemy2 = 3; // vidas del jugador remoto 2
	private int meteors;
	private int waves = 1;

	private Sound       backgroundMusic;
	private Chronometer gameOverTimer;
	private boolean     gameOver;
	private Chronometer ufoSpawner;
	private UdpClient   udpClient;

	public GameState() {

		// Jugador 1 local — flechas
		player = new Player(PLAYER_START_POSITION, new Vector2D(),
				Constants.PLAYER_MAX_VEL, Assets.player, this,
				Player.InputScheme.ARROWS);
		player.setRemote(false);
		movingObjects.add(player);

		// Jugador 2 local — WASD
		player2 = new Player(PLAYER2_START_POSITION, new Vector2D(),
				Constants.PLAYER_MAX_VEL, Assets.player, this,
				Player.InputScheme.WASD);
		player2.setRemote(false);
		movingObjects.add(player2);

		// Enemigo 1 remoto
		enemy = new Player(new Vector2D(-200, -200), new Vector2D(),
				Constants.PLAYER_MAX_VEL, Assets.player, this,
				Player.InputScheme.ARROWS);
		enemy.setRemote(true);
		enemy.setOnMyScreen(false);
		movingObjects.add(enemy);

		// Enemigo 2 remoto
		enemy2 = new Player(new Vector2D(-200, -200), new Vector2D(),
				Constants.PLAYER_MAX_VEL, Assets.player, this,
				Player.InputScheme.WASD);
		enemy2.setRemote(true);
		enemy2.setOnMyScreen(false);
		movingObjects.add(enemy2);

		gameOverTimer = new Chronometer();
		gameOver      = false;
		meteors       = 1;

		backgroundMusic = new Sound(Assets.backgroundMusic);
		backgroundMusic.changeVolume(-10.0f);

		ufoSpawner = new Chronometer();
		ufoSpawner.run(Constants.UFO_SPAWN_RATE);

		try {
			udpClient = new UdpClient(
					Constants.LOCAL_PORT,
					Constants.REMOTE_IP,
					Constants.REMOTE_PORT,
					this);
			new Thread(udpClient).start();

			player.setUdpClient(udpClient);
			player2.setUdpClient(udpClient);
			enemy.setUdpClient(udpClient);
			enemy2.setUdpClient(udpClient);

		} catch (Exception e) {
			System.err.println("Red no disponible: " + e.getMessage());
		}

		startWave();
	}

	@Override
	public void onMessageReceived(GameMessage msg) {
		switch (msg.type) {

			case ENEMY_DIED: {
				// Un jugador del otro PC perdió una vida
				String which = msg.data[0];
				if (which.equals("1")) {
					livesEnemy1--;
				} else {
					livesEnemy2--;
				}

				boolean remoteAlive = livesEnemy1 > 0 || livesEnemy2 > 0;
				boolean localAlive  = lives1 > 0 || lives2 > 0;

				if (!remoteAlive && localAlive) {
					// El otro equipo murió: mi equipo gana
					State.changeState(new WinnerState("Tu equipo gana"));
				}
				break;
			}

			case WINNER: {
				// El otro PC nos dice que ellos perdieron: nosotros ganamos
				State.changeState(new WinnerState("Tu equipo gana"));
				break;
			}

			// ── Jugador remoto 1 ──────────────────────────────────
			case PLAYER_STATE: {
				updateRemotePosition(enemy, msg, true);
				break;
			}
			case PLAYER_CROSS: {
				applyPlayerCross(enemy, msg);
				break;
			}
			case SPAWN_LASER: {
				spawnRemoteLaser(msg);
				break;
			}
			case LASER_CROSS: {
				spawnCrossedLaser(msg);
				break;
			}
			case YOU_DIED: {
				player.destroy();
				break;
			}

			// ── Jugador remoto 2 ──────────────────────────────────
			case PLAYER2_STATE: {
				updateRemotePosition(enemy2, msg, true);
				break;
			}
			case PLAYER2_CROSS: {
				applyPlayerCross(enemy2, msg);
				break;
			}
			case SPAWN_LASER2: {
				spawnRemoteLaser(msg);
				break;
			}
			case LASER2_CROSS: {
				spawnCrossedLaser(msg);
				break;
			}
			case YOU_DIED2: {
				player2.destroy();
				break;
			}

			// ── Meteoros (compartidos) ────────────────────────────
			case METEOR_CROSS: {
				spawnCrossedMeteor(msg);
				break;
			}
		}
	}

	// ── Helpers de red ────────────────────────────────────────────────────

	private void updateRemotePosition(Player remote, GameMessage msg, boolean invert) {
		double x            = Double.parseDouble(msg.data[0]);
		double y            = Double.parseDouble(msg.data[1]);
		double ang          = Double.parseDouble(msg.data[2]);
		boolean onHisScreen = Boolean.parseBoolean(msg.data[3]);
		remote.setAngle(ang);
		remote.setOnMyScreen(!onHisScreen);
		if (!onHisScreen) {
			remote.getPosition().setX(x);
			remote.getPosition().setY(y);
		} else {
			remote.getPosition().setX(-200);
			remote.getPosition().setY(-200);
		}
	}

	private void applyPlayerCross(Player remote, GameMessage msg) {
		String side = msg.data[0];
		double y    = Double.parseDouble(msg.data[1]);
		double vx   = Double.parseDouble(msg.data[2]);
		double vy   = Double.parseDouble(msg.data[3]);
		double ang  = Double.parseDouble(msg.data[4]);
		double newX = side.equals("RIGHT") ? 0
				: Constants.WIDTH - Assets.player.getWidth();
		remote.setPosition(new Vector2D(newX, y));
		remote.setVelocity(new Vector2D(vx, vy));
		remote.setAngle(ang);
		remote.setOnMyScreen(true);
	}

	private void spawnRemoteLaser(GameMessage msg) {
		double x   = Double.parseDouble(msg.data[0]);
		double y   = Double.parseDouble(msg.data[1]);
		double ang = Double.parseDouble(msg.data[2]);
		Vector2D heading = new Vector2D(0, 1).setDirection(ang - Math.PI / 2);
		Laser l = new Laser(new Vector2D(x, y), heading,
				Constants.LASER_VEL, ang, Assets.redLaser, this);
		synchronized (objectsToAdd) { objectsToAdd.add(l); }
	}

	private void spawnCrossedLaser(GameMessage msg) {
		String side = msg.data[0];
		double y    = Double.parseDouble(msg.data[1]);
		double vx   = Double.parseDouble(msg.data[2]);
		double vy   = Double.parseDouble(msg.data[3]);
		double ang  = Double.parseDouble(msg.data[4]);
		double newX = side.equals("RIGHT") ? 0 : Constants.WIDTH;
		Laser l = new Laser(new Vector2D(newX, y), new Vector2D(vx, vy),
				1.0, ang, Assets.redLaser, this);
		synchronized (objectsToAdd) { objectsToAdd.add(l); }
	}

	private void spawnCrossedMeteor(GameMessage msg) {
		String side = msg.data[0];
		double y    = Double.parseDouble(msg.data[1]);
		double vx   = Double.parseDouble(msg.data[2]);
		double vy   = Double.parseDouble(msg.data[3]);
		Size size   = Size.valueOf(msg.data[5]);
		double newX = side.equals("RIGHT") ? 0 : Constants.WIDTH;
		BufferedImage tex = size.textures[(int)(Math.random() * size.textures.length)];
		Meteor m = new Meteor(new Vector2D(newX, y), new Vector2D(vx, vy),
				1.0, tex, this, size);
		m.setUdpClient(udpClient);
		synchronized (objectsToAdd) { objectsToAdd.add(m); }
	}

	// ── Update ────────────────────────────────────────────────────────────

	@Override
	public void update() throws IOException {

		synchronized (objectsToAdd) {
			movingObjects.addAll(objectsToAdd);
			objectsToAdd.clear();
		}

		for (int i = 0; i < movingObjects.size(); i++) {
			MovingObject mo = movingObjects.get(i);
			mo.update();
			if (mo.isDead()) { movingObjects.remove(i--); }
		}

		for (int i = 0; i < explosions.size(); i++) {
			Animation anim = explosions.get(i);
			anim.update();
			if (!anim.isRunning()) { explosions.remove(i--); }
		}

		if (gameOver && !gameOverTimer.isRunning()) {
			try {
				ArrayList<ScoreData> dataList = JSONParser.readFile();
				dataList.add(new ScoreData(score));
				JSONParser.writeFile(dataList);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (udpClient != null) udpClient.stop();
			State.changeState(new MenuState());
		}

		if (!ufoSpawner.isRunning()) {
			ufoSpawner.run(Constants.UFO_SPAWN_RATE);
			spawnUfo();
		}

		gameOverTimer.update();
		ufoSpawner.update();

		boolean meteorsRemaining = false;
		for (MovingObject mo : movingObjects)
			if (mo instanceof Meteor) { meteorsRemaining = true; break; }
		if (!meteorsRemaining) startWave();
	}

	// ── Métodos del juego ─────────────────────────────────────────────────

	public void addScore(int value, Vector2D position) {
		score += value;
		messages.add(new Message(position, true, "+" + value + " score",
				Color.WHITE, false, Assets.fontMed));
	}

	public void divideMeteor(Meteor meteor) {
		Size size = meteor.getSize();
		BufferedImage[] textures = size.textures;
		Size newSize;
		switch (size) {
			case BIG:   newSize = Size.MED;   break;
			case MED:   newSize = Size.SMALL; break;
			case SMALL: newSize = Size.TINY;  break;
			default: return;
		}
		for (int i = 0; i < size.quantity; i++) {
			Meteor frag = new Meteor(
					meteor.getPosition(),
					new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
					Constants.METEOR_VEL * Math.random() + 1,
					textures[(int)(Math.random() * textures.length)],
					this, newSize);
			frag.setUdpClient(udpClient);
			movingObjects.add(frag);
		}
	}

	private void startWave() {
		messages.add(new Message(new Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2),
				false, "WAVE " + waves, Color.WHITE, true, Assets.fontBig));
		waves++;
		for (int i = 0; i < meteors; i++) {
			double x = i % 2 == 0 ? Math.random() * Constants.WIDTH : 0;
			double y = i % 2 == 0 ? 0 : Math.random() * Constants.HEIGHT;
			BufferedImage texture = Assets.bigs[(int)(Math.random() * Assets.bigs.length)];
			Meteor m = new Meteor(
					new Vector2D(x, y),
					new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
					Constants.METEOR_VEL * Math.random() + 1,
					texture, this, Size.BIG);
			m.setUdpClient(udpClient);
			movingObjects.add(m);
		}
		meteors++;
	}

	public void playExplosion(Vector2D position) {
		explosions.add(new Animation(Assets.exp, 50,
				position.subtract(new Vector2D(
						Assets.exp[0].getWidth()  / 2,
						Assets.exp[0].getHeight() / 2))));
	}

	private void spawnUfo() {
		int rand = (int)(Math.random() * 2);
		double x = rand == 0 ? Math.random() * Constants.WIDTH : Constants.WIDTH;
		double y = rand == 0 ? Constants.HEIGHT : Math.random() * Constants.HEIGHT;
		ArrayList<Vector2D> path = new ArrayList<>();
		path.add(new Vector2D(Math.random() * Constants.WIDTH / 2,
				Math.random() * Constants.HEIGHT / 2));
		path.add(new Vector2D(Math.random() * (Constants.WIDTH / 2) + Constants.WIDTH / 2,
				Math.random() * Constants.HEIGHT / 2));
		path.add(new Vector2D(Math.random() * Constants.WIDTH / 2,
				Math.random() * (Constants.HEIGHT / 2) + Constants.HEIGHT / 2));
		path.add(new Vector2D(Math.random() * (Constants.WIDTH / 2) + Constants.WIDTH / 2,
				Math.random() * (Constants.HEIGHT / 2) + Constants.HEIGHT / 2));
		movingObjects.add(new Ufo(new Vector2D(x, y), new Vector2D(),
				Constants.UFO_MAX_VEL, Assets.ufo, path, this));
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).draw(g2d);
			if (messages.get(i).isDead()) messages.remove(i--);
		}
		for (int i = 0; i < movingObjects.size(); i++)
			movingObjects.get(i).draw(g);
		for (int i = 0; i < explosions.size(); i++) {
			Animation anim = explosions.get(i);
			g2d.drawImage(anim.getCurrentFrame(),
					(int) anim.getPosition().getX(),
					(int) anim.getPosition().getY(), null);
		}
		drawScore(g);
		drawLives(g);
	}

	private void drawScore(Graphics g) {
		Vector2D pos = new Vector2D(850, 25);
		String s = Integer.toString(score);
		for (int i = 0; i < s.length(); i++) {
			g.drawImage(Assets.numbers[Integer.parseInt(s.substring(i, i + 1))],
					(int) pos.getX(), (int) pos.getY(), null);
			pos.setX(pos.getX() + 20);
		}
	}

	private void drawLives(Graphics g) {
		g.setFont(Assets.fontMed);

		// Equipo local
		g.setColor(Color.CYAN);
		g.drawString("J1: " + Math.max(lives1, 0) + " vidas", 25, 30);
		g.drawString("J2: " + Math.max(lives2, 0) + " vidas", 25, 55);

		// Equipo rival
		g.setColor(Color.ORANGE);
		g.drawString("E1: " + Math.max(livesEnemy1, 0) + " vidas", 25, 95);
		g.drawString("E2: " + Math.max(livesEnemy2, 0) + " vidas", 25, 120);
	}

	public ArrayList<MovingObject> getMovingObjects() { return movingObjects; }
	public ArrayList<Message>      getMessages()      { return messages; }
	public Player                  getPlayer()        { return player; }

	public boolean subtractLife(Player who) {
		if (who == player) {
			lives1--;
		} else {
			lives2--;
		}

		boolean localAlive  = lives1 > 0 || lives2 > 0;
		boolean remoteAlive = livesEnemy1 > 0 || livesEnemy2 > 0;

		if (!localAlive && !remoteAlive) {
			// Todos muertos: empate
			gameOver();
		} else if (!localAlive) {
			// Mi equipo perdió, el otro gana
			if (udpClient != null)
				udpClient.send(new GameMessage(MessageType.WINNER, "local"));
			State.changeState(new WinnerState("El equipo rival gana"));
		}

		// Avisar al otro PC que uno de mis jugadores perdió una vida
		if (udpClient != null)
			udpClient.send(new GameMessage(MessageType.ENEMY_DIED,
					who == player ? "1" : "2"));

		return who == player ? lives1 > 0 : lives2 > 0;
	}

	public void gameOver() {
		messages.add(new Message(PLAYER_START_POSITION, true,
				"GAME OVER", Color.WHITE, true, Assets.fontBig));
		gameOverTimer.run(Constants.GAME_OVER_TIME);
		gameOver = true;
	}
}