package PlanetCrash.ui.Scenes;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import PlanetCrash.core.Game.Game;
import PlanetCrash.ui.GameGUI;


public abstract class Scene {
	protected Game game;
	protected GameGUI gameGUI;
	
	public Scene(GameGUI gameGUI, Game game) {
		this.game=game;
		this.gameGUI=gameGUI;
	}
	
	public abstract Component create();
	

	public static JPanel emptyMainJPanel() {
		JPanel ret = new JPanel();
		ret.setSize(GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		ret.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));
		ret.setBorder(new EmptyBorder(-5, -5, -5, -5));

		return ret;
	}
	
	public static JLayeredPane emptyMainJLayeredPane() {
		JLayeredPane ret = new JLayeredPane();
		ret.setSize(GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		ret.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));
		ret.setBorder(new EmptyBorder(-5, -5, -5, -5));

		return ret;
	}
}
