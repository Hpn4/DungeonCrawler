package engine;

import engine.graph.hlib.component.HWindow;
import engine.graph.hlib.event.MouseInput;
import engine.graph.renderer.Options;

public class GameEngine implements Runnable {

	public static final int TARGET_FPS = 60;

	public static final int TARGET_UPS = 30;
	
	private final IGameLogic gameLogic;

	private final MouseInput mouseInput;

	private final HWindow window;

	private final Timer timer;

	private double lastFps;

	private int fps;

	public GameEngine(final String windowTitle, final HWindow.WindowOptions opts, final IGameLogic gameLogic)
			throws Exception {
		this(windowTitle, 0, 0, opts, gameLogic);
	}

	public GameEngine(final String windowTitle, final int width, final int height, final HWindow.WindowOptions opts,
			final IGameLogic gameLogic) throws Exception {
		window = new HWindow(windowTitle, width, height, opts);
		mouseInput = new MouseInput();
		this.gameLogic = gameLogic;
		timer = new Timer();
	}

	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (final Exception excp) {
			excp.printStackTrace();
		} finally {
			cleanup();
		}
	}

	protected void init() throws Exception {
		window.init();
		System.out.println("window init");
		timer.init();
		gameLogic.init(window);
		lastFps = timer.getTime();
		fps = 0;
	}

	protected void gameLoop() {
		final float interval = 1f / TARGET_UPS;
		float accumulator = 0f;

		while (!window.windowShouldClose()) {
			accumulator += timer.getElapsedTime();

			input();

			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}

			render();

			if (!Options.vSync)
				sync();
		}
	}

	protected void cleanup() {
		gameLogic.cleanup(window);
		window.destroy();
	}

	private void sync() {
		final double endTime = timer.getLastLoopTime() + 1f / TARGET_FPS;

		while (timer.getTime() < endTime)
			try {
				Thread.sleep(1);
			} catch (final InterruptedException ie) {
				ie.printStackTrace();
			}
	}

	protected void input() {
		window.input(mouseInput);
		gameLogic.input(window, mouseInput);
	}

	protected void update(final float interval) {
		gameLogic.update(interval, mouseInput, window);
	}

	protected void render() {
		if (timer.getLastLoopTime() - lastFps > 1) {
			lastFps = timer.getLastLoopTime();
			window.setWindowTitle( fps + " FPS");
			fps = 0;
		}

		fps++;

		gameLogic.render(window);

		window.update();
	}
}
