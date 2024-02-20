package engine.graph.hlib.utils;

import java.util.Objects;

public class Insets implements Cloneable {

	public int top, left, bot, right;

	public Insets(final int all) {
		this(all, all, all, all);
	}

	public Insets(final int top, final int left, final int bot, final int right) {
		set(top, left, bot, right);
	}

	public void set(final int top, final int left, final int bot, final int right) {
		this.top = top;
		this.left = left;
		this.bot = bot;
		this.right = right;
	}

	public void add(final Insets insets) {
		top += insets.top;
		left += insets.left;
		bot += insets.bot;
		right += insets.right;
	}

	public int getWidth() {
		return left + right;
	}

	public int getHeight() {
		return top + bot;
	}

	public int getTotal() {
		return getWidth() + getHeight();
	}

	public String toString() {
		return getClass().getName() + "[top:" + top + ", left:" + left + ", bot:" + bot + ", right:" + right + "]";
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(bot, left, right, top);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Insets))
			return false;

		final Insets other = (Insets) obj;
		return bot == other.bot && left == other.left && right == other.right && top == other.top;
	}
}
