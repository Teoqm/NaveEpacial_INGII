package autonoma.nave_epacial.states;

import autonoma.nave_epacial.gameObjects.Chronometer;
import autonoma.nave_epacial.gameObjects.Constants;
import autonoma.nave_epacial.graphics.Assets;
import autonoma.nave_epacial.graphics.Text;

import java.awt.*;
import java.io.IOException;

public class WinnerState extends State {

    private final String winnerName;
    private Chronometer timer;

    public WinnerState(String winnerName) {
        this.winnerName = winnerName;
        timer = new Chronometer();
        timer.run(5000); // vuelve al menú después de 5 segundos
    }

    @Override
    public void update() throws IOException {
        timer.update();
        if (!timer.isRunning()) {
            State.changeState(new MenuState());
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        Text.drawText(g2d, "GANADOR",
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 - 80),
                true, Color.YELLOW, Assets.fontBig);

        Text.drawText(g2d, winnerName,
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2),
                true, Color.WHITE, Assets.fontBig);

        Text.drawText(g2d, "Volviendo al menu...",
                new autonoma.nave_epacial.math.Vector2D(Constants.WIDTH / 2, Constants.HEIGHT / 2 + 80),
                true, Color.GRAY, Assets.fontMed);
    }
}