package game.entity.utils;

import org.joml.Vector2f;

public class Animation {

	private float anim;

	private boolean finish;

	private int index;

	public int[] moves;

	public Animation(final int[] defaultMoves) {
		moves = defaultMoves;
	}

	public void update(final float interval, final float speed) {
		finish = false;
		anim += interval;

		final float bias = 0.1f / speed;
		if (anim >= bias) {
			index++;
			anim = 0;
		}

		if (index >= moves[1]) {
			finish = true;
			index = 0;
		}
	}

	public Vector2f getPos(final int direction, final int COLS, final int ROWS) {
		final int tilePos = moves[0] * COLS + COLS * direction + index;
		final int col = tilePos % COLS, row = tilePos / COLS;
		return new Vector2f((float) col / COLS, (float) row / ROWS);
	}

	public void setMoves(final int[] newMoves) {
		moves = newMoves;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public boolean isFinish() {
		return finish;
	}
}
