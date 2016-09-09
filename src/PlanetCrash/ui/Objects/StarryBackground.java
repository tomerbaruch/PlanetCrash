package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import PlanetCrash.core.config.Config;
import PlanetCrash.ui.GameGUI;

public class StarryBackground extends JPanel{

	StarSpawner sspwnr;
	Thread animation;

	public StarryBackground() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		if(new Config().get_high_graphics()==1) {
			sspwnr = new StarSpawner(GameGUI.WINDOW_WIDTH/2, GameGUI.WINDOW_HEIGHT/2, 100, 0.4f);

			add(sspwnr);
			animation = new Thread(sspwnr);
			animation.start();
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT);
	}


}
