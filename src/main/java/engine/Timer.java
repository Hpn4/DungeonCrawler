package engine;

public class Timer {

	private double lastLoopTime;

	public void init() {
		lastLoopTime = getTime();
	}

	public double getTime() {
		return System.nanoTime() / 1000_000_000.0d;
	}

	public float getElapsedTime() {
		final double time = getTime();
		final float elapsedTime = (float) (time - lastLoopTime);
		lastLoopTime = time;
		return elapsedTime;
	}

	public double getLastLoopTime() {
		return lastLoopTime;
	}
}