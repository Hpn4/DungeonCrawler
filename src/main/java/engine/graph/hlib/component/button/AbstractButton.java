package engine.graph.hlib.component.button;

import engine.graph.hlib.component.Labeled;
import engine.graph.hlib.event.EventMulticaster;
import engine.graph.hlib.event.MouseEvent;
import engine.graph.hlib.event.listener.ActionListener;
import engine.graph.hlib.graphics.paint.Paint;

/**
 * This class is the base for all button componnent, it's a subclasses of
 * {@code Labeled}.
 * 
 * This class include a {@code ActionListener} and utility methos for button
 * 
 * @author Hpn4
 *
 */
public abstract class AbstractButton extends Labeled {

	private ActionListener actionListener;

	public AbstractButton() {
		super();
	}

	public AbstractButton(final String txt) {
		super(txt);
	}

	public AbstractButton(final String txt, final Paint fg) {
		super(txt, fg);
	}

	public AbstractButton(final String txt, final Paint fg, final int fontSize) {
		super(txt, fg, fontSize);
	}

	public void addActionListener(final ActionListener actionListener) {
		this.actionListener = EventMulticaster.add(this.actionListener, actionListener);
	}

	public void removeActionListener(final ActionListener actionListener) {
		this.actionListener = EventMulticaster.remove(this.actionListener, actionListener);
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void doClick() {
		if (actionListener != null)
			actionListener.actionPerformed();
	}

	public void mousePressed(final MouseEvent mouse) {
		super.mousePressed(mouse);
		doClick();
	}
}
