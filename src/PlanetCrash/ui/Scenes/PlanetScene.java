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

public class PlanetScene extends Scene {

	public PlanetScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	@Override
	public Component create() {
		
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		s.setLayout(null);
		s.setBackground(game.getLand());
		panel.add(s, new Integer(0),0);
		
		//Add overlay
		JImage planetoverlay = new JImage(GameScene.randomFile(GameGUI.ASSETS+"Overlays/Planet"));
		planetoverlay.setOpaque(false);
		panel.add(planetoverlay, new Integer(1),0);
		
		//Add frame
		JImage planetFrame = new JImage(new File(GameGUI.ASSETS+"planetframe.png"));
		planetFrame.setOpaque(false);
		panel.add(planetFrame,new Integer(2),0);
		
		//Add title
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);
		JTextPane message = new JTextPane(document);
		message.setText("You have crashed on planet:");
		message.setForeground(invertColor(game.getLand()));
		message.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
		message.setEditable(false);
		message.setBackground(new Color(0,0,0,120));
		message.setBorder(BorderFactory.createEmptyBorder());
		int mw=(int)message.getPreferredSize().getWidth(), mh=(int)message.getPreferredSize().getHeight();
		message.setBounds((GameGUI.WINDOW_WIDTH-mw)/2, 20, mw, mh);
		panel.add(message,new Integer(3),0);
		
		//Add planet name
		document = new DefaultStyledDocument();
		defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);

		JTextPane pName = new JTextPane(document);
		pName.setText(game.getPlanetName());
		pName.setForeground(invertColor(game.getLand()));
		pName.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,(game.getPlanetName().length()<30?50:30)));
		pName.setEditable(false);
		pName.setBackground(new Color(0,0,0,120));
		pName.setBorder(BorderFactory.createEmptyBorder());
		int pw=(int)pName.getPreferredSize().getWidth(), ph=(int)pName.getPreferredSize().getHeight();
		pName.setBounds((GameGUI.WINDOW_WIDTH-pw)/2, (GameGUI.WINDOW_HEIGHT-ph)/2, pw, ph);
		panel.add(pName,new Integer(3),1); 
		
		//Add next button
		JRoundedButton nextBtn = new JRoundedButton("Next", 100, 60, 2);
		nextBtn.setBorderColor(Color.green);
		nextBtn.setBounds(GameGUI.WINDOW_WIDTH-100-30, 500, nextBtn.getWidth(), nextBtn.getHeight());
		panel.add(nextBtn, new Integer(3),2);
		
		//Register listeners
		nextBtn.addMouseListener(new NextMouseListener());
		
		return panel;
	}
	

	
	private Color invertColor(Color c) {
		int r,g,b;
		r=255-c.getRed();
		g=255-c.getGreen();
		b=255-c.getBlue();
		
		return new Color(r,g,b);
	}
	
	class NextMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			PrologueScene ps = new PrologueScene(gameGUI, game);
			gameGUI.fadeSwitchScene(ps);
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
