package engine.graph.hlib.event;

import engine.graph.hlib.event.listener.ActionListener;
import engine.graph.hlib.event.listener.EventListener;
import engine.graph.hlib.event.listener.KeyListener;
import engine.graph.hlib.event.listener.MouseListener;

public class EventMulticaster implements ActionListener, KeyListener, MouseListener {

	private EventListener eventA;
	
	private EventListener eventB;

	public EventMulticaster(final EventListener eventA, final EventListener eventB) {
		this.eventA = eventA;
		this.eventB = eventB;
	}

	@Override
	public void fire(final MouseEvent mouse) {
		((MouseListener) eventA).fire(mouse);
		((MouseListener) eventB).fire(mouse);
	}

	@Override
	public void fire(final KeyEvent key) {
		((KeyListener) eventA).fire(key);
		((KeyListener) eventB).fire(key);
	}

	@Override
	public void actionPerformed() {
		((ActionListener) eventA).actionPerformed();
		((ActionListener) eventB).actionPerformed();
	}

	public static ActionListener add(final ActionListener a, final ActionListener b) {
		return (ActionListener) addAny(a, b);
	}

	public static KeyListener add(final KeyListener a, final KeyListener b) {
		return (KeyListener) addAny(a, b);
	}

	public static MouseListener add(final MouseListener a, final MouseListener b) {
		return (MouseListener) addAny(a, b);
	}

	public static ActionListener remove(final ActionListener a, final ActionListener b) {
		return (ActionListener) removeAny(a, b);
	}

	public static KeyListener remove(final KeyListener a, final KeyListener b) {
		return (KeyListener) removeAny(a, b);
	}

	public static MouseListener remove(final MouseListener a, final MouseListener b) {
		return (MouseListener) removeAny(a, b);
	}

	public static EventListener addAny(final EventListener a, final EventListener b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		return new EventMulticaster(a, b);
	}

	protected static EventListener removeAny(final EventListener l, final EventListener oldl) {
		if (l == oldl || l == null) {
			return null;
		} else if (l instanceof EventMulticaster) {
			return ((EventMulticaster) l).remove(oldl);
		} else {
			return l; // it's not here
		}
	}

	protected EventListener remove(final EventListener oldl) {
		if (oldl == eventA)
			return eventB;
		if (oldl == eventB)
			return eventA;
		final EventListener a2 = removeAny(eventA, oldl), b2 = removeAny(eventB, oldl);
		if (a2 == eventA && b2 == eventB)
			return this; // it's not here

		return addAny(a2, b2);
	}
}
