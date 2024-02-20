package game;

import engine.GameEngine;
import engine.IGameLogic;
import engine.graph.hlib.component.HWindow;

public class DungeonCrawler {

    public static void main(final String[] args) {
        try {
            final IGameLogic gameLogic = new DummyGame();
            final HWindow.WindowOptions opts = new HWindow.WindowOptions();

            opts.antialiasing = true;

            final GameEngine gameEng = new GameEngine("Dungeon Crawler", opts, gameLogic);

            gameEng.run();

        } catch (final Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
