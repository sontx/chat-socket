package com.blogspot.sontx.chatsocket.lib.view;

import javax.swing.*;
import java.awt.*;

public class FitImageJLabel extends JLabel {
	private static final long serialVersionUID = -753861462866544072L;

	private float getScaleValue(int iconWidth, int iconHeight) {
		int deltaWidth = iconWidth - getWidth();
		int deltaHeight = iconHeight - getHeight();
		return deltaWidth > deltaHeight ? getWidth() / (float) iconWidth : getHeight() / (float) iconHeight;
	}

	private Icon convertIconToFit(Icon icon) {
		ImageIcon imageIcon = (ImageIcon) icon;
		Image image = imageIcon.getImage();
		float scale = getScaleValue(icon.getIconWidth(), icon.getIconHeight());
		Image scaledImage = image.getScaledInstance((int) (scale * icon.getIconWidth()),
				(int) (scale * icon.getIconHeight()), Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon != null ? convertIconToFit(icon) : null);
	}
}
