package in.sontx.tut.chatsocket.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class ClickableJLabel extends JLabel {
	private static final long serialVersionUID = -8953610647879519149L;
	private List<ActionListener> actionListeners = new ArrayList<>();

	public void addActionListener(ActionListener l) {
		actionListeners.add(l);
	}

	public void removeActionListener(ActionListener l) {
		actionListeners.remove(l);
	}

	public ClickableJLabel(String s) {
		super(s);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionEvent event = new ActionEvent(ClickableJLabel.this, ActionEvent.ACTION_PERFORMED, "clicked");
				for (int i = 0; i < actionListeners.size(); i++) {
					actionListeners.get(i).actionPerformed(event);
				}
			}
		});
	}
}
