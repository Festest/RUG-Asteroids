package aoop.asteroids.view;

import aoop.asteroids.control.PlayerKeyListener;
import aoop.asteroids.game_observer.GameUpdateListener;
import aoop.asteroids.model.Game;
import aoop.asteroids.model.Spaceship;
import aoop.asteroids.view.view_models.AsteroidViewModel;
import aoop.asteroids.view.view_models.BulletViewModel;
import aoop.asteroids.view.view_models.SpaceshipViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * The panel at the center of the game's window which is responsible for the custom drawing of game objects.
 */
public class AsteroidsPanel extends JPanel implements GameUpdateListener {
	/**
	 * The x- and y-coordinates of the score indicator.
	 */
	private static final Point SCORE_INDICATOR_POSITION = new Point(20, 20);

	/**
	 * The game model that this panel will draw to the screen.
	 */
	private final Game game;

	/**
	 * Number of milliseconds since the last time the game's physics were updated. This is used to continue drawing all
	 * game objects as if they have kept moving, even in between game ticks.
	 */
	private long timeSinceLastTick = 0L;

	private final PlayerKeyListener keyListener;

	/** 
	 * Constructs a new game panel, based on the given model. Also starts listening to the game to check for updates, so
	 * that it can repaint itself if necessary.
	 *
	 * @param game The model which will be drawn in this panel.
	 */
	AsteroidsPanel(Game game) {
		this.game = game;
		this.game.addListener(this);
		this.keyListener = new PlayerKeyListener(game);
		this.addKeyListener(keyListener);
	}
	
	/**
	 * The method provided by JPanel for 'painting' this component. It is overridden here so that this panel can define
	 * some custom drawing. By default, a JPanel is just an empty rectangle.
	 *
	 * @param graphics The graphics object that exposes various drawing methods to use.
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		/* The parent method is first called. Here's an excerpt from the documentation stating why we do this:
		"...if you do not invoke super's implementation you must honor the opaque property, that is if this component is
		opaque, you must completely fill in the background in an opaque color. If you do not honor the opaque property
		you will likely see visual artifacts." Just a little FYI.
		 */
		super.paintComponent(graphics);

		// The Graphics2D class offers some more advanced options when drawing, so before doing any drawing, this is obtained simply by casting.
		Graphics2D graphics2D = (Graphics2D) graphics;
		// Set some key-value options for the graphics object. In this case, this just sets antialiasing to true.
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Since the game takes place in space, it is efficient to just lazily make the background black.
		this.setBackground(Color.BLACK);

		this.drawGameObjects(graphics2D);
		this.drawShipInformation(graphics2D);
	}

	/**
	 * Draws the ship's score and energy.
	 *
	 * @param graphics2D The graphics object that provides the drawing methods.
	 */
	private void drawShipInformation(Graphics2D graphics2D) {
		Collection<Spaceship> spaceships = game.getPlayers();

		int i = 0;
		for (Spaceship spaceship: spaceships) {
			graphics2D.setColor(Color.WHITE);
			graphics2D.drawString(
					String.valueOf(spaceship.getScore()),
					SCORE_INDICATOR_POSITION.x + (150*i),
					SCORE_INDICATOR_POSITION.y
			);
			graphics2D.setColor(Color.GREEN);
			graphics2D.drawRect(SCORE_INDICATOR_POSITION.x + (150*i), SCORE_INDICATOR_POSITION.y + 20, 100, 15);
			graphics2D.fillRect(SCORE_INDICATOR_POSITION.x + (150*i), SCORE_INDICATOR_POSITION.y + 20, (int) spaceship.getEnergyPercentage(), 15);
			i++;
		}
	}

	/**
	 * Draws all of the game's objects. Wraps each object in a view model, then uses that to draw the object.
	 *
	 * @param graphics2D The graphics object that provides the drawing methods.
	 */
	private synchronized void drawGameObjects(Graphics2D graphics2D) {
		/*
		 * Because the game engine is running concurrently in its own thread, we must obtain a lock for the game model
		 * while drawing to ensure that we don't encounter a concurrentModificationException, which would happen if we
		 * were in the middle of drawing while the game engine starts a new physics update.
		 */
		synchronized (this.game) {
			this.game.getPlayers().forEach(spaceship -> {
				if (!spaceship.isDestroyed()) new SpaceshipViewModel(spaceship).drawObject(graphics2D, this.timeSinceLastTick);
			});
			this.game.getAsteroids().forEach(asteroid -> new AsteroidViewModel(asteroid).drawObject(graphics2D, this.timeSinceLastTick));
			this.game.getBullets().forEach(bullet -> new BulletViewModel(bullet).drawObject(graphics2D, this.timeSinceLastTick));
		}
	}

	/**
	 * Do something when the game has indicated that it is updated. For this panel, that means redrawing.
	 *
	 * @param timeSinceLastTick The number of milliseconds since the game's physics were updated. This is used to allow
	 *                          objects to continue to appear animated between each game tick.
	 *
	 * Note for your information: when repaint() is called, Swing does some internal stuff, and then paintComponent()
	 * is called.
	 */
	@Override
	public void onGameUpdated(long timeSinceLastTick) {
		this.timeSinceLastTick = timeSinceLastTick;
		this.repaint();
	}
}
