package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import PlanetCrash.core.Game.Game;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.StarryBackground;

public class LoadingScene extends Scene{

	public LoadingScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	@Override
	public Component create() {
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		panel.add(s, new Integer(0),0);

		//Add background
		StarryBackground sb = new StarryBackground();
		s.add(sb);
		
		//Add loading label
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);
		JTextPane loading = new JTextPane(document);
		
		loading.setText("Loading, please wait...");
		loading.setForeground(Color.pink);
		loading.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
		loading.setEditable(false);
		loading.setBackground(new Color(0,0,0,120));
		loading.setBorder(BorderFactory.createEmptyBorder());
		int mw=(int)loading.getPreferredSize().getWidth(), mh=(int)loading.getPreferredSize().getHeight();
		loading.setBounds((GameGUI.WINDOW_WIDTH-mw)/2, (GameGUI.WINDOW_HEIGHT-mh)/2, mw, mh);
		panel.add(loading,new Integer(1),0);
		
		final Timer timer = new Timer(0, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				waitForPlanetName();
				PlanetScene ps = new PlanetScene(gameGUI, game);
				gameGUI.fadeSwitchScene(ps);
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				waitForPlanetName();
				timer.setRepeats(false);
				timer.start();
			}
		}).start();
		
		return panel;
	}

	private void waitForPlanetName() {
		while(game.getPlanetName()==null)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}
	}
	
}
