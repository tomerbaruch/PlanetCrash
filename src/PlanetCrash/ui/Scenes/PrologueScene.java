package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import PlanetCrash.core.Game.Game;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;

public class PrologueScene extends Scene {

	public PrologueScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	@Override
	public Component create() {
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		s.setLayout(null);
		panel.add(s, new Integer(0),0);

		//Add comm center background
		JImage bg = new JImage(new File(GameGUI.ASSETS+"commcenter.png"));
		bg.setBounds(0,0,bg.getWidth(),bg.getHeight());
		panel.add(bg, new Integer(1),(0));
		
		//Add Speech bubble
		JRoundedButton speechBubble= new JRoundedButton("", GameGUI.WINDOW_WIDTH, 100, 2);
		speechBubble.setBounds(0, GameGUI.WINDOW_HEIGHT-speechBubble.getHeight(), speechBubble.getWidth(), speechBubble.getHeight());
		speechBubble.isButton(false);
		panel.add(speechBubble, new Integer(2),0);
		
		//Add actual speech
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);
		JTextPane message = new JTextPane(document);
		message.setText("Captain! We have crashed on the weird planet "+game.getPlanetName()+"."
				+"\nSomething about it reminds me of "+game.getCountry()+", but I can't point my finger at it."
				+"\nTo return home, we must retrieve our ship's power core, \nbut our way is blocked "
				+"by hostile, inquisitive life forms!");
		message.setForeground(Color.orange);
		message.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
		message.setEditable(false);
		message.setBackground(new Color(0,0,0,0));
		message.setBorder(BorderFactory.createEmptyBorder());
		int mw=(int)message.getPreferredSize().getWidth(), mh=(int)message.getPreferredSize().getHeight();

		speechBubble.add(message);
		
		//Add play button
		JRoundedButton playBtn = new JRoundedButton("Play", 100, 60, 2);
		playBtn.setBorderColor(Color.green);
		playBtn.setBounds(GameGUI.WINDOW_WIDTH-playBtn.getWidth(), GameGUI.WINDOW_HEIGHT-speechBubble.getHeight()-playBtn.getHeight(), playBtn.getWidth(), playBtn.getHeight());
		panel.add(playBtn, new Integer(4),0);

		//Register listeners
		playBtn.addMouseListener(new NextMouseListener());

		return panel;
	}

	class NextMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			GameScene gs = new GameScene(gameGUI, game);
			gameGUI.fadeSwitchScene(gs);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
