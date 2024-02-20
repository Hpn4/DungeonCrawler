package game.entity.mob;

public class HitBoxInsets {

	public float left;

	public float right;

	public float top;

	public float bot;

	public HitBoxInsets(final float all) {
		this(all, all, all, all);
	}

	public HitBoxInsets(final float left, final float top, final float right, final float bot) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bot = bot;
	}
}
