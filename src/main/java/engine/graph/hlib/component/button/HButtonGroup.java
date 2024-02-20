package engine.graph.hlib.component.button;

import java.util.ArrayList;

import engine.graph.hlib.event.listener.ActionListener;

public class HButtonGroup {

	private final ArrayList<CheckableButton> buttons = new ArrayList<>();

	private final ArrayList<ActionListener> listeners = new ArrayList<>();

	private int indexSelected = -1;

	public HButtonGroup() {
	}

	public HButtonGroup(final CheckableButton... buttons) {
		for (int i = 0, c = buttons.length; i < c; i++)
			addButton(buttons[i]);
	}

	public void addButton(final CheckableButton but) {
		final int i = buttons.size();
		buttons.add(but);

		final ActionListener list = () -> {
			if (but.isSelected()) {
				if (indexSelected != -1)
					buttons.get(indexSelected).setSelected(false);
				indexSelected = i;
			} else
				indexSelected = -1;
		};

		but.addActionListener(list);
		listeners.add(list);
	}

	public boolean removeButton(final CheckableButton but) {
		boolean removed;
		final int index = buttons.indexOf(but);
		if (removed = index != -1) {
			buttons.remove(index).removeActionListener(listeners.remove(index));
			indexSelected = -1;
		}
		return removed;
	}

	public ArrayList<CheckableButton> getButtons() {
		return buttons;
	}
}
