package engine.graph.hlib.component.button;

import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.graphics.paint.Paint;

/**
 * 
 * @author Hpn4
 *
 */
public abstract class CheckableButton extends AbstractButton {

	private boolean selected;

	public CheckableButton(final boolean selected) {
		this(selected, "");
	}

	public CheckableButton(final boolean selected, final String txt) {
		super(txt);
		setSelected(selected);
	}

	public CheckableButton(final boolean selected, final String txt, final Paint fg, final int fontSize) {
		super(txt, fg, fontSize);
		setSelected(selected);
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void mousePressed(final MouseEvent event) {
		selected = !selected;
		super.mousePressed(event);
	}
}
